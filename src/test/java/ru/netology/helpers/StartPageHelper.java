package ru.netology.helpers;

import com.codeborne.selenide.SelenideElement;
import ru.netology.helpers.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

import java.time.Duration;

public class StartPageHelper {
    private static StartPageHelper instance;

    private final SelenideElement buttonBuyWithDebitCard = $(byText("Купить"));
    private final SelenideElement buttonBuyWithCreditCard = $(byText("Купить в кредит"));
    private final SelenideElement cardNumber = $(byXpath("//span[contains(text(),'Номер карты')]/following-sibling::span/input"));
    private final SelenideElement cardHolder = $(byXpath("//span[contains(text(),'Владелец')]/following-sibling::span/input"));
    private final SelenideElement month = $(byXpath("//span[contains(text(),'Месяц')]/following-sibling::span/input"));
    private final SelenideElement year = $(byXpath("//span[contains(text(),'Год')]/following-sibling::span/input"));
    private final SelenideElement cvc = $(byXpath("//span[contains(text(),'CVC/CVV')]/following-sibling::span/input"));
    private final SelenideElement notificationApproved = $(".notification_status_ok");
    private final SelenideElement notificationDeclined = $(".notification_status_error");
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement continueButtonWrapper = continueButton.parent().parent();

    // Приватный конструктор, чтобы запретить создание экземпляров класса вне его самого
    private StartPageHelper() {}

    // Метод для получения единственного экземпляра класса. Синглтон
    public static StartPageHelper getInstance() {
        if (instance == null) {
            instance = new StartPageHelper();
        }
        return instance;
    }

    public void clickContinueButton() { continueButton.click(); }
    public void clickButtonBuyWithDebitCard() { buttonBuyWithDebitCard.click(); }
    public void clickButtonBuyWithCreditCard() { buttonBuyWithCreditCard.click(); }

    public SelenideElement getNotificationApproved() { return notificationApproved; }
    public SelenideElement getNotificationDeclined() { return notificationDeclined; }
    public SelenideElement getCardNumberInput() { return cardNumber; }
    public SelenideElement getCardHolderInput() { return cardHolder; }
    public SelenideElement getMonthInput() { return month; }
    public SelenideElement getYearInput() { return year; }
    public SelenideElement getCvcInput() { return cvc; }

    public void waitForContinueButtonEnabled(int timeout) {
        continueButtonWrapper.shouldNotHave(cssClass("button_disabled"), Duration.ofSeconds(timeout));
    }

    public void fieldShouldHasError(SelenideElement field, String errorText) {
        field.parent().parent().parent().shouldHave(cssClass("input_invalid"));
        field.parent().parent().shouldHave(text(errorText));
    }

    public void fieldShouldBeValid(SelenideElement field) {
        field.parent().parent().parent().shouldNotHave(cssClass("input_invalid"));
        field.parent().parent().find("span.input__sub").shouldNot(exist);
    }

    public void fillCardInfo(DataHelper.CardInfo cardInfo) {
        cardNumber.setValue(cardInfo.getCardNumber());
        cardHolder.setValue(cardInfo.getCardHolder());
        month.setValue(cardInfo.getMonth());
        year.setValue(cardInfo.getYear());
        cvc.setValue(cardInfo.getCvc());
    }

}
