import Model.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class OrderClient {

    protected static final String CANCEL_ORDER_ENDPOINT = "/api/v1/orders/cancel";
    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    private static final String GET_ORDERS_ENDPOINT = "/api/v1/orders";

    @Step("Создание заказа с указанием цвета")
    public Response createOrderWithColor(List<String> color) {
        Order orderBody = OrderUtils.createOrderWithColor(color);

        return given()
                .header("Content-Type", "application/json")
                .body(orderBody)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then()
                .extract().response();
    }

    @Step("Отмена заказа по трек‑номеру")
    public int cancelOrderByTrack(String trackNumber) {
        String cancelBody = String.format("{\"track\":%s}", trackNumber);

        Response cancelResponse = given()
                .header("Content-Type", "application/json")
                .body(cancelBody)
                .when()
                .put(CANCEL_ORDER_ENDPOINT)
                .then()
                .extract().response();

        System.out.println("Тело запроса отмены: " + cancelBody);
        return cancelResponse.statusCode();
    }

    @Step("Создание заказа с передачей тела запроса")
    public Response createOrder(Map<String, Object> orderBody) {
        return given()
                .header("Content-Type", "application/json")
                .body(orderBody)
                .when()
                .post(CREATE_ORDER_ENDPOINT)
                .then()
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


