import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class CourierApiRequests {

    //Отправляем запрос на создание курьера
    public Response post(String endpoint, Object requestBody) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .log().ifError()
                .extract().response();
    }

    //Отправляем запрос на удаление курьера
    public Response delete(String endpoint, Object requestBody) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .delete(endpoint)
                .then()
                .log().ifError()
                .extract().response();
    }
}