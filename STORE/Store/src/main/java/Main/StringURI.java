package Main;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StringURI {
    private final Pattern pattern;
    private final MyHandler httpHandler;
    private final BiFunction<String, Pattern, Map<String, String>> parameters;

    public static StringURI of(String pattern, MyHandler httpHandler, BiFunction<String, Pattern, Map<String, String>> parameters) {
        return new StringURI(Pattern.compile(pattern), httpHandler, parameters);
    }

    public boolean matches(final String uri) {
        return pattern.matcher(uri).matches();
    }

    public HttpHandler handler() {
        return exchange -> {
            Map<String, String> params = parameters.apply(exchange.getRequestURI().toString(), pattern);
            httpHandler.handle(exchange, params);
        };
    }

    public interface MyHandler {
        void handle(HttpExchange exchange, Map<String, String> parameters) throws IOException;
    }
}
