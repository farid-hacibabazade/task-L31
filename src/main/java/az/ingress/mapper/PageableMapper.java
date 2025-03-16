package az.ingress.mapper;

import az.ingress.model.response.PageableResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public enum PageableMapper {
    PAGEABLE_MAPPER;

    public <T, E> PageableResponse<T> buildPageableResponse(Page<E> page, Function<E, T> mapper) {
        return PageableResponse.<T>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .pageCount(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNextPage(page.hasNext())
                .build();
    }
}
