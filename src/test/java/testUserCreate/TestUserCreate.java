package testUserCreate;

import api.UserClient;
import etc.User;
import etc.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestUserCreate {
    private UserClient userClient;
    private static User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
    }

    @Test
    @DisplayName("Проверка status code 200 создания пользователя")
    @Description("Основной тест для POST api/auth/register")
    public void canCreateUser() {
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        assertTrue(createResponse.extract().path("success"));
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
