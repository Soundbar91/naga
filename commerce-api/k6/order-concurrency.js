import http from 'k6/http';
import { check, fail } from 'k6';
import exec from 'k6/execution';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080';
const USER_COUNT = 500;
const PRODUCT_ID = 1;
const ORDER_QUANTITY = 1;
const EXPECTED_SUCCESS = 100;
const EXPECTED_CONFLICT = USER_COUNT - EXPECTED_SUCCESS;
const LOGIN_PASSWORD = 'password';

const orderSuccess = new Counter('order_success');
const orderConflict = new Counter('order_conflict');
const orderUnexpected = new Counter('order_unexpected');

export const options = {
  scenarios: {
    create_orders: {
      executor: 'shared-iterations',
      vus: USER_COUNT,
      iterations: USER_COUNT,
      maxDuration: '1m',
    },
  },
  thresholds: {
    checks: ['rate == 1'],
    order_success: [`count == ${EXPECTED_SUCCESS}`],
    order_conflict: [`count == ${EXPECTED_CONFLICT}`],
    order_unexpected: ['count == 0'],
  },
};

export function setup() {
  const tokens = [];

  for (let userNo = 1; userNo <= USER_COUNT; userNo += 1) {
    const loginId = `test-user-${String(userNo).padStart(3, '0')}`;
    const response = http.post(
      `${BASE_URL}/v1/auth/login`,
      JSON.stringify({
        loginId,
        password: LOGIN_PASSWORD,
      }),
      {
        headers: {
          'Content-Type': 'application/json',
        },
        tags: {
          name: 'login',
        },
      },
    );

    const ok = check(response, {
      'login status is 200': (res) => res.status === 200,
      'login response has access token': (res) => Boolean(res.json('data.accessToken')),
    });

    if (!ok) {
      fail(`failed to login user ${loginId}: status=${response.status}, body=${response.body}`);
    }

    tokens.push(response.json('data.accessToken'));
  }

  return { tokens };
}

export default function (data) {
  const index = exec.scenario.iterationInTest;
  const token = data.tokens[index];

  const response = http.post(
    `${BASE_URL}/v1/orders`,
    JSON.stringify({
      items: [
        {
          productId: PRODUCT_ID,
          quantity: ORDER_QUANTITY,
        },
      ],
    }),
    {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      tags: {
        name: 'create_order',
      },
    },
  );

  const expected = check(response, {
    'order status is 200 or 409': (res) => res.status === 200 || res.status === 409,
    'order conflict has expected error code': (res) => {
      if (res.status !== 409) {
        return true;
      }
      const errorCode = res.json('error.code');
      return errorCode === 'OUT_OF_STOCK' || errorCode === 'NOT_SALE_PRODUCT';
    },
  });

  if (response.status === 200) {
    orderSuccess.add(1);
    return;
  }

  if (response.status === 409) {
    orderConflict.add(1);
    return;
  }

  if (!expected) {
    orderUnexpected.add(1);
  }
}
