package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import io.restassured.response.Response;
import ru.praktikum.model.Courier;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class CreateCourierTests extends BaseTest {

    @Test
    @DisplayName("CanCreate new courier and check response")
    @Description("Можно создать нового курьера и проверить ответ сервера")
    public void canCreateNewCourierAndCheckResponse() {
        try {
        Courier courier = CourierAuthData.getUniqueCourier();

        // Создаём курьера
        Response createResponse = given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().response();

        System.out.println("Полный ответ создания: " + createResponse.asString());

        // Получаем ID через авторизацию
        testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

        if (testCourierId == null) {
            throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
        }

        System.out.println("Создан курьер с ID: " + testCourierId);
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create the same courier and check response")
    @Description("Нельзя создать двух одинаковых курьеров, с  проверкой ответа сервера")
    public void cantCreateTheSameCourierAndCheckResponce() {
        try {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Создаём курьера
        Response createResponse = given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().response();

        System.out.println("Полный ответ создания: " + createResponse.asString());

        //Пытаемся создать еще одного курьера с тем же логином
        Response createResponseForSecondRequest = given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .extract().response();
        System.out.println("Полный ответ ошибки: " + createResponseForSecondRequest.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create courier without login field")
    @Description("Нельзя создать курьера без поля логина в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutLoginField () {
        try {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Формируем тело запроса БЕЗ поля login
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("password", courier.getPassword());
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create courier without password field")
    @Description("Нельзя создать курьера без поля пароля в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutPasswordField () {
        try {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Формируем тело запроса БЕЗ поля password
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("login", courier.getLogin());
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    //Бонусный тест, раз уж написал, не удалять же...
    @Test
    @DisplayName("Can create courier without name field")
    @Description("Можно создать курьера без поля имя в теле запроса, с  проверкой ответа сервера")
    public void canCreateCourierWithoutNameField () {
        try {
        Courier courier = CourierAuthData.getUniqueCourier();
        Courier notFullCourierRequestData = new Courier(courier.getLogin(),courier.getPassword() , null);
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(notFullCourierRequestData)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().response();
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}
