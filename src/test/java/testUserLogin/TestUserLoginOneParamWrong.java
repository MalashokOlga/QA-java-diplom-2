package testUserLogin;

import api.UserClient;
import etc.User;
import etc.UserCredentials;
import etc.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TestUserLoginOneParamWrong {
    private static UserClient userClient;
    private static User user;
    private static String accessToken;

    @Parameterized.Parameter
    public String email;
    @Parameterized.Parameter(1)
    public String password;

    @Parameterized.Parameters(name = "{index}: данные для логина пользователя")
    public static Object[][] loginData() {
        user = UserGenerator.getFaker();
        userClient = new UserClient();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        return new Object[][] {{"user@email.ru", user.getPassword()},
                                {user.getEmail(), "userPassword"}};
    }

    @Before
    public void setUp() {
        user = new User(email, password, user.getName());
    }

    @Test
    @DisplayName("Проверка status code 401 логина пользователя с неправильным полем")
    @Description("Основной тест для POST api/auth/login")
    public void testUserLoginOneParamWrong() {
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        int statusCode = loginResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 401, statusCode);
        String isUserLogin = loginResponse.extract().path("message");
        assertEquals("email or password are incorrect", isUserLogin);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
