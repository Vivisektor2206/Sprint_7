import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.response.Response;
import Model.Courier;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTests extends CourierBaseTest {

    @Test
    @DisplayName("CanCreate new courier and check response")
    @Description("Можно создать нового курьера и проверить ответ сервера")
    public void canCreateNewCourierAndCheckResponse() {
        Courier courier = CourierAuthData.getUniqueCourier();

        // Создаём курьера
        CourierClient courierClient = new CourierClient();
        Response createResponse = courierClient.create(courier);

        System.out.println("Полный ответ создания: " + createResponse.asString());
        Assert.assertEquals(SC_CREATED, createResponse.getStatusCode());

        // Получаем ID через авторизацию
        testCourierId = CourierClient.getCourierIdByLoginPassword(courier);

        if (testCourierId == null) {
            throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
        }
    }

    @Test
    @DisplayName("Can't create the same courier and check response")
    @Description("Нельзя создать двух одинаковых курьеров, с  проверкой ответа сервера")
    public void cantCreateTheSameCourierAndCheckResponce() {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Создаём курьера
        CourierClient courierClient = new CourierClient();
        Response createResponse = courierClient.create(courier);

        System.out.println("Полный ответ создания: " + createResponse.asString());

        // Пытаемся создать ещё одного курьера с тем же логином
        Response createResponseForSecondRequest = courierClient.create(courier);
        Assert.assertEquals(SC_CONFLICT, createResponseForSecondRequest.getStatusCode());
        String actualMessage = createResponseForSecondRequest.jsonPath().getString("message");
        String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
        Assert.assertEquals("Сообщение об ошибке не соответствует спецификации", expectedMessage, actualMessage);

        System.out.println("Полный ответ ошибки: " + createResponseForSecondRequest.asString());
    }

    @Test
    @DisplayName("Can't create courier without login field")
    @Description("Нельзя создать курьера без поля логина в теле запроса, с проверкой ответа сервера")
    public void cantCreateCourierWithoutLoginField() {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Создаём объект без логина
        Courier courierWithoutLogin = new Courier(null, courier.getPassword(), null);

        CourierClient courierClient = new CourierClient();
        Response createResponse = courierClient.create(courierWithoutLogin);

        System.out.println("Полный ответ создания: " + createResponse.asString());
        createResponse.then().statusCode(SC_BAD_REQUEST).body("message", CoreMatchers.containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Can't create courier without password field")
    @Description("Нельзя создать курьера без поля пароля в теле запроса, с проверкой ответа сервера")
    public void cantCreateCourierWithoutPasswordField() {
        Courier courier = CourierAuthData.getUniqueCourier();
        // Создаём объект без пароля
        Courier courierWithoutPassword = new Courier(courier.getLogin(), null, null);

        CourierClient courierClient = new CourierClient();
        Response createResponse = courierClient.create(courierWithoutPassword);

        System.out.println("Полный ответ создания: " + createResponse.asString());
        createResponse.then().statusCode(SC_BAD_REQUEST).body("message", CoreMatchers.containsString("Недостаточно данных для создания учетной записи"));
    }
}
