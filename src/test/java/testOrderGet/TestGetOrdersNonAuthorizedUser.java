package testOrderGet;

import api.UserClient;
import etc.User;
import etc.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import api.OrderClient;

import static org.junit.Assert.assertEquals;

public class TestGetOrdersNonAuthorizedUser {
    private UserClient userClient;
    private static User user;
    private String accessToken;
    private OrderClient orderClient;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        orderClient = new OrderClient();
    }
    @Test
    @DisplayName("Проверка status code 200 получение заказов неавторизованного пользователя")
    @Description("Основной тест для GET api/orders")
    public void testGetOrdersAuthorizedUser() {
        ValidatableResponse createResponse = orderClient.getUserOrders(accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        boolean isOrdersGet = createResponse.extract().path("success");
        assertEquals("Неверный ответ!", true, isOrdersGet);
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
