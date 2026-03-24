import Model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierClient {
    private static final String CREATE_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";

    private final CourierApiRequests apiClient = new CourierApiRequests();

    @Step("Отправка запроса на создание курьера")
    public Response create(Courier courier) {
        Response response = apiClient.post(CREATE_ENDPOINT, courier);
        response.then().log().ifError();
        return response;
    }

    @Step("Отправка запроса на авторизацию курьера")
    public Response authorization(Courier courier) {
        Response response = apiClient.post(LOGIN_ENDPOINT, courier);
        response.then().log().ifError();
        return response;
    }

    @Step("Отправка запроса на удаление курьера")
    public static Response deleteCourier(String courierId) {
        // Создаём тело запроса для удаления
        String json = String.format("{\"id\":\"%s\"}", courierId);

        CourierApiRequests apiClient = new CourierApiRequests();
        Response response = apiClient.delete(CREATE_ENDPOINT + "/" + courierId, json);

        // Удаляем все проверки из метода
        response.then().log().ifError();

        return response;
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
