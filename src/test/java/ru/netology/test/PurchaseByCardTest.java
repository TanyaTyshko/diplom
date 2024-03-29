package ru.netology.test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import java.time.Duration;
import ru.netology.helpers.*;

import java.util.Calendar;

public class PurchaseByCardTest {
    private static final StartPageHelper startPage = StartPageHelper.getInstance();
    private static final int maxTimeout = 20;
    private static final SqlHelper sql = SqlHelper.getInstance();

    private final String errMsgWrongFormat = "Неверный формат";
    private final String errMsgRequiredField = "Поле обязательно для заполнения";
    private final String errMsgInvalidDeadline = "Неверно указан срок действия карты";
    private final String errMsgCardHasExpired = "Истёк срок действия карты";
    private final String errMsgWrongCardHolder = "Должно состоять из букв на латинице";
    private final String errMsgWrongCardHolderCyrillic = "Имя и фамилия латиницей";

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @Test
    @DisplayName("01: Купить. Ввод валидных данных в поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV»")
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
    @DisplayName("02: Купить. Отказ в оплате при вводе номера отклонённой карты")
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
    @DisplayName("03: Купить. Ввод невалидных данных в поле «Месяц»")
    void shouldBeErrorWithWrongMonth() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", true);
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();
        startPage.fieldShouldHasError(startPage.getMonthInput(), errMsgInvalidDeadline);
        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("04: Купить. Оставить поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV» незаполненными")
    void shouldBeErrorWithEmptyForm() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getMonthInput(), errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgWrongFormat);
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgRequiredField);
        startPage.fieldShouldHasError(startPage.getCvcInput(), errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("05: Купить. Оставить поле «Номер карты» незаполненным")
    void shouldBeErrorWithEmptyCard() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardNumber("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("06: Купить. Оставить поле «Месяц» незаполненным")
    void shouldBeErrorWithEmptyMonth() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setMonth("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldHasError(startPage.getMonthInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("07: Купить. Оставить поле «Владелец» незаполненным")
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
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgRequiredField);
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("08: Купить. Оставить поле «Год» незаполненным")
    void shouldBeErrorWithEmptyYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear("");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("09: Купить. Оставить поле «CVC/CVV» незаполненным")
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
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgRequiredField);
        startPage.fieldShouldHasError(startPage.getCvcInput(), errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("10: Купить. Ввод невалидных данных в поле «Год», карта просрочена на год")
    void shouldBeErrorWithPrevYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear(DataHelper.getYearWithOffset(-1));
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgCardHasExpired);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("11: Купить. Ввод невалидных данных в поле «Год», срок действия карты больше 5 лет")
    void shouldBeErrorWithCardLongerThenFiveYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear(DataHelper.getYearWithOffset(6));
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgInvalidDeadline);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("12: Купить. Ввод некорректных данных в поле «Год», ввод одной цифры вместо двух")
    void shouldBeErrorWithWrongYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear("2");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("13: Купить. Ввод некорректных данных в поле «CVC/CVV», ввод одной цифры вместо трех")
    void shouldBeErrorWithWrongCvc() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCvc("1");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldHasError(startPage.getCvcInput(), errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("14: Купить. Ввод невалидных данных в поле «Месяц», ввод одной цифры вместо двух")
    void shouldBeErrorWithWrongMonthLength() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setMonth("1");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldHasError(startPage.getMonthInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("15: Купить. Ввод невалидных данных в поле «Номер карты», ввод 15 цифр вместо 16")
    void shouldBeErrorWithWrongCardNumber() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardNumber(cardInfo.getCardNumber().substring(0, 18));
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("16: Купить. Ввод невалидных данных в поле «Год», ввод двух нулей (00)")
    void shouldBeErrorWithZerosInsteadYear() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setYear("00");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldHasError(startPage.getYearInput(), errMsgCardHasExpired);
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("17: Купить. Ввод невалидных данных в поле «Месяц», ввод двух нулей (00)")
    void shouldBeErrorWithZerosInsteadMonth() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setMonth("00");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldHasError(startPage.getMonthInput(), errMsgInvalidDeadline);
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("18: Купить. Ввод трёх нулей (000) в поле «CVC/CVV»")
    void shouldBeErrorWithZerosInsteadCvc() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCvc("000");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldHasError(startPage.getCvcInput(), errMsgWrongFormat);

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("19: Купить. Ввод 16 нулей (0000 0000 0000 0000) в поле «Номер карты»")
    void shouldBeErrorWithZerosInsteadCardNumber() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardNumber("0000 0000 0000 0000");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldHasError(startPage.getCardNumberInput(), errMsgWrongFormat);
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldBeValid(startPage.getCardHolderInput());
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("20: Купить. Ввод невалидных данных (цифры) в поле «Владелец»")
    void shouldBeErrorWithWrongCardHolder() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardHolder("123");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgWrongCardHolder);
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("21: Купить. Ввод невалидных данных (спецсимволы) в поле «Владелец»")
    void shouldBeErrorWithWrongCardHolderSpecialSymbols() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "en", false);
        cardInfo.setCardHolder("!!!");
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgWrongCardHolder);
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

    @Test
    @DisplayName("22: Купить. Ввод невалидных данных (русские буквы) в поле «Владелец»")
    void shouldBeErrorWithWrongCardHolderCyrillic() {
        int countRecordsBefore = sql.getPaymentsCount();
        startPage.clickButtonBuyWithDebitCard();
        DataHelper.CardInfo cardInfo = DataHelper.generateCard(DataHelper.getStatusApproved(), "ru", false);
        startPage.fillCardInfo(cardInfo);
        startPage.clickContinueButton();

        startPage.fieldShouldBeValid(startPage.getCardNumberInput());
        startPage.fieldShouldBeValid(startPage.getMonthInput());
        startPage.fieldShouldBeValid(startPage.getYearInput());
        startPage.fieldShouldHasError(startPage.getCardHolderInput(), errMsgWrongCardHolderCyrillic);
        startPage.fieldShouldBeValid(startPage.getCvcInput());

        int countRecordsAfter = sql.getPaymentsCount();
        assertEquals(countRecordsBefore, countRecordsAfter);
    }

}
