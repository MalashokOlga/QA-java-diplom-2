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
public class TestChangeUserDataNotLogin {
    private static UserClient userClient;
    private static User user;
    private static String accessToken;
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
        System.out.println(user);
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
        accessToken = accessToken.replace("Bearer ", "");
    }
    @Test
    @DisplayName("Проверка status code 401 изменения незалогиненного пользователя")
    @Description("Основной тест для PATCH api/auth/user")
    public void TestChangeUserData() {
        ValidatableResponse changeResponse = userClient.change(accessToken, fieldToChange);
        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Неверный код ответа!", 401, statusCode);
        String isUserChange = changeResponse.extract().path("message");
        assertEquals("You should be authorised", isUserChange);
    }
    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
