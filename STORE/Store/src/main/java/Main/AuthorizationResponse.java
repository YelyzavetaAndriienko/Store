package Main;

import lombok.Data;

@Data(staticConstructor = "of")
public class AuthorizationResponse {

    private final String token;
    private final String login;
}
