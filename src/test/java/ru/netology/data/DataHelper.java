package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String cardHolder;
        String year;
        String cvc;
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
        return faker.name().lastName() + " " + faker.name().firstName();
    }


//    public static String generateMonth(String locale) {
//        var faker = new Faker(new Locale(locale));
//        return faker.month().month();
//    }

//    public static String generateYear(String locale) {
//        var faker = new Faker(new Locale(locale));
//        return faker.year().year();
//    }
//
//
//    public static String generateCvc(String locale) {
//        var faker = new Faker(new Locale(locale));
//        return faker.cvc().cvc();
//    }
//
//    public static String generateCvc() {
//        Faker faker = new Faker();
//        return faker.numerify("###");
//    }
//
//    public static String generateCvc() {
//        Random random = new Random();
//        return String.format("%03d", random.nextInt());
//
    }
}