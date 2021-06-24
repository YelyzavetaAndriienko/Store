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

import static Databases.CommonDB.database;

@WebServlet("/CreateProduct")
public class CreateProductServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet - create product");

        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("createProduct.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost - create product");
        StringBuilder jb = new StringBuilder();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        Database database = new Database();
        try {
            JSONObject jsonObject = new JSONObject(jb.toString());

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            String gname = jsonObject.getString("gname");
            List<Group> groups = database.readGroups();
            Group group = null;

            for (int i = 0; i < groups.size(); i++) {
                if (gname.equals(groups.get(i).getName())) {
                    group = groups.get(i);
                }
            }
            int gId = group.getId();
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            String manufacturer = jsonObject.getString("manufacturer");
            int amount = jsonObject.getInt("amount");
            double price = jsonObject.getDouble("price");

            Product product = new Product(gId, name, description, manufacturer, amount, price);

            database.createProduct(product);

            System.out.println(product.toString());
            JSONObject jsonToReturn1 = new JSONObject();
            jsonToReturn1.put("answer", "ok");
            out.println(jsonToReturn1.toString());
            System.out.println(jsonToReturn1.toString());

            System.out.println(database.readProducts());

            String text = jsonToReturn1.toString() + "\n  ------- \n" + product.toString();

            try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Liza\\Documents\\notes.txt")) {
                byte[] buffer = text.getBytes();

                fos.write(buffer, 0, buffer.length);
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }
}