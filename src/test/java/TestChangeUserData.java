package userChange;

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
public class TestChangeUserData {
    private static UserClient userClient;
    private static User user;
    private static String accessToken;
    private static String accessTokenLogged;
    @Parameterized.Parameter
    public String fieldToChange;

    @Parameterized.Parameters(name = "{index}: данные для изменения пользователя")
    public static Object[] changeData() {
        return new Object[] {"{\"email\": \"xoxo@yandex.ru\"}", "{\"password\": \"0106\"}", "{\"name\": \"Olala\"}"};
    }
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getFaker();
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessTokenLogged = loginResponse.extract().path("accessToken");
        accessTokenLogged = accessTokenLogged.replace("Bearer ", "");
    }
    @Test
    @DisplayName("Проверка status code 200 изменения пользователя")
    @Description("Основной тест для PATCH api/auth/user")
    public void TestChangeUserData() {
        ValidatableResponse changeResponse = userClient.change(accessTokenLogged, fieldToChange);
        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 200, statusCode);
        boolean isUserChange = changeResponse.extract().path("success");
        assertEquals(true, isUserChange);
    }
    @After
    public void cleanUp() {
       userClient.delete(accessToken);
    }
}
