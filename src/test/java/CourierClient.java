import Model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

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
}
