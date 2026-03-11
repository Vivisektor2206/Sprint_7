
package ru.praktikum;

import ru.praktikum.model.Courier;

public class CourierAuthData {
    // Статический метод для генерации уникального курьера
    public static Courier getUniqueCourier() {
        String uniqueLogin = "test_login_" + System.currentTimeMillis();
        return new Courier(uniqueLogin, "test_pass", "Test Name");
    }

}