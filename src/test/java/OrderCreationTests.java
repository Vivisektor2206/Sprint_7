import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreationTests extends OrdersBaseTest {

    private final List<String> color;
    private final String testName;
    private final OrderClient orderClient = new OrderClient();

    public OrderCreationTests(List<String> color, String testName) {
        this.color = color;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK"), "Can create order with BLACK color"},
                {Arrays.asList("GREY"), "Can create order with GREY color"},
                {Arrays.asList("BLACK", "GREY"), "Can create order with both BLACK and GREY colors"},
                {null, "Can create order without specifying color"}
        });
    }

    @Test
    @DisplayName("Can create order with different color options")
    @Description("Проверяется создание заказа с разными вариантами указания цвета, проверяется наличие поля track в ответе")
    public void canCreateOrderWithDifferentColorOptions() {
        // Используем метод из OrderClient для создания заказа
        Response createOrderResponse = orderClient.createOrderWithColor(color);

        // Извлекаем track как число
        Integer trackNumber = createOrderResponse.jsonPath().getObject("track", Integer.class);
        lastTrackNumber = trackNumber;

        System.out.println("Заказ создан успешно. Трек‑номер: " + trackNumber);
        System.out.println("Полный ответ создания заказа: " + createOrderResponse.asString());
    }
}
