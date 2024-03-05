package ru.netology.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import java.time.Duration;
import ru.netology.helpers.*;

public class PurchaseByCardTest {
    private static final StartPageHelper startPage = StartPageHelper.getInstance();
    private static final int maxTimeout = 30;
    private static final SqlHelper sql = SqlHelper.getInstance();

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
        startPage.fillCardInfo(cardInfo);
        startPage.continueButtonClick();
        startPage.getNotificationApproved().shouldBe(visible, Duration.ofSeconds(maxTimeout));
        assertEquals(DataHelper.getStatusApproved(), sql.getLastPaymentStatus());
    }

    @Test
    @DisplayName("2.1 Купить. Отказ в оплате при вводе номера отклонённой карты")
    void shouldBeDeclinedByDeclinedCard() {
        startPage.buttonBuyWithDebitCardClick();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusDeclined(), "ru");
        startPage.fillCardInfo(cardInfo);
        startPage.continueButtonClick();
        startPage.getNotificationDeclined().shouldBe(visible, Duration.ofSeconds(maxTimeout));
        assertEquals(DataHelper.getStatusDeclined(), sql.getLastPaymentStatus());
    }

    @Test
    @DisplayName("2.2 Купить. Отказ в оплате при вводе номера невалидной карты. Не добавляется в БД")
    void shouldBeRejectedByInvalidCard() {
        startPage.buttonBuyWithDebitCardClick();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusInvalid(), "ru");
        startPage.fillCardInfo(cardInfo);
        startPage.continueButtonClick();
        startPage.getNotificationInvalid().shouldBe(visible, Duration.ofSeconds(maxTimeout));
    }
}
