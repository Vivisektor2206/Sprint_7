import io.restassured.RestAssured;
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
            try {
                given()
                        .log().all()
                        .pathParam("id", testCourierId)
                        .when()
                        .delete(DELETE_ENDPOINT)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .body("ok", equalTo(true));

                System.out.println("Курьер с ID '" + testCourierId + "' успешно удалён");
            } catch (Exception e) {
                System.err.println("Ошибка при удалении курьера с ID '" + testCourierId + "': " + e.getMessage());
            } finally {
                testCourierId = null;
            }
        }
    }
}
