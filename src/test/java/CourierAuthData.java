import Model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierAuthData {

    // Статический метод для генерации уникального курьера
    @Step("Генерация данных для уникального курьера")
    public static Courier getUniqueCourier() {
        String uniqueLogin = "test_login_" + System.currentTimeMillis();
        String uniquePassword = "test_password_" + System.currentTimeMillis();
        String uniqueName = "test_name_" + System.currentTimeMillis();

        return new Courier(uniqueLogin, uniquePassword, uniqueName);
    }

    // Метод для получения ID курьера через логин/пароль
    @Step("Отправка запроса на авторизацию курьера")
    public static Integer getCourierIdByLoginPassword(Courier courier) {
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                // Оставляем только логирование ошибок — не прерываем выполнение при ошибке
                .log().ifError()
                .extract().response();

        // Проверка статуса и обработка ошибок
        if (loginResponse.statusCode() == SC_OK) {
            return loginResponse.jsonPath().getInt("id");
        } else {
            System.err.println("Не удалось авторизоваться для получения ID. Статус: "
                    + loginResponse.statusCode()
                    + ", тело ответа: " + loginResponse.asString());
            return null;
        }
    }
}

