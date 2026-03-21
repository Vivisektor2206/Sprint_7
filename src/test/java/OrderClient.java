import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderClient {

    protected static final String CANCEL_ORDER_ENDPOINT = "/api/v1/orders/cancel";
    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    private static final String GET_ORDERS_ENDPOINT = "/api/v1/orders";


    public Response createOrderWithColor(List<String> color) {
        Map<String, Object> orderBody = OrderUtils.createOrderBodyWithColor(color);

        return given()
                .header("Content-Type", "application/json")
                .body(orderBody)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response();
    }

    public int cancelOrderByTrack(Integer trackNumber) {
        String cancelBody = String.format("{\"track\":%d}", trackNumber);

        Response cancelResponse = given()
                .header("Content-Type", "application/json")
                .body(cancelBody)
                .when()
                .put(CANCEL_ORDER_ENDPOINT)
                .then()
                .extract().response();

        return cancelResponse.statusCode();
    }

    public Response createOrder(Map<String, Object> orderBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(orderBody)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response();
    }


    @Step("Отправка запроса для получения списка заказов")
    public static Response getOrdersList(Map<String, Object> params) {
        return given()
                .params(params != null ? params : new java.util.HashMap<>())
                .when()
                .get(GET_ORDERS_ENDPOINT)
                .then()
                .extract().response();
    }
}


