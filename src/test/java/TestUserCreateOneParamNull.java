package userCreate;

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
public class TestUserCreateOneParamNull {
    private UserClient userClient;
    private static User user;

    @Parameterized.Parameter
    public String email;
    @Parameterized.Parameter(1)
    public String password;
    @Parameterized.Parameter(2)
    public String name;

    @Parameterized.Parameters(name = "{index}: данные для создания пользователя")
    public static Object[][] createData() {
        user = UserGenerator.getFaker();
        return new Object[][] {{null, user.getPassword(), user.getName()},
                {user.getEmail(), null, user.getName()},
                {user.getEmail(), user.getPassword(), null}};
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User(email, password, name);
    }

    @Test
    @DisplayName("Проверка status code 403 создания пользователя без обязательного поля")
    @Description("Основной тест для POST api/auth/register")
    public void TestUserCreateOneParamNull() {
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 403, statusCode);
        String isUserCreated = createResponse.extract().path("message");
        assertEquals("Email, password and name are required fields", isUserCreated);
    }
}
