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

@WebServlet("/allProductsInGroup")
public class AllProductsInGroup extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet allProductsInGroup");

        response.setContentType("text/html");
        // List<Group> groups = Database.readGroups();
        // request.setAttribute("groups", groups);
        RequestDispatcher dispatcher = request.getRequestDispatcher("allProductsInGroup.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost allProductsInGroup");
    }

}