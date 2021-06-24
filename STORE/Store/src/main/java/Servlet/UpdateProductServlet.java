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
        // List<Group> groups = Database.readGroups();
        // request.setAttribute("groups", groups);
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateProduct.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    /*protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }*/

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doGet");
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

       // System.out.println("hello");
        try {
     /*       JSONObject jsonObject = new JSONObject(jb.toString());
            Group group = (Group) jsonObject.get("group");

            System.out.println("-------------------------------" + group.toString());
*/
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}