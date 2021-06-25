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

@WebServlet("/allProducts")
public class AllProducts extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet allProducts");
        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("allProducts.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Database database = new Database();
        List<Product> allProducts = database.readProducts();
        StringBuilder outputStr = new StringBuilder();
        double total_cost = 0.0;
        for(int i = 0; i < allProducts.size(); i++){
            Product pr = allProducts.get(i);
            total_cost = total_cost + pr.getPrice()*pr.getAmount();
            System.out.println("Product " + i + " = " + pr);
            outputStr.append(pr).append("\n");
        }
        if(allProducts.size()!=0){
            System.out.println("Total cost = " + total_cost);
            outputStr.append("Total cost: ").append(total_cost);
            try (FileOutputStream fos = new FileOutputStream("C:\\temp\\allProducts.txt")) {
                byte[] buffer = outputStr.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}