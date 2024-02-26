package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


    public static String generateCardHolder(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }


    public static String generateMonth(String locale) {
        Faker faker = new Faker();
        return faker.number().numberBetween(1, 12);
    }

    public static String generateYear(String locale) {
        Faker faker = new Faker();
        return faker.number().numberBetween(24, 30);
    }

    public static String generateCvc() {
        Faker faker = new Faker();
        return faker.numerify("###");
    }

}
    public static CardInfo generateUser(String locale) {
        return new CardInfo(generateCardHolder(), generateMonth(), generateYear(), generateCvc());
    }

@Value
public static class CardInfo {
    String cardNumber;
    String month;
    String cardHolder;
    String year;
    String cvc;
}