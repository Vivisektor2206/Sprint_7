import io.qameta.allure.Step;

import java.util.List;

import Model.Order;

class OrderUtils {

    @Step("Отправка запроса для формирования тела заказа с указанным цветом или без цвета")
    static Order createOrderWithColor(List<String> colors) {
        return new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );
    }
}


