import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static io.restassured.RestAssured.given;


public class OrdersBaseTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    protected static final String CANCEL_ORDER_ENDPOINT = "/api/v1/orders/cancel";

    protected static Integer lastTrackNumber;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterClass
    public static void cancelLastOrder() {
        if (lastTrackNumber != null) {
            // Формируем JSON вручную — без кавычек вокруг числа
            String cancelBody = String.format("{\"track\":%d}", lastTrackNumber);

            Response cancelResponse = given()
                    .header("Content-Type", "application/json")
                    .body(cancelBody)
                    .when()
                    .put(CANCEL_ORDER_ENDPOINT)
                    .then()
                    .extract().response();

            int statusCode = cancelResponse.statusCode();
            String responseBody = cancelResponse.asString();

            if (statusCode == 200) {
                System.out.println("Заказ успешно отменён. Трек‑номер: " + lastTrackNumber);
            } else if (statusCode == 404) {
                System.out.println("Заказ не найден (возможно, уже отменён). Трек‑номер: " + lastTrackNumber);
            } else {
                System.err.println("Ошибка отмены заказа. Трек‑номер: " + lastTrackNumber +
                        ", статус: " + statusCode + ", ответ: " + responseBody);
                System.err.println("Тело запроса на отмену: " + cancelBody);
            }
        }
    }

}


