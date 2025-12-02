package data;

import models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static utils.RandomUtils.*;

public class TestData {
    private static List<String> photoUrlsData = List.of(getRandomUrl());
    private static List<Tag> tagsData = List.of(new Tag(
            getRandomId(), getRandomStringAsFunnyName()));

    public static Category getNewCategoryData() {
        return new Category(
                getRandomId(), getRandomStringAsFunnyName()
        );
    }

    public static PetModel getNewPetData() {
        return new PetModel(
                getRandomId(),
                getNewCategoryData(),
                getRandomPetName(),
                photoUrlsData,
                tagsData,
                getRandomPetStatus()
        );
    }

    public static OrderModel getNewOrderData() {
        return new OrderModel(
                getRandomId(),
                getRandomId(),
                getRandomInt(),
                LocalDateTime.now()
                        .plusDays(7)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+0000")),
                getRandomOrderStatus(),
                getRandomBoolean());
    }

    public static OrderModel getNewOrderWithIncorrectDateData() {
        return new OrderModel(
                getRandomId(),
                getRandomId(),
                getRandomInt(),
                "invalid-date-format",
                getRandomOrderStatus(),
                getRandomBoolean());
    }

    public static UserModel getNewUserData() {
        return new UserModel(
                getRandomId(),
                getRandomUserName(),
                getRandomFirstName(),
                getRandomLastName(),
                getRandomEmail(),
                getRandomPassword(),
                getRandomPhone(),
                getRandomInt()
        );
    }
}
