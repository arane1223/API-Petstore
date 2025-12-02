package helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;


import static io.restassured.RestAssured.given;
import static specs.BaseSpecs.*;

public class ApiTestHelpers {

    @Step("Сделать GET запрос")
    public static Response executeGet(String path, int statusCode) {
        return given(baseReqSpec)
                .get(path)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать GET запрос")
    public static Response executeGet(String path, String queryParamName, String queryParam, int statusCode) {
        return given(baseReqSpec)
                .queryParam(queryParamName, queryParam)
                .get(path)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать GET запрос для логина")
    public static Response executeGetForLogin(String path, String username, String password, int statusCode) {
        return given(baseReqSpec)
                .queryParam("username", username)
                .queryParam("password", password)
                .get(path)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать GET запрос")
    public static Response executeGet(String path, Object pathParam, int statusCode) {
        return given(baseReqSpec)
                .get(path, pathParam)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать POST запрос")
    public static Response executePost(String path, Object body, int statusCode) {
        return given(baseReqSpec)
                .body(body)
                .when()
                .post(path)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать PUT запрос")
    public static Response executePut(String path, Object body, int statusCode) {
        return given(baseReqSpec)
                .body(body)
                .when()
                .put(path)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать PUT запрос")
    public static Response executePut(String path, String pathParam, Object body, int statusCode) {
        return given(baseReqSpec)
                .body(body)
                .when()
                .put(path, pathParam)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }

    @Step("Сделать DELETE запрос")
    public static Response executeDelete(String path, Object pathParam, int statusCode) {
        return given(baseReqSpec)
                .when()
                .delete(path, pathParam)
                .then()
                .spec(baseRespSpec(statusCode))
                .extract().response();
    }
}
