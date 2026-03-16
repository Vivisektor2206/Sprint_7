import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderUtils {

    public static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";
    public static final String GET_ORDERS_ENDPOINT = "/api/v1/orders";


    //Вспомогательный метод для формирования тела заказа с указанным цветом или без цвета
    @Step("Отправка запроса для формирования тела заказа с указанным цветом или без цвета")
    public static Map<String, Object> createOrderBodyWithColor(List<String> colors) {
        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("firstName", "Naruto");
        orderBody.put("lastName", "Uchiha");
        orderBody.put("address", "Konoha, 142 apt.");
        orderBody.put("metroStation", 4);
        orderBody.put("phone", "+7 800 355 35 35");
        orderBody.put("rentTime", 5);
        orderBody.put("deliveryDate", "2020-06-06");
        orderBody.put("comment", "Saske, come back to Konoha");

        if (colors != null && !colors.isEmpty()) {
            orderBody.put("color", colors);
        }
        // Если colors == null или пустой список, поле color не добавляется

        return orderBody;
    }

    //Вспомогательный метод для создания заказа через API
    @Step("Отправка запроса для создания заказа через API")
    public static Response createOrder(Map<String, Object> orderBody) {
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
