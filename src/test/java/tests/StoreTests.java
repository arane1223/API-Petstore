package tests;

import data.PetStatus;
import data.TestData;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.OrderModel;
import models.ApiResponseModel;
import org.junit.jupiter.api.*;

import java.util.Map;

import static helpers.ApiTestHelpers.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.RandomUtils.getRandomId;

@Tags({@Tag("api"), @Tag("store")})
@Owner("sergeyglukhov")
@Feature("Магазин")
@DisplayName("Тесты сущности Store (Магазин)")
public class StoreTests extends TestBase {

    @Test
    @Story("Получение инвентаря")
    @DisplayName("Успешная выдача статуса у инвентаря")
    void successfulGettingInventoryStatusTest() {
        Map<String, Integer> inventory = step("Сделать запрос на получение статусов инвентаря", () ->
                executeGet("store/inventory", 200).jsonPath().getMap("$"));

        step("Проверить в ответе: инвентарь не пуст, содержит основные PetStatuses, " +
                        "значения у каждого статуса ≥ 0", () -> {
                    assertThat(inventory).isNotEmpty();
                    assertThat(inventory.keySet()).contains(PetStatus.getAllValues());
                    inventory.values().forEach(count -> assertThat(count).isGreaterThan(0));
                });
    }

    @Test
    @Story("Создание")
    @DisplayName("Успешное создание нового заказа")
    void successfulAddingNewOrderTest() {
        // Note: Ошибка в API: должен возвращать 201, но возвращает 200 с пустым списком.
        // TODO: Удалить этот комментарий, когда ошибка в API будет исправлена

        OrderModel testOrder = step("Создать новый заказ", TestData::getNewOrderData);

        OrderModel orderResponse = step("Отправить запрос на добавление заказа", () ->
                executePost("store/order", testOrder, 200).as(OrderModel.class));

        step("Проверить, что заказ из ответа соответствует созданному", () ->
                assertThat(orderResponse).isEqualTo(testOrder));
    }

    @Test
    @Story("Создание")
    @DisplayName("Неуспешное создание нового заказа с некорректной датой")
    void unsuccessfulAddingNewOrderWithIncorrectDataTest() {
        OrderModel testOrder = step("Создание заказа с некорректной датой", TestData::getNewOrderWithIncorrectDateData);

        Response response = step("Отправить запрос на добавление заказа", () ->
                executePost("store/order", testOrder, 500)); // TODO: должен быть 404

        step("Проверить ответ", () -> {
            assertThat(response.jsonPath().getInt("code")).isEqualTo(500);
            assertThat(response.jsonPath().getString("type")).isEqualTo("unknown");
            assertThat(response.jsonPath().getString("message")).isEqualTo("something bad happened");
        });
    }

    @Test
    @Story("Получение заказа")
    @DisplayName("Успешное получение заказа по ID")
    void successfulGettingOrderByIdTest() {
        OrderModel testOrder = step("Создать новый заказ", TestData::getNewOrderData);

        step("Отправить запрос на добавление заказа", () ->
                executePost("store/order", testOrder, 200)); // TODO: должен быть 201

        OrderModel orderResponse = step("Отправить запрос на получение заказа по ID", () ->
                executeGet("store/order/{orderId}", testOrder.getId(), 200).as(OrderModel.class));

        step("Проверить, что заказ из ответа соответствует тестовому", () ->
                assertThat(orderResponse).isEqualTo(testOrder));
    }

    @Test
    @Story("Получение заказа")
    @DisplayName("Неуспешное получение заказа по отрицательному ID")
    void unsuccessfulGettingOrderByIdTest() {
        long incorrectTestId = step("Сгенерировать случайный отрицательный ID", () -> -getRandomId());

        ApiResponseModel response = step("Отправить запрос на получение заказа по ID", () ->
                executeGet("store/order/{orderId}", incorrectTestId, 404).as(ApiResponseModel.class));

        step("Проверить ответ", () -> {
            assertThat(response.getCode()).isEqualTo(1);
            assertThat(response.getType()).isEqualTo("error");
            assertThat(response.getMessage()).isEqualTo("Order not found");
        });
    }

    @Test
    @Story("Удаление заказа")
    @DisplayName("Успешное получение заказа по ID")
    void successfulDeletingOrderByIdTest() {
        OrderModel testOrder = step("Создать новый заказ", TestData::getNewOrderData);

        step("Отправить запрос на добавление заказа", () ->
                executePost("store/order", testOrder, 200)); // TODO: должен быть 201

        ApiResponseModel response = step("Отправить запрос на удаление заказа", () ->
                executeDelete("store/order/{orderId}", testOrder.getId(), 200).as(ApiResponseModel.class));

        step("Проверить ответ после удаления: код 200, тип unknown, в поле message id заказа", () -> {
            assertThat(response.getCode()).isEqualTo(200);
            assertThat(response.getType()).isEqualTo("unknown");
            assertThat(response.getMessage()).isEqualTo(String.valueOf(testOrder.getId()));
        });
    }
}
