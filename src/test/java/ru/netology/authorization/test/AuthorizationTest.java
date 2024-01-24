package ru.netology.authorization.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.authorization.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthorizationTest {
    @Test
    @DisplayName("Registered active user should successfully login")
    void shouldLoginRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("h2")
                .shouldHave(Condition.exactText("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Registered active user should get error-notification when logging with wrong login")
    void shouldLoginRegisteredActiveUserWithWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Registered active user should get error-notification when logging with wrong password")
    void shouldLoginRegisteredActiveUserWithWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Unregistered blocked user should get error-notification when logging")
    void shouldLoginUnregisteredActiveUser() {
        var unregisteredUser = DataGenerator.Registration.getUser("active");
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(unregisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(unregisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("Registered blocked user should get error-notification when logging")
    void shouldLoginRegisteredBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        open("http://localhost:9999");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));
    }
}
