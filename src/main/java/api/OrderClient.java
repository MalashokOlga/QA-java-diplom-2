package api;

import etc.Order;
import etc.RestClient;
import io.restassured.response.ValidatableResponse;
import lombok.NonNull;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_CREATE_GET = "api/orders";
    public ValidatableResponse create(Order order, @NonNull String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .and()
                .body(order)
                .when()
                .post(ORDER_CREATE_GET)
                .then();
    }
    public ValidatableResponse getUserOrders(@NonNull String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token)
                .when()
                .get(ORDER_CREATE_GET)
                .then();
    }
}
