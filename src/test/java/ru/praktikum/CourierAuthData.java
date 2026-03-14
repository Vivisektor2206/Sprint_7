
package ru.praktikum;

import io.restassured.response.Response;
import ru.praktikum.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierAuthData {
    // Статический метод для генерации уникального курьера
    public static Courier getUniqueCourier() {
        String uniqueLogin = "test_login_" + System.currentTimeMillis();
        return new Courier(uniqueLogin, "test_pass", "Test Name");
    }

    //Метод для получения ID курьера через логин/пароль
    public static Integer getCourierIdByLoginPassword(Courier courier) {
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();

        if (loginResponse.statusCode() == 200) {
            return loginResponse.jsonPath().getInt("id");
        } else {
            System.err.println("Не удалось авторизоваться для получения ID. Статус: " + loginResponse.statusCode());
            return null;
        }
    }
}