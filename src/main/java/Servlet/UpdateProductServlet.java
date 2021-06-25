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

@WebServlet("/updateProduct")
public class UpdateProductServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet updateProduct");
        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateProduct.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }


    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }*/

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost - update product");
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
            String oldname = jsonObject.getString("oldname");
            List<Product> products = database.readProducts();
            System.out.println("Step 120: ");
            Product product = null;
            System.out.println("Step 121: ");
            for (int i = 0; i < products.size(); i++) {
                System.out.println("Step 122: " + i + " of " + products.size());
                if (oldname.equals(products.get(i).getName())) {
                    product = products.get(i);
                    System.out.println("group is found!");
                    System.out.println(product);
                }
            }
            System.out.println("Step 123: ");
            if (product != null) {
                int id = product.getId();
                String gname = jsonObject.getString("gname");
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                String manufacturer = jsonObject.getString("manufacturer");
                int amount = jsonObject.getInt("amount");
                double price = jsonObject.getDouble("price");

                List<Group> groups = database.readGroups();
                Group group = null;
                for (int i = 0; i < groups.size(); i++) {
                    if (gname.equals(groups.get(i).getName())) {
                        group = groups.get(i);
                        System.out.println("group is found!");
                        System.out.println(group);
                    }
                }
                if(group!=null){
                    int gId = group.getId();
                    database.updateProduct(id, gId, name, description, manufacturer, amount, price);
                    System.out.println("Product updated");
                } else {
                    System.out.println("There is no group with such name!");
                }
            } else {
                System.out.println("There is no product with such name!");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}