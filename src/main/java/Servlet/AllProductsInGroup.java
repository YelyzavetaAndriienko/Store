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
            List<Group> groups = database.readGroups();
            Group group = null;
            for (int i = 0; i < groups.size(); i++) {
                if (gname.equals(groups.get(i).getName())) {
                    group = groups.get(i);
                    //System.out.println("group is found!");
                    //System.out.println(group);
                }
            }
            if (group != null) {
                int gId = group.getId();
                ArrayList<Product> productsFromGroup = database.getAllProductsFromGroup(gId);
                StringBuilder outputStr = new StringBuilder();
                double total_cost = 0.0;
                for (int i = 0; i < productsFromGroup.size(); i++) {
                    Product pr = productsFromGroup.get(i);
                    total_cost = total_cost + pr.getPrice() * pr.getAmount();
                    System.out.println(pr);
                    outputStr.append(pr).append("\n");
                }
                if (productsFromGroup.size() != 0) {
                    System.out.println("Total cost = " + total_cost);
                    outputStr.append("Total cost: ").append(total_cost);
                    try (FileOutputStream fos = new FileOutputStream("C:\\temp\\allProductsInGroup.txt")) {
                        byte[] buffer = outputStr.toString().getBytes();
                        fos.write(buffer, 0, buffer.length);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                JSONObject jsonToReturn1 = new JSONObject();
                jsonToReturn1.put("answer", "ok");
                out.println(jsonToReturn1.toString());
                System.out.println(jsonToReturn1.toString());
            } else {
                System.out.println("There is no group with such name");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}