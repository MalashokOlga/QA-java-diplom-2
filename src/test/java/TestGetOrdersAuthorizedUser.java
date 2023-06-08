import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestCetOrdersAuthorizedUser {
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
    @DisplayName("Проверка status code 200 создания заказа с авторизацией")
    @Description("Основной тест для POST api/auth/register")
    public void testOrderCreateAuthorized() {
        ValidatableResponse createResponse = orderClient.getUserOrders(accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        boolean isOrderCreated = createResponse.extract().path("success");
        assertEquals("Неверный ответ!", true, isOrderCreated);
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
