package be.jsilkens.devbooks.common.domain;

import java.util.List;

public record PaginatedResult<T>(
        List<T> items,
        PaginationMetadata metadata
) {}
