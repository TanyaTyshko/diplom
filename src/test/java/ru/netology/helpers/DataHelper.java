package ru.netology.helpers;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Calendar;


import java.util.Locale;

public class DataHelper {

    private DataHelper() {
    }

    public static String approvedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String declinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getStatusApproved() {
        return "APPROVED";
    }

    public static String getStatusDeclined() {
        return "DECLINED";
    }

    public static String generateCardNumber(String status) {
        if (status == getStatusApproved()) {
            return approvedCardNumber();
        }
        if (status == getStatusDeclined()) {
            return declinedCardNumber();
        }
        return "Allowed Statuses: APPROVED|DECLINED";
    }

    public static String generateCardHolder(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    public static String generateMonth(boolean wrongMonth) {
        Faker faker = new Faker();
        return wrongMonth ? 
            String.format("%02d", faker.number().numberBetween(13, 33)) :
            String.format("%02d", faker.number().numberBetween(1, 12));
    }

    public static String generateYear() {
        Faker faker = new Faker();
        int startYear = Calendar.getInstance().get(Calendar.YEAR) - 2000 + 1;
        int endYear = startYear + 4;
        return String.valueOf(faker.number().numberBetween(startYear, endYear));
    }

    public static String getYearWithOffset(int offset) {
        int offsetYear = Calendar.getInstance().get(Calendar.YEAR) + offset;
        return String.valueOf(offsetYear).substring(2);
    }

    public static String generateCvc() {
        Faker faker = new Faker();
        return faker.numerify("###");
    }

    public static CardInfo generateCard(String status, String locale, boolean wrongMonth) {
        return new CardInfo(
            status,
            generateCardNumber(status),
            generateCardHolder(locale),
            generateMonth(wrongMonth),
            generateYear(),
            generateCvc()
        );
    }

    @Data
    @AllArgsConstructor
    public static class CardInfo {
        String status;
        String cardNumber;
        String cardHolder;
        String month;
        String year;
        String cvc;
    }
}
