package testOrderGet;


import api.OrderClient;
import api.UserClient;
import etc.User;
import etc.UserCredentials;
import etc.UserGenerator;import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetOrdersAuthorizedUser {
    private UserClient userClient;
    private static User user;
    private String accessToken;
    private OrderClient orderClient;
        @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = loginResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        orderClient = new OrderClient();
    }
    @Test
    @DisplayName("Проверка status code 200 получение заказов авторизованного пользователя")
    @Description("Основной тест для GET api/orders")
    public void testGetOrdersAuthorizedUser() {
        ValidatableResponse createResponse = orderClient.getUserOrders(accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        assertTrue(createResponse.extract().path("success"));
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
