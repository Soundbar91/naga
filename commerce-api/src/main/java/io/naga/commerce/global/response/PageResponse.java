package io.naga.commerce.global.response;

import java.util.List;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
    List<T> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages,
    Boolean hasNext
) {

    public static <S> PageResponse<S> from(Page<S> page) {
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext()
        );
    }
}
