import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrdersBaseTest {


    protected String lastTrackNumber;
    private OrderClient orderClient = new OrderClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = CourierBaseTest.BASE_URL;
        Response createResponse = orderClient.createOrderWithColor(null);

        // Добавляем проверки при создании заказа в setUp
        createResponse.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        lastTrackNumber = createResponse.jsonPath().getString("track");
        System.out.println("Заказ создан для теста. Трек‑номер: " + lastTrackNumber);
    }

    @After
    @SuppressWarnings("PMD.SystemPrintln")
    public void cancelLastOrder() {
        if (lastTrackNumber != null) {
            int statusCode = orderClient.cancelOrderByTrack(lastTrackNumber);

            if (statusCode == SC_OK) {
                System.out.println("Заказ успешно отменён. Трек‑номер: " + lastTrackNumber);
            } else if (statusCode == SC_NOT_FOUND) {
                System.out.println("Заказ не найден (возможно, уже отменён). Трек‑номер: " + lastTrackNumber);
            } else {
                System.err.println("Ошибка отмены заказа. Трек‑номер: " + lastTrackNumber + ", статус: " + statusCode);
            }
        }
    }
}



