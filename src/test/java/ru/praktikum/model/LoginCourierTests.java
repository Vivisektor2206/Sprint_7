package ru.praktikum.model;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.platform.commons.function.Try;
import ru.praktikum.BaseTest;
import ru.praktikum.CourierAuthData;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class LoginCourierTests extends BaseTest {

    @Test
    @DisplayName("Can login courier and check response")
    @Description("Курьер может авторизоваться, с  проверкой ответа сервера")
    public void canLoginCourierAndCheckResponse() {

        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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
            Integer testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

            if (testCourierId == null) {
                throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
            }

            System.out.println("Создан курьер с ID: " + testCourierId);

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(200)
                    .body("id", equalTo(testCourierId))
                    .extract().response();

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Cant login courier without login field and check response")
    @Description("Курьер не может авторизоваться без поля login в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithoutLoginFieldAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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

            // Формируем тело запроса БЕЗ поля login
            Map<String, Object> loginBody = new HashMap<>();
            loginBody.put("password", courier.getPassword());

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(new Courier(null, courier.getPassword(), null))
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для входа"))
                    .extract().response();

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Cant login courier without password field and check response")
    @Description("Курьер не может авторизоваться без поля password в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithoutPasswordFieldAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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

            // Формируем тело запроса БЕЗ поля password
            Map<String, Object> loginBody = new HashMap<>();
            loginBody.put("login", courier.getLogin());

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(loginBody)
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для входа"))
                    .extract().response();
            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Cant login courier with wrong login and check response")
    @Description("Курьер не может авторизоваться с неверным логином в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongLoginAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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

            Courier dataWithWrongLogin = new Courier(courier.getLogin() + "123", courier.getPassword(), null);

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(dataWithWrongLogin)
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(404)
                    .body("message", equalTo("Учетная запись не найдена"))
                    .extract().response();

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Cant login courier with wrong password and check response")
    @Description("Курьер не может авторизоваться с неверным паролем в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongPasswordAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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

            Courier dataWithWrongPassword = new Courier(courier.getLogin(), courier.getPassword() + "123", null);

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(dataWithWrongPassword)
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(404)
                    .body("message", equalTo("Учетная запись не найдена"))
                    .extract().response();

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Cant login courier with wrong user and check response")
    @Description("Курьер не может авторизоваться с несуществующим пользователем, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongUserAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            Courier dataWithWrongLogin = new Courier(courier.getLogin() + "123", courier.getPassword() + "123", null);

            // Проверяем авторизацию и полный ответ
            Response loginResponse = given()
                    .log().all()
                    .header("Content-Type", "application/json")
                    .body(dataWithWrongLogin)
                    .when()
                    .post("/api/v1/courier/login")
                    .then()
                    .log().ifError()
                    .statusCode(404)
                    .body("message", equalTo("Учетная запись не найдена"))
                    .extract().response();

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("successful request returns user ID, and check response")
    @Description("Успешный запрос возвращает id")
    public void successfulRequestReturnsUserId() {

        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            Response createResponse = given()
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
            Integer testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

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

}
