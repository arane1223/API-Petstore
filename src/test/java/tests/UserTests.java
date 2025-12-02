package tests;

import data.TestData;
import io.qameta.allure.*;
import models.ApiResponseModel;
import models.UserModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static helpers.ApiTestHelpers.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tags({@Tag("api"), @Tag("user")})
@Owner("sergeyglukhov")
@Feature("Пользователь")
@DisplayName("Тесты сущности User (Пользователь)")
public class UserTests extends TestBase {

    @Test
    @Story("Создание пользователя")
    @DisplayName("Успешное создание пользователя")
    void successfulCreateUserTest() {
        UserModel testUser = step("Создать данные для пользователя", TestData::getNewUserData);

        ApiResponseModel response = step("Отправить запрос на создание пользователя", () ->
                executePost("user", testUser, 200).as(ApiResponseModel.class)); // TODO: должен быть 201

        step("Проверить ответ", () -> {
            assertThat(response.getCode()).isEqualTo(200);
            assertThat(response.getType()).isEqualTo("unknown");
            assertThat(response.getMessage()).isEqualTo(String.valueOf(testUser.getId()));
        });
    }

    static Stream<Arguments> pathForResponse() {
        return Stream.of(
                Arguments.of("user/createWithList"),
                Arguments.of("user/createWithArray")
        );
    }

    @Story("Создание пользователя")
    @MethodSource("pathForResponse")
    @ParameterizedTest(name = "path = {0}")
    @DisplayName("Успешное создание листа пользователей")
    void successfulCreateUserWithListParametrizedTest(String path) {
        UserModel firstTestUser = step("Создать данные для первого пользователя", TestData::getNewUserData);
        UserModel secondTestUser = step("Создать данные для второго пользователя", TestData::getNewUserData);
        List<UserModel> testUsers = step("Объединить данные в List", () ->
                List.of(firstTestUser, secondTestUser));

        ApiResponseModel response = step("Отправить запрос на создание пользователей", () ->
                executePost(path, testUsers, 200).as(ApiResponseModel.class)); // TODO: должен быть 201

        step("Проверить ответ", () -> {
            assertThat(response.getCode()).isEqualTo(200);
            assertThat(response.getType()).isEqualTo("unknown");
            assertThat(response.getMessage()).isEqualTo("ok");
        });
    }

    @Test
    @Story("Получение пользователя")
    @DisplayName("Успешное получение пользователя по username")
    void successfulGettingUserWithUsernameTest() {
        UserModel testUser = step("Создать данные для пользователя", TestData::getNewUserData);

        step("Отправить запрос на добавление пользователя", () ->
                executePost("user", testUser, 200)); // TODO: должен быть 201

        UserModel userResponse = step("Отправить запрос на получение пользователя", () ->
                executeGet("user/{username}", testUser.getUsername(), 200).as(UserModel.class));

        step("Проверить, что пользователь из ответа соответствует созданному", () ->
                assertThat(userResponse).isEqualTo(testUser));
    }

    @Test
    @Story("Обновление пользователя")
    @DisplayName("Успешное обновление пользователя по username")
    void successfulUpdateUserWithUsernameTest() {
        UserModel testUser = step("Создать данные для пользователя", TestData::getNewUserData);
        UserModel updateData = step("Создать данные для обновления", TestData::getNewUserData);

        step("Отправить запрос на добавление пользователя", () ->
                executePost("user", testUser, 200)); // TODO: должен быть 201

        ApiResponseModel updateResponse = step("Отправить запрос на обновление", () ->
                executePut("user/{username}", testUser.getUsername(), updateData, 200)
                        .as(ApiResponseModel.class));

        step("Отправить запрос на добавление пользователя", () -> {
            assertThat(updateResponse.getCode()).isEqualTo(200);
            assertThat(updateResponse.getType()).isEqualTo("unknown");
            assertThat(updateResponse.getMessage()).isEqualTo(String.valueOf(updateData.getId()));
            assertThat(updateResponse.getMessage()).isNotEqualTo(String.valueOf(testUser.getId()));
        });
    }

    @Test
    @Story("Удаление пользователя")
    @DisplayName("Успешное удаление пользователя по username")
    void successfulDeletingUserWithUsernameTest() {
        UserModel testUser = step("Создать данные для пользователя", TestData::getNewUserData);

        step("Отправить запрос на добавление пользователя", () ->
                executePost("user", testUser, 200)); // TODO: должен быть 201

        ApiResponseModel response = step("Отправить запрос на удаление пользователя", () ->
                executeDelete("user/{username}", testUser.getUsername(),200)
                        .as(ApiResponseModel.class));

        step("Проверить ответ после удаления", () -> {
            assertThat(response.getCode()).isEqualTo(200);
            assertThat(response.getType()).isEqualTo("unknown");
            assertThat(response.getMessage()).isEqualTo(testUser.getUsername());
        });
    }
}
