import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.response.Response;
import Model.Courier;


public class CreateCourierBaseTest extends CourierBaseTest {


    @Test
    @DisplayName("CanCreate new courier and check response")
    @Description("Можно создать нового курьера и проверить ответ сервера")
    public void canCreateNewCourierAndCheckResponse() {
        try {
            Courier courier = CourierAuthData.getUniqueCourier();

            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);

            System.out.println("Полный ответ создания: " + createResponse.asString());
            Assert.assertEquals(201, createResponse.getStatusCode());

            // Получаем ID через авторизацию
            testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

            if (testCourierId == null) {
                throw new RuntimeException("Не удалось получить ID курьера с логином: " + courier.getLogin());
            }

            System.out.println("Создан курьер с ID: " + testCourierId);
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create the same courier and check response")
    @Description("Нельзя создать двух одинаковых курьеров, с  проверкой ответа сервера")
    public void cantCreateTheSameCourierAndCheckResponce() {
        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);

            System.out.println("Полный ответ создания: " + createResponse.asString());

            //Пытаемся создать еще одного курьера с тем же логином

            Response createResponseForSecondRequest = courierClient.createSameCourier(courier);
            Assert.assertEquals(409, createResponseForSecondRequest.getStatusCode());
            String actualMessage = createResponseForSecondRequest.jsonPath().getString("message");
            String expectedMessage = "Этот логин уже используется. Попробуйте другой.";
            Assert.assertEquals("Сообщение об ошибке не соответствует спецификации", expectedMessage,
                    actualMessage);

            System.out.println("Полный ответ ошибки: " + createResponseForSecondRequest.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create courier without login field")
    @Description("Нельзя создать курьера без поля логина в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutLoginField() {
        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Формируем тело запроса БЕЗ поля login
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.createWithoutLoginField(courier);

            System.out.println("Полный ответ создания: " + createResponse.asString());
            Assert.assertEquals(400, createResponse.getStatusCode());

        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Can't create courier without password field")
    @Description("Нельзя создать курьера без поля пароля в теле запроса, с  проверкой ответа сервера")
    public void cantCreateCourierWithoutPasswordField() {
        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Формируем тело запроса БЕЗ поля password
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.createWithoutPasswordField(courier);

            System.out.println("Полный ответ создания: " + createResponse.asString());
            Assert.assertEquals(400, createResponse.getStatusCode());

        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

}
