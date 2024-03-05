package ru.netology.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import java.time.Duration;
import ru.netology.page.StartPageHelper;
import ru.netology.data.DataHelper;

public class PurchaseByCardTest {
    private static final StartPageHelper startPage = new StartPageHelper();
    private static final int maxTimeout = 30;

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        sleep(1000);
    }

    @Test
    @DisplayName("1. Купить. Ввод валидных данных в поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV»")
    void shouldBePaidByApprovedCard() {
        startPage.buttonBuyWithDebitCardClick();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "ru");
        startPage.setCardNumber(cardInfo.getCardNumber());
        startPage.setCardHolder(cardInfo.getCardHolder());
        startPage.setMonth(cardInfo.getMonth());
        startPage.setYear(cardInfo.getYear());
        startPage.setCvc(cardInfo.getCvc());
        startPage.continueButtonClick();
        startPage.getNotificationApproved().shouldBe(visible, Duration.ofSeconds(maxTimeout));
    }

}
