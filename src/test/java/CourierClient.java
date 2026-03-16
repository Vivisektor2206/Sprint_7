import Model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierClient {

    private static final String CREATE_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";

    @Step("Отправка запроса на создание курьера")
    public Response create(Courier courier) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_ENDPOINT).then()
                .log().ifError()
                .body("ok", equalTo(true))
                .extract().response();
    }

    @Step("Отправка запроса на создание существующего курьера")
    public Response createSameCourier(Courier courier) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_ENDPOINT).then()
                .log().ifError()
                .extract().response();
    }

    @Step("Отправка запроса на авторизацию без поля логин")
    public Response createWithoutLoginField(Courier courier) {
        Map<String, Object> passwordBody = new HashMap<>();
        passwordBody.put("password", courier.getPassword());
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(passwordBody)
                .when()
                .post(CREATE_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", CoreMatchers.containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
    }

    @Step("Отправка запроса на авторизацию без поля пароль")
    public Response createWithoutPasswordField(Courier courier) {
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("login", courier.getLogin());
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post(CREATE_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", CoreMatchers.containsString("Недостаточно данных для создания учетной записи"))
                .extract().response();
    }

   // @Step("Отправка запроса на проверку авторизации курьера")
    public Response checkAuthorization(Courier courier) {
        Integer testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

        if (testCourierId == null) {
            throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
        }
        System.out.println("Создан курьер с ID: " + testCourierId);

        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(new Courier(courier.getLogin(), courier.getPassword(), null))
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("id", equalTo(testCourierId))
                .extract().response();
    }

    @Step("Отправка запроса на логин курьера без поля логин")
    public static Response authorizationWithoutLoginField(Courier courier) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(new Courier(null, courier.getPassword(), null))
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract().response();
    }

    @Step("Отправка запроса на логин курьера без поля пароль")
    public static Response authorizationWithoutPasswordField(Courier courier) {
        // Формируем тело запроса БЕЗ поля password
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("login", courier.getLogin());

        // Проверяем авторизацию и полный ответ
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(loginBody)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"))
                .extract().response();

    }

    @Step("Отправка запроса на авторизацию курьера с неверным логином")
    public Response authorizationWithWrongLogin(Courier courier) {

        Courier dataWithWrongLogin = new Courier(courier.getLogin() + "123", courier.getPassword(), null);

        // Проверяем авторизацию и полный ответ
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(dataWithWrongLogin)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"))
                .extract().response();

    }

    @Step("Отправка запроса на авторизацию курьера с неверным паролем")
    public Response authorizationWithWrongPassword(Courier courier) {

        Courier dataWithWrongPassword = new Courier(courier.getLogin(), courier.getPassword() + "123", null);

        // Проверяем авторизацию и полный ответ
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(dataWithWrongPassword)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"))
                .extract().response();

    }

}
