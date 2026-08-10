package io.naga.commerce.global.response;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages,
    Boolean hasNext
) {

    public static <S> PageResponse<S> of(
        List<S> content,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean hasNext
    ) {
        return new PageResponse<>(
            content,
            page,
            size,
            totalElements,
            totalPages,
            hasNext
        );
    }
}
