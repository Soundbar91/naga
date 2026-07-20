package io.naga.pg.domain.payment.support;

import static io.naga.common.error.ErrorCode.BAD_REQUEST;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import io.naga.common.error.BusinessException;
import io.naga.common.error.ErrorCode;
import io.naga.pg.domain.payment.model.Payment;

@Component
public class PaymentRedirectUrlBuilder {

    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";

    public void validate(String redirectUrl, String parameterName) {
        createBuilder(redirectUrl, parameterName);
    }

    public URI buildSuccessUrl(String successUrl, Payment payment) {
        return createBuilder(successUrl, "successUrl")
            .queryParam("paymentKey", payment.getPaymentKey())
            .queryParam("orderId", payment.getOrderId())
            .queryParam("amount", payment.getAmount())
            .build()
            .encode()
            .toUri();
    }

    public URI buildFailUrl(String failUrl, ErrorCode errorCode, String orderId) {
        return createBuilder(failUrl, "failUrl")
            .queryParam("code", errorCode.name())
            .queryParam("message", errorCode.getMessage())
            .queryParam("orderId", orderId == null ? "" : orderId)
            .build()
            .encode()
            .toUri();
    }

    private UriComponentsBuilder createBuilder(String redirectUrl, String parameterName) {
        if (!StringUtils.hasText(redirectUrl)) {
            throw BusinessException.of(BAD_REQUEST, parameterName + " is required");
        }

        try {
            URI uri = URI.create(redirectUrl);
            if (!isHttpUrl(uri)) {
                throw BusinessException.of(BAD_REQUEST, parameterName + " must be an absolute HTTP URL");
            }
            return UriComponentsBuilder.fromUri(uri);
        } catch (IllegalArgumentException exception) {
            throw BusinessException.of(BAD_REQUEST, parameterName + " is invalid");
        }
    }

    private boolean isHttpUrl(URI uri) {
        String scheme = uri.getScheme();
        return uri.isAbsolute()
            && StringUtils.hasText(uri.getHost())
            && (HTTP_SCHEME.equalsIgnoreCase(scheme) || HTTPS_SCHEME.equalsIgnoreCase(scheme));
    }
}
