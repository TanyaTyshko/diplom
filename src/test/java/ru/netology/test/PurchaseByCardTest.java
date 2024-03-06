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
    @DisplayName("1: Купить. Ввод валидных данных в поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV»")
    void shouldBePaidByApprovedCard() {
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();
        startPage.waitForContinueButtonEnabled(maxTimeout);
        startPage.getNotificationApproved().shouldBe(visible);
        assertEquals(DataHelper.getStatusApproved(), sql.getLastPaymentStatus());
    }

    @Test
    @DisplayName("2: Купить. Отказ в оплате при вводе номера отклонённой карты")
    void shouldBeDeclinedByDeclinedCard() {
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusDeclined(), "en", false);
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();
        startPage.waitForContinueButtonEnabled(maxTimeout);
        startPage.getNotificationDeclined().shouldBe(visible);
        assertEquals(DataHelper.getStatusDeclined(), sql.getLastPaymentStatus());
    }

    @Test
    @DisplayName("3: Купить. Ввод невалидных данных в поле «Месяц»")
    void shouldBeErrorWithWrongMonth() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", true);
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();
        startPage.fieldShouldHasError(startPage.getMonthInput(), startPage.errMsgInvalidDeadline);
        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("4: Купить. Оставить поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV» незаполненными")
    void shouldBeErrorWithEmptyForm() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getMonthInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getYearInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), startPage.errMsgRequiredField);
        startPage.fieldShouldHasError(startPage.getCvcInput(), startPage.errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("5: Купить. Оставить поле «Номер карты» незаполненным")
    void shouldBeErrorWithEmptyCard() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardNumber("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("6: Купить. Оставить поле «Месяц» незаполненным")
    void shouldBeErrorWithEmptyMonth() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setMonth("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldHasError(startPage.getMonthInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("7: Купить. Оставить поле «Владелец» незаполненным")
    void shouldBeErrorWithEmptyCardHolder() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardHolder("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), startPage.errMsgRequiredField);
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("8: Купить. Оставить поле «Год» незаполненным")
    void shouldBeErrorWithEmptyYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), startPage.errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("9: Купить. Оставить поле «CVC/CVV» незаполненным")
    void shouldBeErrorWithEmptyCvc() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCvc("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), startPage.errMsgRequiredField);
        startPage.fieldShouldHasError(startPage.getCvcInput(), startPage.errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

}
