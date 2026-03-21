import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CourierBaseTest {
    protected Integer testCourierId;
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String DELETE_ENDPOINT = "/api/v1/courier/{id}";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void cleanupTestCourierById() {
        if (testCourierId != null) {
            CourierClient courierClient = new CourierClient();
            Response deleteResponse = courierClient.deleteCourier(testCourierId.toString());
            System.out.println("Курьер с ID " + testCourierId + " удалён. Ответ: " + deleteResponse.asString());
        }
    }

}
