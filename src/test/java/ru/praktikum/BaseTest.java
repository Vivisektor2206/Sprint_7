package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class BaseTest {
    protected Integer testCourierId;
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @After
    @DisplayName("Clean up test courier by ID")
    @Description("Удаляет созданного курьера по ID через DELETE /api/v1/courier/:id")
    public void cleanupTestCourierById() {
        if (testCourierId != null) {
            try {
                given()
                        .log().all()
                        .pathParam("id", testCourierId)
                        .when()
                        .delete("/api/v1/courier/{id}")
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
