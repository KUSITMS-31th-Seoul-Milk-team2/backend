package com.seoulmilk.invoice.infrastructure.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceRegexTest {
    @Nested
    @DisplayName("NON_DIGIT 테스트")
    class NonDigitTest {
        @ParameterizedTest
        @CsvSource({
                "'A1B2-C3', '123'",
                "'', ''",
                "'12345', '12345'",
                "'!@#\\$%^', ''"
        })
        void removeFrom_validInput(String input, String expected) {
            assertEquals(expected, InvoiceRegex.NON_DIGIT.removeFrom(input));
        }

        @Test
        void getter_검증() {
            assertEquals("[^0-9]", InvoiceRegex.NON_DIGIT.getPattern());
            assertEquals("숫자가 아닌 문자 제거", InvoiceRegex.NON_DIGIT.getDescription());
        }
    }

    @Nested
    @DisplayName("BUSINESS_REGISTER_NUMBER 테스트")
    class BusinessRegisterNumberTest {
        @ParameterizedTest
        @CsvSource({
                "1234567890, true",
                "1234567890123, true",
                "12345, false",
                "ABCD567890, false",
                "123-45-6789, false"
        })
        void 패턴_매칭_검증(String input, boolean expected) {
            boolean actual = InvoiceRegex.BUSINESS_REGISTER_NUMBER
                    .getCompiledPattern()
                    .matcher(input)
                    .matches();
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("HYPHEN 테스트")
    class HyphenTest {
        @ParameterizedTest
        @CsvSource({
                "'2025-03-11', '20250311'",
                "'no-hyphen', 'nohyphen'",
                "'A-B-C', 'ABC'"
        })
        void removeFrom_validInput(String input, String expected) {
            assertEquals(expected, InvoiceRegex.HYPHEN.removeFrom(input));
        }
    }

    @Nested
    @DisplayName("WHITESPACE 테스트")
    class WhitespaceTest {
        @ParameterizedTest
        @CsvSource({
                "'Hello  World', 'HelloWorld'",
                "'Tab\tText', 'TabText'",
                "'NoSpace', 'NoSpace'"
        })
        void removeFrom_validInput(String input, String expected) {
            assertEquals(expected,
                    InvoiceRegex.WHITESPACE.removeFrom(input));
        }

        @Test
        void 복합_공백_제거() {
            String input = "  Multiple\nSpaces\tHere  ";
            String result = input
                    .replaceAll(InvoiceRegex.WHITESPACE.getPattern(), "");
            assertEquals("MultipleSpacesHere", result);
        }
    }
}
