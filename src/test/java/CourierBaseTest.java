import Model.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

public class CourierBaseTest {
    protected Integer testCourierId;
    protected Courier testCourier; // Храним курьера для использования в тестах
    static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        // Создаём тестового курьера перед каждым тестом
        testCourier = CourierAuthData.getUniqueCourier();
        CourierClient courierClient = new CourierClient();
        Response createResponse = courierClient.create(testCourier);

        // Переносим проверку статуса создания курьера в setUp
        createResponse.then().statusCode(SC_CREATED);

        testCourierId = CourierAuthData.getCourierIdByLoginPassword(testCourier);
    }

    @After
    public void cleanupTestCourierById() {
        if (testCourierId != null) {
            CourierClient courierClient = new CourierClient();
            Response deleteResponse = courierClient.deleteCourier(testCourierId.toString());

            // Добавляем проверки при удалении курьера
            deleteResponse.then()
                    .statusCode(SC_OK)
                    .body("ok", equalTo(true));

            System.out.println("Курьер с ID " + testCourierId + " удалён. Ответ: " + deleteResponse.asString());
        } else {
            System.out.println("testCourierId не установлен — пропуск удаления");
        }
    }
}

