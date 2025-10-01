package com.naipy.alpha.modules.utils.models;

import java.io.Serializable;
import java.util.List;

public record ItemsPaginatorResponse<T>(
        List<T> items,
        long totalItems,
        int totalPages,
        Integer currentPage) implements Serializable {
}
