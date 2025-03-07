package com.seoulmilk.receipt.dto.request;

import java.util.List;

public record ValidResponseSearchRequest(
        List<String> employeeName,
        List<String> suNames,
        List<String> ipNames,
        String erdatStart,
        String erdatEnd
){

}