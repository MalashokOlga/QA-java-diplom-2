package userLogin;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestUserAlreadyLoginCreate {
    private UserClient userClient;
    private static User user;
    private static User userSecondTry;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        userClient.login(UserCredentials.from(user));
        userSecondTry = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName()).build();
    }

    @Test
    @DisplayName("Проверка status code 403 создания уже залогиненного пользователя")
    @Description("Основной тест для POST api/auth/register")
    public void TestUserAlreadyLoginCreate() {
        ValidatableResponse createResponse = userClient.create(userSecondTry);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 403, statusCode);
        String isUserCreated = createResponse.extract().path("message");
        assertEquals("User already exists", isUserCreated);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
