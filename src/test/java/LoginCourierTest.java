import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import Model.Courier;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


public class LoginCourierTest extends CourierBaseTest {

@Test
@DisplayName("Can login courier and check response")
@Description("Курьер может авторизоваться, с проверкой ответа сервера")
public void canLoginCourierAndCheckResponse() {
    Courier courier = CourierAuthData.getUniqueCourier();
    CourierClient courierClient = new CourierClient();

    Response createResponse = courierClient.create(courier);
    System.out.println("Полный ответ создания: " + createResponse.asString());

    createResponse.then()
            .statusCode(201);

    Response loginResponse = courierClient.authorization(courier);
    System.out.println("Полный ответ авторизации: " + loginResponse.asString());
    loginResponse.then()
            .statusCode(200)
            .body("id", notNullValue());

}



    @Test
    @DisplayName("Cant login courier without login field and check response")
    @Description("Курьер не может авторизоваться без поля login в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithoutLoginFieldAndCheckResponse() {
        Courier courier = CourierAuthData.getUniqueCourier();
        CourierClient courierClient = new CourierClient();

        Response createResponse = courierClient.create(courier);
        System.out.println("Полный ответ создания: " + createResponse.asString());

        Courier courierWithoutPassword = new Courier(null, courier.getPassword(), null);

        Response loginResponse = courierClient.authorization(courierWithoutPassword);
        System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        loginResponse.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }


    @Test
    @DisplayName("Cant login courier without password field and check response")
    @Description("Курьер не может авторизоваться без поля password в теле запроса, с проверкой ответа сервера")
    public void cantLoginCourierWithoutPasswordFieldAndCheckResponse() {
        Courier courier = CourierAuthData.getUniqueCourier();
        CourierClient courierClient = new CourierClient();

        Response createResponse = courierClient.create(courier);
        System.out.println("Полный ответ создания: " + createResponse.asString());

        Courier courierWithoutPassword = new Courier(courier.getLogin(), null, null);

        Response loginResponse = courierClient.authorization(courierWithoutPassword);
        System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        loginResponse.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Cant login courier with wrong login and check response")
    @Description("Курьер не может авторизоваться с неверным логином в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongLoginAndCheckResponse() {
        Courier courier = CourierAuthData.getUniqueCourier();
        CourierClient courierClient = new CourierClient();

        Response createResponse = courierClient.create(courier);
        System.out.println("Полный ответ создания: " + createResponse.asString());

        Courier courierWithoutPassword = new Courier(courier.getLogin() + "123", courier.getPassword(), null);

        Response loginResponse = courierClient.authorization(courierWithoutPassword);
        System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        loginResponse.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Cant login courier with wrong password and check response")
    @Description("Курьер не может авторизоваться с неверным паролем в теле запроса, с  проверкой ответа сервера")
    public void cantLoginCourierWithWrongPasswordAndCheckResponse() {
        Courier courier = CourierAuthData.getUniqueCourier();
        CourierClient courierClient = new CourierClient();

        Response createResponse = courierClient.create(courier);
        System.out.println("Полный ответ создания: " + createResponse.asString());

        Courier courierWithoutPassword = new Courier(courier.getLogin(), courier.getPassword() + "123", null);

        Response loginResponse = courierClient.authorization(courierWithoutPassword);
        System.out.println("Полный ответ авторизации: " + loginResponse.asString());
        loginResponse.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Successful request returns user ID, and check response")
    @Description("Успешный запрос возвращает id")
    public void successfulRequestReturnsUserId() {
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
    }

}
