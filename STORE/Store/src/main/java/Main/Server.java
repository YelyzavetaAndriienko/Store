package Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Databases.Database;
import Models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import org.apache.commons.codec.digest.DigestUtils;
import Models.User;

public class Server {
    /*
    private static HttpServer httpServer;
    private static Database database;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Gson gson = new Gson();
    private static final List<StringURI> uriList = new ArrayList<StringURI>() {{
        add(StringURI.of("\\/login", Server::authorizationHandler, (a, b) -> new HashMap<>()));
        add(StringURI.of("\\/Store\\/good", Server::productHandler, (a, b) -> new HashMap<>()));
        add(StringURI.of("^\\/Store\\/good\\/(\\d+)$", Server::productByIdHandler, Server::productParametersId));
    }};

    public Server(int port, Database database) throws IOException {
        this.database = database;

        this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.start();

        httpServer.createContext("/", Server::rootHandler)
                .setAuthenticator(new MyAuthenticator());
    }

    private static void rootHandler(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();

        Optional<StringURI> stringURI = uriList.stream()
                .filter(s -> s.matches(uri))
                .findFirst();

        //HttpPrincipal httpPrincipal = exchange.getPrincipal();
        //System.out.println(httpPrincipal);

        if (stringURI.isPresent()) {
            stringURI.get().handler().handle(exchange);
        }else {
            errorHandler(exchange);
        }
    }

    private static void authorizationHandler(HttpExchange exchange, Map<String, String> parameters) throws IOException {
        if(exchange.getRequestMethod().equals("POST")) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            User user = objectMapper.readValue(exchange.getRequestBody(), User.class);
            User userFromDB = database.readUserByLogin(user.getLogin());
            if(userFromDB != null) {
                if(userFromDB.getPassword().equals(DigestUtils.md5Hex(user.getPassword()))) {
                    //String jwt = ModelJWT.createJWT(userFromDB.getLogin());
                    //response(exchange, 200, jwt);

                    AuthorizationResponse response = AuthorizationResponse.of(ModelJWT.generateJwt(userFromDB.getLogin()), userFromDB.getLogin());
                    response(exchange, 200, response);
                }else {
                    response(exchange, 401, "Unauthorized (Incorrect password)");
                }
            }else {
                response(exchange, 401, "Unauthorized (Unknown user)");
            }
        }else {
            response(exchange, 405, "Incorrect method");
        }
        exchange.close();
    }

    private static void productHandler(HttpExchange exchange, Map<String, String> parameters) throws IOException {
        if(exchange.getRequestMethod().equals("PUT")) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            //ObjectMapper mapper = new ObjectMapper();
            //Product product = mapper.readValue(stringBuilder.toString(), Product.class);

            Product product = gson.fromJson(jsonToString(exchange), Product.class);

            if((product != null) && (product.getAmount() >= 0) && (product.getPrice() > 0)) {
                database.createProduct(product);
                String jwt = ModelJWT.createJWT(product.getName());
                exchange.getResponseHeaders().set("Create-Product", jwt);
                response(exchange, 201, "Created " + product.getId());
            }else {
                response(exchange, 409, "Conflict");
            }
        }else {
            response(exchange, 405, "Incorrect method");
        }
        exchange.close();
    }

    private static void productByIdHandler(HttpExchange exchange, Map<String, String> parameters) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int productId = Integer.parseInt(parameters.get("productId"));
            Product product = database.readProduct(productId);

            if (product != null) {
                response(exchange, 200, product);
            } else {
                response(exchange, 404, "Not found");
            }
        }
        else if(exchange.getRequestMethod().equals("POST")) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int productId = Integer.parseInt(parameters.get("productId"));

            //ObjectMapper mapper = new ObjectMapper();
            //Product product = mapper.readValue(stringBuilder.toString(), Product.class);

            Product product = gson.fromJson(jsonToString(exchange), Product.class);
            Product productFromDB = database.readProduct(productId);
            
            if(productFromDB != null) {
                database.updateProduct(productFromDB.getId(), product.getGId(), product.getName(), product.getDescription(),
                        product.getManufacturer(), product.getAmount(), product.getPrice());
                String jwt = ModelJWT.createJWT(productFromDB.getName());
                exchange.getResponseHeaders().set("Update-Product", jwt);
                exchange.sendResponseHeaders(204, 0);
            }else {
                response(exchange, 404, "Not found");
                response(exchange, 409, "Conflict");
            }
        }
        else if(exchange.getRequestMethod().equals("DELETE")) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            int productId = Integer.parseInt(parameters.get("productId"));
            Product product = database.readProduct(productId);

            if (product != null) {
                String jwt = ModelJWT.createJWT(product.getName());
                exchange.getResponseHeaders().set("Delete-Product", jwt);
                database.deleteProduct(product.getId());
                response(exchange, 204, "No content");
            } else {
                response(exchange, 404, "Not found");
            }
        }else {
            response(exchange, 405, "Incorrect method");
        }
    }

    private static Map<String, String> productParametersId(String uri, Pattern pattern) {
        Matcher matcher = pattern.matcher(uri);
        matcher.find();
        return new HashMap<String, String>() {{
            put("productId", matcher.group(1));
        }};
    }

    private static void errorHandler(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
    }

    private static void response(HttpExchange exchange, int status, Object response) throws IOException {
        byte[] bytes = objectMapper.writeValueAsBytes(response);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    private static String jsonToString(HttpExchange exchange) throws IOException {
        InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder stringBuilder = new StringBuilder();
        String string;
        while((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string).append("\n");
        }

        bufferedReader.close();

        return stringBuilder.toString();
    }

    public void stop() {
        this.httpServer.stop(1);
    }

    private static class MyAuthenticator extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            String jwt = httpExchange.getRequestHeaders().getFirst("Authorization");

            if(jwt != null) {
                String login = ModelJWT.getLoginFromJWT(jwt);
                User user = database.readUserByLogin(login);
                if(user != null) {
                    return new Success(new HttpPrincipal(login, "admin"));
                }
            }
            return new Failure(403);
        }
    }*/
}
