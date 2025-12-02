package tests;

import data.PetStatus;
import data.TestData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.PetModel;
import models.ApiResponseModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import utils.RandomUtils;

import java.util.List;

import static data.TestData.*;
import static helpers.ApiTestHelpers.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tags({@Tag("api"), @Tag("pet")})
@Owner("sergeyglukhov")
@Feature("Питомцы")
@DisplayName("Тесты сущности Pet (Питомцы)")
public class PetTests extends TestBase {

    @Story("Список животных")
    @EnumSource(PetStatus.class)
    @ParameterizedTest(name = "Статус: {0}")
    @DisplayName("Успешная выдача списка питомцев по статусу")
    void successfulGettingPetsByStatusParametrizedTest(PetStatus status) {
        List<PetModel> pets = step("Сделать запрос на выдачу списка животных по статусу", () ->
                executeGet("pet/findByStatus", "status", status.getValue(), 200)
                        .jsonPath().getList("$", PetModel.class));

        step("Проверить ответ, что база животных не пуста и статус соответствует", () -> {
            assertThat(pets)
                    .isNotEmpty()
                    .allMatch(pet -> pet.getStatus().getValue().equals(status.getValue()));
        });
    }

    @Test
    @Story("Список животных")
    @DisplayName("Успешная выдача животного по ID")
    void successfulGettingPetsByIdTest() {
        PetModel testPet = step("Создать новое животное для теста", TestData::getNewPetData);

        step("Сделать запрос на добавление животного", () ->
                executePost("pet", testPet, 200));

        PetModel petResponse = step("Сделать запрос на выдачу животного по ID", () ->
                executeGet("pet/{petId}", testPet.getId(), 200).as(PetModel.class));

        step("Проверить, что животное из ответа корректное", () ->
                assertThat(petResponse).isEqualTo(testPet));
    }

    @Test
    @Story("Список животных")
    @DisplayName("Неуспешная выдача животного со случайным ID")
    void unsuccessfulGettingPetsByIncorrectIdTest() {
        long testId = step("Сгенерировать случайны ID", RandomUtils::getRandomId);

        ApiResponseModel response = step("Сделать запрос на выдачу животного по ID", () ->
                executeGet("pet/{petId}", testId, 404).as(ApiResponseModel.class));

        step("Проверить ответ", () -> {
            assertThat(response.getCode()).isEqualTo(1);
            assertThat(response.getType()).isEqualTo("error");
            assertThat(response.getMessage()).isEqualTo("Pet not found");
        });
    }

    @Test
    @Story("Список животных")
    @DisplayName("Запрос с невалидным статусом возвращает пустой список")
    void gettingEmptyListByInvalidStatusTest() {
        // Note: Ошибка в API: должен возвращать 400, но возвращает 200 с пустым списком.
        // TODO: Удалить этот комментарий, когда ошибка в API будет исправлена

        String status = step("Сгенерировать случайный статус", RandomUtils::getRandomFakePetStatus);

        List<PetModel> pets = step("Сделать запрос на выдачу списка животных c сгенерированным статусом", () ->
                executeGet("pet/findByStatus", "status", status, 200)
                        .jsonPath().getList("$", PetModel.class));

        step("Проверить, что ответ пустой", () -> {
            assertThat(pets).isEmpty();
        });
    }

    @Test
    @Story("Добавление")
    @DisplayName("Успешное добавление нового животного")
    void successfulAddNewPetTest() {
        // Note: Ошибка в API: должен возвращать 201, но возвращает 200 с пустым списком.
        // TODO: Удалить этот комментарий, когда ошибка в API будет исправлена

        PetModel testPet = step("Подготовить случайные тестовые данные для добавления нового животного",
                TestData::getNewPetData);

        PetModel createdPet = step("Отправить запрос на добавление нового животного", () ->
                executePost("pet", testPet, 200).as(PetModel.class));

        step("Проверить, что питомец создан с правильными данными", () ->
                assertThat(createdPet).usingRecursiveComparison().isEqualTo(testPet));
    }

    @Test
    @Story("Изменение")
    @DisplayName("Успешное изменение животного")
    void successfulUpdatePetTets() {
        PetModel firstTestPet = getNewPetData();
        PetModel secondTestPet = getNewPetData();
        secondTestPet.setId(firstTestPet.getId());

        step("Отправить запрос на добавление первого животного", () ->
                executePost("pet", firstTestPet, 200)); // TODO: должен быть статус 201

        PetModel updatePet = step("Отправить запрос на изменение животных", () ->
                executePut("pet", secondTestPet, 200).as(PetModel.class));

        step("Проверить что животное изменилось", () -> {
            assertThat(updatePet).isEqualTo(secondTestPet);
            assertThat(updatePet).isNotEqualTo(firstTestPet);
        });
    }

    @Test
    @Story("Удаление")
    @DisplayName("Успешное удаление животного по ID")
    void successfulDeletePetTest() {
        PetModel testPet = step("Создать новое животное", TestData::getNewPetData);

        step("Отправить запрос на добавление животного", () ->
                executePost("pet", testPet, 200)); // TODO: должен быть статус 201

        ApiResponseModel response = step("Отправить запрос на удаление животного", () ->
                executeDelete("pet/{petId}", testPet.getId(), 200).as(ApiResponseModel.class));

        step("Проверить ответ после удаления: код 200, тип unknown, в поле message id животного", () -> {
            assertThat(response.getCode()).isEqualTo(200);
            assertThat(response.getType()).isEqualTo("unknown");
            assertThat(response.getMessage()).isEqualTo(String.valueOf(testPet.getId()));
        });
    }

    @Test
    @Story("Удаление")
    @DisplayName("Неуспешное удаление животного с некорректным ID в формате текста")
    void unsuccessfulDeletePetWithIncorrectIdTest() {
        String incorrectId = step("Создать случайны ID", RandomUtils::getRandomPetName);

        Response response = step("Отправить запрос на удаление животного", () ->
                executeDelete("pet/{petId}", incorrectId, 404));

        step("Проверить, что в ответе после удаления код ответа 404", () ->
                assertThat(response.statusCode()).isEqualTo(404));
    }
}
