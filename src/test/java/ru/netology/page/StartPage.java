package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartPage {

    private static final SelenideElement buttonBuyWithDebitCard = $(byText("Купить"));
    private static final SelenideElement buttonBuyWithCreditCard = $(byText("Купить в кредит"));
    private static final SelenideElement cardNumber = $(byText("Номер карты"));
    private static final SelenideElement month = $(byText("Месяц"));
    private static final SelenideElement cardHolder = $(byText("Владелец"));
    private static final SelenideElement year = $(byText("Год"));
    private static final SelenideElement cvc = $(byText("CVC/CVV"));
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

    public void buttonBuyWithDebitCard() {
        buttonBuyWithDebitCard.click();
    }

    public void buttonBuyWithCreditCard() {
        buttonBuyWithCreditCard.click();
    }

}
