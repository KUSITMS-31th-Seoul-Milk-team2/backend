package com.seoulmilk.emp.dto.request;

import java.util.List;

public record DeleteEmpsRequest(
        List<Long> ids
) {
}
