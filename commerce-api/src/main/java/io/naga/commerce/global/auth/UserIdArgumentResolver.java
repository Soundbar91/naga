package io.naga.commerce.global.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.naga.common.error.BusinessException;
import io.naga.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserIdContext userIdContext;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        Integer userId = userIdContext.getUserId();
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "missing access token");
        }
        return userId;
    }
}
