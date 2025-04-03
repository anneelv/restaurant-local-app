package org.anneelv.burritokingv2.Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* The Validator class responsible for validating various
* types of data such as time, credit card details,
* expiry date, points, and email addresses.
* */
public class Validator {
    // Regular expressions for validating time and expiry date
    String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d)$";
    String expiryPattern = "^(0[1-9]|1[0-2])/\\d{4}$";

    /*
     * Checks if the provided time string is in the correct
     * format and represents a valid time.
     */
    public boolean isValidTime(String time){
        if (!time.matches(timePattern)) {
            return false;
        }

        // Further validation by parsing the time
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(time, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /*
     * Checks if the provided credit card number is valid.
     */
    public boolean isValidCardNumber(String cardNumber){
        String sanitizedCardNumber = cardNumber.replaceAll("\\s", "");
        return sanitizedCardNumber.matches("\\d{16}");
    }

    /*
     * Checks if the provided CVV (Card Verification Value) is valid.
     */
    public boolean isValidCardCVV(String cardCVV){
        String sanitizedCardCVV = cardCVV.replaceAll("\\s", "");
        return sanitizedCardCVV.matches("\\d{3}");
    }

    /*
     * Checks if the provided expiry date string is in the correct
     * format and represents an active card.
     */
    public boolean isActiveCard(String expiryDate){
        if (!expiryDate.matches(expiryPattern)) {
            return false;
        }

        try {
            // Parse the expiry date
            LocalDate expiry = LocalDate.parse("01/" + expiryDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate currentDate = LocalDate.now();

            // Check if the card is not expired
            return !expiry.isBefore(currentDate.withDayOfMonth(1));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /*
     * Checks if the provided point redemption amount is valid based on the collected points.
     */
    public boolean isValidPoint(String pointTF, int pointsCollected){
        try {
            int pointsToRedeem = Integer.parseInt(pointTF);
            if (pointsToRedeem > 0 && pointsToRedeem <= pointsCollected && (pointsToRedeem % 50 == 0 || pointsToRedeem % 100 == 0)) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
    }

    /*
     * Checks if the provided email address is valid.
     */
    public boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
