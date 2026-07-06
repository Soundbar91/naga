import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';
import { SharedArray } from 'k6/data';
import exec from 'k6/execution';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const PRODUCT_ID = Number(__ENV.PRODUCT_ID || 1);
const USER_COUNT = Number(__ENV.USER_COUNT || 300);
const ORDER_QUANTITY = Number(__ENV.ORDER_QUANTITY || 1);
const EXPECTED_SUCCESS_COUNT = Number(__ENV.EXPECTED_SUCCESS_COUNT || 100);

const users = new SharedArray('mock users', function () {
    return JSON.parse(open('./data/users.json'));
});

http.setResponseCallback(
    http.expectedStatuses(200, 409)
);

const orderSuccessCount = new Counter('order_success_count');
const orderConflictCount = new Counter('order_conflict_count');
const orderUnexpectedFailureCount = new Counter('order_unexpected_failure_count');

export const options = {
    setupTimeout: '5m',

    scenarios: {
        order_consistency: {
            executor: 'per-vu-iterations',
            vus: USER_COUNT,
            iterations: 1,
            maxDuration: '1m',
        }
    },

    thresholds: {
        order_unexpected_failure_count: ['count==0'],
        order_success_count: [`count==${EXPECTED_SUCCESS_COUNT}`],
        order_conflict_count: [`count==${USER_COUNT - EXPECTED_SUCCESS_COUNT}`],
    },
};

export function setup() {
    if (users.length < USER_COUNT) {
        throw new Error(
            `users.json의 사용자 수가 부족합니다. 필요: ${USER_COUNT}, 현재: ${users.length}`
        );
    }

    const selectedUsers = users.slice(0, USER_COUNT);
    const tokens = [];

    for (const user of selectedUsers) {
        const loginResponse = http.post(
            `${BASE_URL}/v1/auth/login`,
            JSON.stringify({
                loginId: user.loginId,
                password: user.password,
            }),
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        const loginOk = check(loginResponse, {
            'login status is 200': (res) => res.status === 200,
            'access token exists': (res) => Boolean(res.json('data.accessToken')),
        });

        if (!loginOk) {
            throw new Error(
                `로그인 실패: loginId=${user.loginId}, status=${loginResponse.status}, body=${loginResponse.body}`
            )
        }

        tokens.push(loginResponse.json('data.accessToken'));
    }

    return {
        tokens,
    };
}

export default function (data) {
    const userIndex = exec.vu.idInTest - 1;
    const accessToken = data.tokens[userIndex];

    const orderResponse = http.post(
        `${BASE_URL}/v1/orders`,
        JSON.stringify({
            items: [
                {
                    productId: PRODUCT_ID,
                    quantity: ORDER_QUANTITY,
                }
            ]
        }),
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`,
            },
        }
    );

    if (orderResponse.status === 200) {
        orderSuccessCount.add(1);
        check(orderResponse, {
            'order success status is 200': (res) => res.status === 200,
        });

        return;
    }

    if (orderResponse.status === 409) {
        orderConflictCount.add(1);
        check(orderResponse, {
            'order conflict status is 409': (res) => res.status === 409,
            'order conflict has expected error code': (res) => {
                const code = res.json('error.code');
                return code === 'OUT_OF_STOCK' || code === 'NOT_SALE_PRODUCT';
            },
        });

        return;
    }

    orderUnexpectedFailureCount.add(1);
    check(orderResponse, {
        'order response is expected status': (res) =>
            res.status === 200 || res.status === 409,
    });
}
