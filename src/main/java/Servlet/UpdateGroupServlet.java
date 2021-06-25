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

@WebServlet("/update")
public class UpdateGroupServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet update");

        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateGroup.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost - update group");
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
            String gname = jsonObject.getString("gname");
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
                String name = jsonObject.getString("name");
                String description = jsonObject.getString("description");
                database.updateGroup(gId, name, description);
                System.out.println("Group updated: ");
            } else {
                System.out.println("There is no group with such name!");
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}