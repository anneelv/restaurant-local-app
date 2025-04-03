package org.anneelv.burritokingv2.Controllers;

import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
    private final Validator validator = new Validator();

    /*Test all possible valid times*/
    @ParameterizedTest
    @ValueSource(strings = {"00:00", "23:59", "12:34", "01:05", "10:02"})
    void testValidTimes(String time) {
        assertTrue(validator.isValidTime(time), "Valid time case failed for: " + time);
    }

    /*Test all possible invalid times, null is not included since there is another
     * validator to ensure it is not null before going to this method*/
    @ParameterizedTest
    @ValueSource(strings = {
            "24:00",   // Hour out of range
            "12:60",   // Minute out of range
            "3:00",    // Invalid hour format
            "12:5",    // Invalid minute format
            "ab:cd",   // Non-numeric characters
            "1234",    // Missing colon
            "12:34pm", // Additional characters
            "001:00",  // Extra number in front
            "",        // Empty string
            "12: 34",  // Space in between numbers
            "23:60",   // Hour at boundary but invalid minute
            "24:59",   // Minute at boundary but invalid hour
            "-01:00"   // Negative time
    })
    void testInvalidTimes(String time) {
        assertFalse(validator.isValidTime(time), "Invalid time case failed for: " + time);
    }

    /*Test all possible valid card number input*/
    @ParameterizedTest
    @ValueSource(strings = {
            "1234567812345678", // valid 16 digits
            "1234 5678 1234 5678", // valid 16 digits with spaces
            "1234 5678 1234 5678  ", // trailing spaces
            "  1234 5678 1234 5678" // leading spaces
    })
    void testValidCardNumber(String cardNumber) {
        assertTrue(validator.isValidCardNumber(cardNumber), "Valid card number case failed for: " + cardNumber);
    }

    /*Test all possible invalid ard number input*/
    @ParameterizedTest
    @ValueSource(strings = {
            "123456781234567", // 15 digits
            "12345678123456789", // 17 digits
            "1234 5678 1234 567", // 15 digits with spaces
            "abcd efgh ijkl mnop", // non-digit characters
            "1234-5678-1234-5678", // special characters
            "", // empty string
    })
    void testInvalidCardNumber(String cardNumber) {
        assertFalse(validator.isValidCardNumber(cardNumber), "Invalid card number case failed for: " + cardNumber);
    }

    /*Test all possible valid card CVV*/
    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "456",
            "709",
            " 829", // leading spaces
            "012 ", // spaces at the end
            "12 3" // space in between
    })
    void testValidCardCVV(String cardCVV) {
        assertTrue(validator.isValidCardCVV(cardCVV), "Valid card CVV case failed for: " + cardCVV);
    }

    /*Test all possible card CVV number input*/
    @ParameterizedTest
    @ValueSource(strings = {
            "12", // 2 digits
            "1234", // 4 digits
            "12a", // non-digit characters
            "abc", // all non-digit characters
            "" // empty string
    })
    void testInvalidCardCVV(String cardCVV) {
        assertFalse(validator.isValidCardCVV(cardCVV), "Invalid card CVV case failed for: " + cardCVV);
    }


    /*Test all possible invalid card CVV number input*/
    @ParameterizedTest
    @CsvSource({
            "01/2030, true", // valid future date
            "12/2022, false", // past date
            "05/2024, true", // current month and year
            "13/2023, false", // invalid month
            "00/2023, false", // invalid month
            "11/20, false", // invalid year format
            "11-2023, false", // invalid date format
            "112023, false" // no separator
    })
    void testIsActiveCard(String expiryDate, boolean expected) {
        assertEquals(expected, validator.isActiveCard(expiryDate), "Active card case failed for: " + expiryDate);
    }

    // Testing isValidPoint method for both valid and invalid numbers
    @ParameterizedTest
    @CsvSource({
            "100, 200, true", // valid points, multiple of 50 and 100
            "50, 50, true", // exact match, multiple of 50
            "150, 150, true", // exact match, multiple of 50 and 100
            "75, 200, false", // multiple of 25, not 50 or 100
            "30, 200, false", // multiple of 10, not 50 or 100
            "1000, 200, false", // exceeds points collected
            "abc, 200, false", // non-numeric
            ", 200, false", // empty string
            "-50, 200, false", // negative number
            "50, 0, false" // points collected is zero
    })
    void testIsValidPoint(String pointTF, int pointsCollected, boolean expected) {
        assertEquals(expected, validator.isValidPoint(pointTF, pointsCollected), "Valid point case failed for: " + pointTF);
    }

    /*Testing valid email strings*/
    @ParameterizedTest
    @ValueSource(strings = {
            "email@example.com",
            "firstname.lastname@example.com",
            "email@subdomain.example.com",
            "firstname+lastname@example.com",
            "1234567890@example.com",
            "email@example-one.com",
            "_______@example.com",
            "email@example.name",
            "email@example.museum",
            "email@example.co.jp",
            "firstname-lastname@example.com"
    })
    void testValidEmail(String email) {
        assertTrue(validator.isValidEmail(email), "Valid email case failed for: " + email);
    }

    /*Testing invalid email strings*/
    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missingusername.com",
            "email.example.com", // missing @
            "email@example@example.com", // two @ symbols
            ".email@example.com", // leading dot in address
            "email.@example.com", // trailing dot in address
            "email@123.123.123.123",
            "email@[123.123.123.123]",
            "\"email\"@example.com",
            "email..email@example.com", // multiple dots
            "email@example.com (Joe Smith)", // text followed by email
            "email@example", // missing top level domain
            "email@111.222.333.44444", // invalid IP format
            "email@example..com", // multiple dots in domain
            "Abc..123@example.com", // multiple dots in local part
            "", // empty string
    })
    void testInvalidEmail(String email) {
        assertFalse(validator.isValidEmail(email), "Invalid email case failed for: " + email);
    }
}