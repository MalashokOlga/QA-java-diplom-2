import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestOrderCreateAuthorized {
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
        //int size =
        order = new Order();
    }
    @Test
    @DisplayName("Проверка status code 200 создания пользователя")
    @Description("Основной тест для POST api/auth/register")
    public void canCreateOrder() {
        ValidatableResponse createResponse = orderClient.create(order, accessToken);
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
