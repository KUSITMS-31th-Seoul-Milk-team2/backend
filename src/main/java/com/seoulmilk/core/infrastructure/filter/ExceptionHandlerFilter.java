package com.seoulmilk.core.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seoulmilk.core.exception.DomainException;
import com.seoulmilk.core.presentation.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Log4j2
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (DomainException ex) {
            setErrorResponse(response, ex);
        } catch (RuntimeException ex) {
            setErrorResponse(response, ex);
        }
    }

    private void setErrorResponse(HttpServletResponse response, RuntimeException ex) throws IOException {
        if (ex instanceof DomainException) {
            log.error("ERROR ::: [DomainException] ", ex);
            HttpStatus httpStatus = Optional.ofNullable(((DomainException) ex).getHttpStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
            ErrorResponse errorResponse = ErrorResponse.createDomainErrorResponse().statusCode(httpStatus.value()).exception((DomainException) ex).build();
            throwException(response, errorResponse);
        } else {
            log.error("ERROR ::: [RuntimeException] ", ex);
            ErrorResponse errorResponse = ErrorResponse.createErrorResponse().statusCode(500).exception(ex).build();
            throwException(response, errorResponse);
        }
    }

    private void throwException(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatusCode());
        String json = objectMapper.writeValueAsString(errorResponse);
        byte[] utf8Bytes = json.getBytes(StandardCharsets.UTF_8);
        response.getOutputStream().write(utf8Bytes);
    }
}
