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

import static org.junit.Assert.assertEquals;

public class TestOrderCreateAuthorizedNoIngredient {
    private UserClient userClient;
    private static User user;
    private String accessToken;
    private OrderClient orderClient;
    private static Order order;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        userClient.login(UserCredentials.from(user));
        orderClient = new OrderClient();
        order = new Order();
    }
    @Test
    @DisplayName("Проверка status code 400 создания заказа без ингредиентов")
    @Description("Основной тест для POST api/orders")
    public void testOrderCreateAuthorizedNoIngredient() {
        ValidatableResponse createResponse = orderClient.create(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 400, statusCode);
        String isOrderCreated = createResponse.extract().path("message");
        assertEquals("Неверный ответ!", "Ingredient ids must be provided", isOrderCreated);
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
