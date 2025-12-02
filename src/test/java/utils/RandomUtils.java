package utils;

import com.github.javafaker.Faker;
import data.OrderStatus;
import data.PetStatus;

public class RandomUtils {
    public static Faker faker = new Faker();

    public static String getRandomFakePetStatus() {
        return faker.beer().name();
    }

    public static long getRandomId() {
        return faker.number().randomNumber();
    }

    public static String getRandomPetName() {
        return faker.animal().name();
    }

    public static String getRandomUrl() {
        return faker.internet().url();
    }

    public static String getRandomStringAsFunnyName() {
        return faker.funnyName().name();
    }

    public static PetStatus getRandomPetStatus() {
        return faker.options().option(
                PetStatus.AVAILABLE, PetStatus.PENDING, PetStatus.SOLD
        );
    }

    public static int getRandomInt() {
        return faker.number().randomDigit();
    }

    public static OrderStatus getRandomOrderStatus() {
        return faker.options().option(
                OrderStatus.APPROVED, OrderStatus.DELIVERED, OrderStatus.PLACED
        );
    }

    public static boolean getRandomBoolean() {
        return faker.bool().bool();
    }

    public static String getRandomUserName() {
        return faker.name().username();
    }

    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomLastName() {
        return faker.name().lastName();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return faker.internet().password();
    }

    public static String getRandomPhone() {
        return faker.phoneNumber().phoneNumber();
    }
}
