package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.open;

public class DataGenerator {
    static ArrayList<String> cities = new ArrayList<>();
    private static Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    public static void getCityList() {
        open("https://ru.wikipedia.org/wiki/%D0%90%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B8%D0%B2%D0%BD%D1%8B%D0%B5_%D1%86%D0%B5%D0%BD%D1%82%D1%80%D1%8B_%D1%81%D1%83%D0%B1%D1%8A%D0%B5%D0%BA%D1%82%D0%BE%D0%B2_%D0%A0%D0%BE%D1%81%D1%81%D0%B8%D0%B9%D1%81%D0%BA%D0%BE%D0%B9_%D0%A4%D0%B5%D0%B4%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D0%B8");
        int lastCityIndex = $$x("//td[3]").exclude(empty).size() - 1;
        for (int i = 0; i <= lastCityIndex; i++) {
            cities.add($$x("//td[3]").exclude(empty).get(i).text().trim());
        }
    }

    public static String generateDate(int shift, String formatPattern) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public static String generateCity() {
        Random indexGenerator = new Random();
        int cityIndex = indexGenerator.nextInt(cities.size() - 1);
        return cities.get(cityIndex);
    }

    public static String generateName() {
        String name = faker.name().fullName();
        return name;
    }

    public static String generatePhone() {
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }
}