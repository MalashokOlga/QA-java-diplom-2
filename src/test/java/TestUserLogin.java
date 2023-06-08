package userLogin;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUserLogin {
    private UserClient userClient;
    private static User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        userClient.create(user);
    }

    @Test
    @DisplayName("Проверка status code 200 логина пользователя")
    @Description("Основной тест для POST api/auth/login")
    public void TestUserLogin() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        accessToken = loginResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        boolean isUserLogin = loginResponse.extract().path("success");
        assertEquals("Неверный ответ!", true, isUserLogin);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
