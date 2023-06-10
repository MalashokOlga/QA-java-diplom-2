package etc;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserGenerator {
    public static User getFaker() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(3, 6);
        final String name = faker.name().firstName();
        return new User(email, password, name);
    }
}
