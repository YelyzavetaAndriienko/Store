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

@WebServlet("/deleteProduct")
public class DeleteProductServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet deleteProduct");

        response.setContentType("text/html");
       // List<Product> products = Database.readProducts();
       // request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("deleteProduct.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost for deleteProduct");
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
            String name = jsonObject.getString("name");
            Product g = null;
            List<Product> productsList = database.readProducts();
            for (int i = 0; i < productsList.size(); i++) {
                if (name.equals(productsList.get(i).getName())) {
                    g = productsList.get(i);
                }
            }
            if(g!=null){
            System.out.println(g);
            database.deleteProduct(g.getId());
            JSONObject jsonToReturn1 = new JSONObject();
            jsonToReturn1.put("answer", "ok");
            out.println(jsonToReturn1.toString());
            System.out.println(jsonToReturn1.toString());

            System.out.println(database.readProducts());
            } else {
                System.out.println("There is no product with such name");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}