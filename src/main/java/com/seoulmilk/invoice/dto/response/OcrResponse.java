package com.seoulmilk.invoice.dto.response;

import java.util.List;

public record OcrResponse(
        String version,
        String requestId,
        long timestamp,
        List<ImageResult> images
) {
    public record ImageResult(
            String uid,
            String name,
            String inferResult,
            String message,
            MatchedTemplate matchedTemplate,
            ValidationResult validationResult,
            List<Field> fields,
            Title title
    ) {


        public record MatchedTemplate(
                int id,
                String name
        ) {
        }

        public record ValidationResult(
                String result
        ) {
        }

        public record Field(
                String name,
                Bounding bounding,
                String valueType,
                String inferText,
                double inferConfidence
        ) {
        }

        public record Title(
                String name,
                Bounding bounding,
                String inferText,
                double inferConfidence
        ) {
        }

        public record Bounding(
                double top,
                double left,
                double width,
                double height
        ) {
        }


    }
}
