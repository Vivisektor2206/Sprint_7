import com.google.gson.Gson;
import io.qameta.allure.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderUtils {

    @Step("Отправка запроса для формирования тела заказа с указанным цветом или без цвета")
    static Map<String, Object> createOrderBodyWithColor(List<String> colors) {
        Map<String, Object> orderBody = new HashMap<>();
        orderBody.put("firstName", "Naruto");
        orderBody.put("lastName", "Uchiha");
        orderBody.put("address", "Konoha, 142 apt.");
        orderBody.put("metroStation", 4);
        orderBody.put("phone", "+7 800 355 35 35");
        orderBody.put("rentTime", 5);
        orderBody.put("deliveryDate", "2020-06-06");
        orderBody.put("comment", "Saske, come back to Konoha");

        if (colors != null && !colors.isEmpty()) {
            orderBody.put("color", colors);
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(orderBody);
        return gson.fromJson(jsonString, Map.class);
    }
}

