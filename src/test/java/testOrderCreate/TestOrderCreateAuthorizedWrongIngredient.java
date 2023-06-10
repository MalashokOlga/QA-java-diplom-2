package testOrderCreate;

import api.OrderClient;
import api.UserClient;
import etc.Order;
import etc.User;
import etc.UserCredentials;
import etc.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestOrderCreateAuthorizedWrongIngredient {
    private UserClient userClient;
    private static User user;
    private String accessToken;
    private OrderClient orderClient;
    private static Order order;
    List<String> ingredients = new ArrayList<>();
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        orderClient = new OrderClient();
        ingredients.add("булка");
        ingredients.add("соус");
        order = new Order(ingredients);
    }
    @Test
    @DisplayName("Проверка status code 500 создания заказа с неверным хэшем ингредиентов")
    @Description("Основной тест для POST api/orders")
    public void testOrderCreateAuthorizedWrongIngredient() {
        ValidatableResponse createResponse = orderClient.create(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 500, statusCode);
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
