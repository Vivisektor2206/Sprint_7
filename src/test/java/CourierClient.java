import Model.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import com.google.gson.Gson;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierClient {

    private static final String CREATE_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";

    @Step("Отправка запроса на создание курьера")
    public Response create(Courier courier) {
        Gson gson = new Gson();
        String json = gson.toJson(courier);

        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(CREATE_ENDPOINT)
                .then()
                .log().ifError()
                .extract().response();
    }


    @Step("Отправка запроса на авторизацию курьера")
    public Response authorization(Courier courier) {
        Gson gson = new Gson();
        String json = gson.toJson(courier);

        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .post(LOGIN_ENDPOINT)
                .then()
                .log().ifError()
                .extract().response();
    }


    @Step("Отправка запроса на удаление курьера")
    public static Response deleteCourier(String courierId) {
        String json = String.format("{\"id\":\"%s\"}", courierId);

        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(json)
                .when()
                .delete(CREATE_ENDPOINT + "/" + courierId)
                .then()
                .log().ifError()
                .statusCode(200)
                .body("ok", equalTo(true))
                .extract().response();
    }


}
