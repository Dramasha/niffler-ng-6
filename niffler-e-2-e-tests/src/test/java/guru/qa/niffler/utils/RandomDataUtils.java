package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    public static String getRandomUsername() {

        return faker.name().username();
    }

    public static String getRandomName() {

        return faker.name().firstName();
    }

    public static String getRandomSurname() {

        return faker.name().lastName();
    }

    public static String getRandomCategoryName() {
        return faker.internet().password(2,15);
    }

    public static String getRandomSentence(int wordsCount) {
        if (wordsCount <= 0) {
            throw new IllegalArgumentException("Количество слов должно быть положительным");
        }

        return faker.lorem().sentence(wordsCount);
    }

    public static String getRandomPassword(int minLength, int maxLength) {
        if (minLength <= 0 || maxLength <= 0 || minLength > maxLength) {
            throw new IllegalArgumentException("Длина пароля должна быть положительной и minLength не может быть больше maxLength");
        }

        return faker.internet().password(minLength, maxLength);
    }
}
