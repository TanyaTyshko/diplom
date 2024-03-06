package ru.netology.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
// import static com.codeborne.selenide.Condition.cssClass;
import java.time.Duration;
import ru.netology.helpers.*;

public class PurchaseByCardTest {
    private static final StartPageHelper startPage = StartPageHelper.getInstance();
    private static final int maxTimeout = 20;
    private static final SqlHelper sql = SqlHelper.getInstance();

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("1. Купить. Ввод валидных данных в поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV»")
    void shouldBePaidByApprovedCard() {
        startPage.buttonBuyWithDebitCardClick();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en");
        startPage.fillCardInfo(cardInfo);
        startPage.continueButtonClick();
        startPage.waitForContinueButtonEnabled(maxTimeout);
        startPage.getNotificationApproved().shouldBe(visible);
        assertEquals(DataHelper.getStatusApproved(), sql.getLastPaymentStatus());
    }

    @Test
    @DisplayName("2.1 Купить. Отказ в оплате при вводе номера отклонённой карты")
    void shouldBeDeclinedByDeclinedCard() {
        startPage.buttonBuyWithDebitCardClick();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusDeclined(), "ru");
        startPage.fillCardInfo(cardInfo);
        startPage.continueButtonClick();
        startPage.waitForContinueButtonEnabled(maxTimeout);
        startPage.getNotificationDeclined().shouldBe(visible);
        assertEquals(DataHelper.getStatusDeclined(), sql.getLastPaymentStatus());
    }

}
