package ru.netology.helpers;

import com.github.javafaker.Faker;
import lombok.Value;

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

    public static String invalidCardNumber() {
        return "4444 4444 4444 4444";
    }

    public static String getStatusApproved() {
        return "APPROVED";
    }

    public static String getStatusDeclined() {
        return "DECLINED";
    }

    public static String getStatusInvalid() {
        return "INVALID";
    }

    public static String generateCardNumber(String status) {
        if (status == getStatusApproved()) {
            return approvedCardNumber();
        }
        if (status == getStatusDeclined()) {
            return declinedCardNumber();
        }
        if (status == getStatusInvalid()) {
            return declinedCardNumber();
        }
        return "Allowed Statuses: APPROVED|DECLINED|INVALID";
    }

    public static String generateCardHolder(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    public static String generateMonth() {
        Faker faker = new Faker();
        return String.format("%02d", faker.number().numberBetween(1, 12));
    }

    public static String generateYear() {
        Faker faker = new Faker();
        return String.valueOf(faker.number().numberBetween(25, 30));
    }

    public static String generateCvc() {
        Faker faker = new Faker();
        return faker.numerify("###");
    }

    public static CardInfo generateCard(String status, String locale) {
        return new CardInfo(
            status,
            generateCardNumber(status),
            generateCardHolder(locale),
            generateMonth(),
            generateYear(),
            generateCvc()
        );
    }

    @Value
    public static class CardInfo {
        String status;
        String cardNumber;
        String cardHolder;
        String month;
        String year;
        String cvc;
    }
}