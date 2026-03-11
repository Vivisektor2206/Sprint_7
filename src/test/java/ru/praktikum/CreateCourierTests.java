package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import io.restassured.response.Response;
import ru.praktikum.model.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class CreateCourierTests extends BaseTest {


    @Step("Метод для получения ID курьера через логин/пароль")
    private Integer getCourierIdByLoginPassword(Courier courier) {
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

    @Test
    @DisplayName("CanCreate new courier and check response")
    @Description("Можно создать нового курьера и проверить ответ сервера")
    public void canCreateNewCourierAndCheckResponse() {
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
        testCourierId = getCourierIdByLoginPassword(courier);

        if (testCourierId == null) {
            throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
        }

        System.out.println("Создан курьер с ID: " + testCourierId);
    }

    @Test
    @DisplayName("Can't create the same courier and check response")
    @Description("Нельзя создать двух одинаковых курьеров, с  проверкой ответа сервера")
    public void cantCreateTheSameCourierAndCheckResponce() {
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
    }

    @Test
    @DisplayName("Can't create courier without login field")
    @Description("Нельзя создать курьера без поля логина в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutLoginField () {
        Courier courierWithoutLogin = new Courier(null, "test_pass", "Test Name");
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courierWithoutLogin)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
    }

    @Test
    @DisplayName("Can't create courier without password field")
    @Description("Нельзя создать курьера без поля пароля в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutPasswordField () {
        Courier courier = CourierAuthData.getUniqueCourier();
        Courier courierWithoutPassword = new Courier(courier.getLogin(), null, "Test Name");
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courierWithoutPassword)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
    }

    //Бонусный тест, раз уж написал, не удалять же...
    @Test
    @DisplayName("Can create courier without name field")
    @Description("Можно создать курьера без поля имя в теле запроса, с  проверкой ответа сервера")
    public void canCreateCourierWithoutNameField () {
        Courier courier = CourierAuthData.getUniqueCourier();
        Courier courierWithoutPassword = new Courier(courier.getLogin(),courier.getPassword() , null);
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courierWithoutPassword)
                .when()
                .post("/api/v1/courier")
                .then()
                .log().ifError()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().response();
    }
}
