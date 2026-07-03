package io.naga.commerce.domain.order.dto.request;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCreateRequest(
    @Valid
    @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
    List<InnerOrderCreateItemRequest> items
) {

    public Map<Integer, Integer> getQuantitiesByProductId() {
        return items.stream()
            .collect(Collectors.toMap(
                InnerOrderCreateItemRequest::productId,
                InnerOrderCreateItemRequest::quantity,
                Integer::sum
            ));
    }

    public Set<Integer> getProductIds() {
        return items.stream()
            .map(InnerOrderCreateItemRequest::productId)
            .collect(Collectors.toSet());
    }

    public record InnerOrderCreateItemRequest(
        @NotNull(message = "상품 ID는 필수입니다.")
        Integer productId,

        @NotNull(message = "주문 수량은 필수입니다.")
        @Positive(message = "주문 수량은 1개 이상이어야 합니다.")
        Integer quantity
    ) {
    }
}
