package ru.netology.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class PurchaseByCardTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");

    }

    @Test
    @DisplayName("1.Купить. Ввод валидных данных в поля «Номер карты», «Месяц», «Владелец», «Год», «CVC/CVV»")
    void shouldBePaidByApprovedCard() {

    }
}
