import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTests extends OrdersBaseTest {

    @Test
    @DisplayName("Проверка, что в ответе возвращается список заказов")
    @Description("Отправляется запрос на получение списка заказов, проверяется наличие поля 'orders' в ответе")
    public void testOrdersListReturnsOrdersField() {
        Response response = getOrdersListAndVerifyStatus();
        verifyOrdersFieldExists(response);
        printOrdersInfo(response);
    }

    @Step("Отправка запроса на получение списка заказов")
    private Response getOrdersListAndVerifyStatus() {
        Response response = OrderClient.getOrdersList(null);
        response.then().statusCode(200);
        return response;
    }

    @Step("Проверка наличия поля 'orders' в ответе")
    private void verifyOrdersFieldExists(Response response) {
        response.then().body("orders", notNullValue());
    }

    @Step("Вывод информации о заказах: получено {ordersCount} заказов")
    private void printOrdersInfo(Response response) {
        int ordersCount = response.jsonPath().getList("orders").size();
        System.out.println("Получено заказов: " + ordersCount);
        if (ordersCount > 0) {
            int firstOrderId = response.jsonPath().getInt("orders[0].id");
            int firstOrderTrack = response.jsonPath().getInt("orders[0].track");
            System.out.println("Первый заказ: ID=" + firstOrderId + ", трек‑номер=" + firstOrderTrack);
        }
        System.out.println("Полный ответ: " + response.asString());
    }
}
