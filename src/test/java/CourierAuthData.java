import Model.Courier;
import io.qameta.allure.Step;


public class CourierAuthData {

    // Статический метод для генерации уникального курьера
    @Step("Генерация данных для уникального курьера")
    public static Courier getUniqueCourier() {
        String uniqueLogin = "test_login_" + System.currentTimeMillis();
        String uniquePassword = "test_password_" + System.currentTimeMillis();
        String uniqueName = "test_name_" + System.currentTimeMillis();

        return new Courier(uniqueLogin, uniquePassword, uniqueName);
    }
}

