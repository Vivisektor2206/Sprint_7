/*import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static io.restassured.RestAssured.given;


public class OrdersBaseTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";


    protected static Integer lastTrackNumber;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterClass
    public static void cancelLastOrder() {
        if (lastTrackNumber != null) {
            // Формируем JSON вручную — без кавычек вокруг числа

        orderClient.createJsonRequest();


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
*/
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static io.restassured.RestAssured.given;

public class OrdersBaseTest {

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    protected static Integer lastTrackNumber;
    // Инициализируем orderClient для использования в @AfterClass
    private static OrderClient orderClient = new OrderClient();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterClass
    public static void cancelLastOrder() {
        if (lastTrackNumber != null) {
            // Вызываем метод отмены заказа и получаем статус
            int statusCode = orderClient.cancelOrderByTrack(lastTrackNumber);

            if (statusCode == 200) {
                System.out.println("Заказ успешно отменён. Трек‑номер: " + lastTrackNumber);
            } else if (statusCode == 404) {
                System.out.println("Заказ не найден (возможно, уже отменён). Трек‑номер: " + lastTrackNumber);
            } else {
                System.err.println("Ошибка отмены заказа. Трек‑номер: " + lastTrackNumber +
                        ", статус: " + statusCode);
            }
        }
    }
}


