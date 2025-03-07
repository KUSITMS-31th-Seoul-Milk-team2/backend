package com.seoulmilk.receipt.dto.request;

import java.util.List;

public record ValidResponseSearchRequest(
        List<String> employeeId,
        List<String> suNames,
        List<String> ipNames,
        String erdatStart,
        String erdatEnd
){

}