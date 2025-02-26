package com.seoulmilk.invoice.domain.value;

import com.seoulmilk.invoice.exception.InvoiceErrorCode;

import java.util.List;

public record FileMetaData(String filename, String contentType) {
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "pdf");
    private static final List<String> ALLOWED_MIME_TYPES = List.of("image/jpeg", "image/png", "application/pdf");

    public FileMetaData {
        validateExtension(filename);
        validateContentType(contentType);
    }

    private void validateExtension(String filename) {
        String extension = getExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw InvoiceErrorCode.NOT_SUPPORTED_EXTENSION.toException();
        }
    }

    private void validateContentType(String contentType) {
        if (!ALLOWED_MIME_TYPES.contains(contentType)) {
            throw InvoiceErrorCode.NOT_SUPPORTED_FILE.toException();
        }
    }

    public String getBaseFilename(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(0, lastDotIndex) : filename;
    }

    public String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0
                ? filename.substring(lastDotIndex + 1).toLowerCase()
                : "";
    }

}
