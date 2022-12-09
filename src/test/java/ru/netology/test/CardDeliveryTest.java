package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeAll
    static void receiveCities() {
        DataGenerator.getCityList();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendForm() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldSendFormWithWrongDateFormat() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldAcceptYoInName() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val("Семёнов Пётр");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldAcceptYoInCity() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val("Орёл");
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldAcceptDash() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val("Петрова-Водкина Анна-Мария");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldSendWithoutPlusInPhone() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val("79991112233");
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldResendFormWithOtherDate() {
        String city = DataGenerator.generateCity();
        String name = DataGenerator.generateName();
        String phone = DataGenerator.generatePhone();
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        String secondPlanningDate = DataGenerator.generateDate(5, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(name);
        $("[data-test-id='phone'] input").val(phone);
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
        open("http://localhost:9999");
        $("[data-test-id='city'] input").val(city);
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(5, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(name);
        $("[data-test-id='phone'] input").val(phone);
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'] button").click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + secondPlanningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldNotSendEmptyByDefault() {
        $(withText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendWithoutDate() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    void shouldNotSendWithoutName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendWithoutPhone() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendWithoutAgreement() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $(withText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid").should(appear);
    }

    @Test
    void shouldNotSendWithoutSurname() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val("Иван");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendWithSpaceForName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(" ");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendWithDashForName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val("-");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldNotSendCityOutOfList() {
        $("[data-test-id='city'] input").val("Королев");
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(text(
                "Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotAcceptDateInPast() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(-7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(text(
                "Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotSendLatinName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val("Ivanov Ivan");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSendSpecialSymbolsForName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val("!#&?");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSendNumbersInName() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val("Иван 1");
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(text(
                "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotSendLessDigitsInPhone() {
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(DataGenerator.generateDate(7, "dd.MM.yyyy"));
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val("+799911122");
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldHave(text(
                "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldUseCityList() {
        String planningDate = DataGenerator.generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val("мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(planningDate);
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldUseWebCalendarWithinCurrentMonth() {
        int defaultCalendarShift = 3;
        int planningDateShift = 7;
        String planningDate = DataGenerator.generateDate(planningDateShift, "dd.MM.yyyy");
        $("[data-test-id='city'] input").val(DataGenerator.generateCity());
        $("[data-test-id='date'] button").click();
        if (!DataGenerator.generateDate(defaultCalendarShift, "MM")
                .equals(DataGenerator.generateDate(planningDateShift, "MM"))) {
            $("[data-step='1'].calendar__arrow_direction_right").click();
        }
        $$("[data-day]").findBy(exactText(DataGenerator.generateDate(planningDateShift, "d"))).click();
        $("[data-test-id='name'] input").val(DataGenerator.generateName());
        $("[data-test-id='phone'] input").val(DataGenerator.generatePhone());
        $("[data-test-id='agreement'] span").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldHave(text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }
}