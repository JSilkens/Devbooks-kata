package be.jsilkens.devbooks.common.domain;

public record PaginationMetadata(
        int currentPage,
        int pageSize,
        long totalItems,
        int totalPages
) {}
