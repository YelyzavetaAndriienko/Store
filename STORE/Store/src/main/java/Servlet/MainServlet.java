package Servlet;

import Databases.Database;
import Models.Group;
import Models.Product;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

@WebServlet("/")
public class MainServlet extends HttpServlet {
    private Database database;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet");

        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost");
        database = new Database();
        StringBuilder jb = new StringBuilder();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            JSONObject jsonObject = new JSONObject(jb.toString());

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            int command = jsonObject.getInt("command");

            switch (command) {

                case 0: //get all groups

                    List<Product> names = Database.readProducts();

                    JSONObject jsonToReturn0 = new JSONObject();
                    jsonToReturn0.put("answer", "names");
                    jsonToReturn0.put("list", names.toString());
                    out.println(jsonToReturn0.toString());

                    break;

                case 1: //create group
/*
                    int gId = jsonObject.getInt("groupId");
                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");
                    String manufacturer = jsonObject.getString("manufacturer");
                    int amount = jsonObject.getInt("amount");
                    double price = jsonObject.getDouble("price");

                    Product product = new Product(gId, name, description, manufacturer, amount, price);

                    Database.createProduct(product);
*/

                    String name = jsonObject.getString("name");
                    String description = jsonObject.getString("description");

                    Group group = new Group(name, description);
                    System.out.println(group);
                    database.createGroup(group);
                    JSONObject jsonToReturn1 = new JSONObject();
                    jsonToReturn1.put("answer", "ok");
                    out.println(jsonToReturn1.toString());
                    System.out.println(jsonToReturn1.toString());

                    String text = jsonToReturn1.toString() + "\n  ------- \n" + group.toString();

                    try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Khrys\\Documents\\notes.txt")) {
                        // перевод строки в байты
                        byte[] buffer = text.getBytes();

                        fos.write(buffer, 0, buffer.length);
                    } catch (IOException ex) {

                        System.out.println(ex.getMessage());
                    }

                default:
                    System.out.println("default switch");
                    break;

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        /*
        try {
            database.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

         */
    }

}