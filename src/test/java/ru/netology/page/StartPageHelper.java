package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class StartPageHelper {

    private static final SelenideElement buttonBuyWithDebitCard = $(byText("Купить"));
    private static final SelenideElement buttonBuyWithCreditCard = $(byText("Купить в кредит"));
    private static final SelenideElement cardNumber = $(byXpath("//span[contains(text(),'Номер карты')]/following-sibling::span/input"));
    private static final SelenideElement cardHolder = $(byXpath("//span[contains(text(),'Владелец')]/following-sibling::span/input"));
    private static final SelenideElement month = $(byXpath("//span[contains(text(),'Месяц')]/following-sibling::span/input"));
    private static final SelenideElement year = $(byXpath("//span[contains(text(),'Год')]/following-sibling::span/input"));
    private static final SelenideElement cvc = $(byXpath("//span[contains(text(),'CVC/CVV')]/following-sibling::span/input"));
    private static final SelenideElement continueButton = $(byText("Продолжить"));
    private static final SelenideElement notificationApproved = $(".notification_status_ok");
    private static final SelenideElement notificationDeclined = $(".notification_status_error");

    private static final SelenideElement wrongFormat = $(byText("Неверный формат"));
    private static final SelenideElement requiredField = $(byText("Поле обязательно для заполнения"));
    private static final SelenideElement invalidDeadline = $(byText("Неверно указан срок действия карты"));
    private static final SelenideElement cardHasExpired = $(byText("Истёк срок действия карты"));


    public void continueButtonClick() {
        continueButton.click();
    }

    public void buttonBuyWithDebitCardClick() {
        buttonBuyWithDebitCard.click();
    }

    public void buttonBuyWithCreditCard() {
        buttonBuyWithCreditCard.click();
    }

    public void setCardNumber(String value) {
        cardNumber.setValue(value);
    }

    public void setCardHolder(String value) {
        cardHolder.setValue(value);
    }

    public void setMonth(String value) {
        month.setValue(value);
    }

    public void setYear(String value) {
        year.setValue(value);
    }

    public void setCvc(String value) {
        cvc.setValue(value);
    }

    public SelenideElement getNotificationApproved() {
        return notificationApproved;
    }
}
