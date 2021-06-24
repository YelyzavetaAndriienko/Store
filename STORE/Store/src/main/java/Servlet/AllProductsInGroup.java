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
        RequestDispatcher dispatcher = request.getRequestDispatcher("allProductsInGroup.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost allProductsInGroup");
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

            String gname = jsonObject.getString("name");
            System.out.println("Group name is " + gname);
            System.out.println("Step 119: ");
            List<Group> groups = database.readGroups();
            System.out.println("Step 120: ");
            Group group = null;
            System.out.println("Step 121: ");
            for (int i = 0; i < groups.size(); i++) {
                System.out.println("Step 122: " + i + " of " + groups.size());
                if (gname.equals(groups.get(i).getName())) {
                    group = groups.get(i);
                    System.out.println("group is found!");
                    System.out.println(group);
                }
            }
            System.out.println("Step 123: ");
            if (group != null) {
                int gId = group.getId();
                System.out.println("Step 124: ");
                ArrayList<Product> productsFromGroup = database.getAllProductsFromGroup(gId);
                System.out.println("Step 125: ");
                for(int i = 0; i < productsFromGroup.size(); i++){
                    Product pr = productsFromGroup.get(i);
                    System.out.println("Product " + i + " = " + pr);
                }
                /*response.setContentType("text/html");
                PrintWriter out1 = response.getWriter();

                // send HTML page to client
                out1.println("<html>");
                out1.println("<head><title>A Test Servlet</title></head>");
                out1.println("<body>");
                out1.println("<h1>Test</h1>");
                out1.println("<p>Simple servlet for testing.</p>");
                out1.println("</body></html>");*/

                JSONObject jsonToReturn1 = new JSONObject();
                jsonToReturn1.put("answer", "ok");
                out.println(jsonToReturn1.toString());
                System.out.println(jsonToReturn1.toString());

                //System.out.println(database.readProducts());
                //String text = jsonToReturn1.toString() + "\n  ------- \n" + product.toString();
            } else {
                System.out.println("There is no group with such name");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}