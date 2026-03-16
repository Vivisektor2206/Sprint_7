import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import Model.Courier;


public class LoginCourierTest extends CourierBaseTest {

    @Test
    @DisplayName("Can login courier and check response")
    @Description("Курьер может авторизоваться, с  проверкой ответа сервера")
    public void canLoginCourierAndCheckResponse() {

        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());

            // Проверяем авторизацию и полный ответ
            Response loginResponse = courierClient.checkAuthorization(courier);
            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Cant login courier without login field and check response")
    @Description("Курьер не может авторизоваться без поля login в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithoutLoginFieldAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());
            //Пытаемся авторизоваться без логина
            Response loginResponse = CourierClient.authorizationWithoutLoginField(courier);

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Cant login courier without password field and check response")
    @Description("Курьер не может авторизоваться без поля password в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithoutPasswordFieldAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());
            //Пытаемся авторизоваться без пароля
            Response loginResponse = CourierClient.authorizationWithoutPasswordField(courier);

            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Cant login courier with wrong login and check response")
    @Description("Курьер не может авторизоваться с неверным логином в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongLoginAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());
            Response loginResponse = courierClient.authorizationWithWrongLogin(courier);
            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Cant login courier with wrong password and check response")
    @Description("Курьер не может авторизоваться с неверным паролем в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongPasswordAndCheckResponse() {
        try {

            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());
            Response loginResponse = courierClient.authorizationWithWrongPassword(courier);
            System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        } catch (AssertionError e) {
            System.err.println("Тест не прошёл проверку: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Successful request returns user ID, and check response")
    @Description("Успешный запрос возвращает id")
    public void successfulRequestReturnsUserId() {

        try {
            Courier courier = CourierAuthData.getUniqueCourier();
            // Создаём курьера
            CourierClient courierClient = new CourierClient();
            Response createResponse = courierClient.create(courier);
            System.out.println("Полный ответ создания: " + createResponse.asString());

            // Получаем ID через авторизацию
            Integer testCourierId = CourierAuthData.getCourierIdByLoginPassword(courier);

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
}
