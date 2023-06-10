package api;

import etc.RestClient;
import etc.User;
import etc.UserCredentials;
import io.restassured.response.ValidatableResponse;
import lombok.NonNull;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_CREATE = "api/auth/register";
    private static final String USER_LOGIN = "api/auth/login";
    private static final String USER_LOGOUT = "api/auth/logout";
    private static final String USER_CHANGE = "api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_CREATE)
                .then();
    }

    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_LOGIN)
                .then();
    }

    public ValidatableResponse logout(@NonNull String token) {
        return given()
                .spec(getBaseSpec())
                .body(token)
                .when()
                .post(USER_LOGOUT)
                .then();
    }

    public ValidatableResponse change(@NonNull String token, String field) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(field)
                .when()
                .patch(USER_CHANGE)
                .then();
    }

    public ValidatableResponse delete(@NonNull String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .delete(USER_CHANGE)
                .then();
    }
}
