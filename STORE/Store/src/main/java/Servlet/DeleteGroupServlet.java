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

@WebServlet("/delete")
public class DeleteGroupServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet delete");

        response.setContentType("text/html");
        // List<Group> groups = Database.readGroups();
        // request.setAttribute("groups", groups);
        RequestDispatcher dispatcher = request.getRequestDispatcher("deleteGroup.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost for delete");
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
            Group g = null;
            List<Group> groupsList = database.readGroups();
            for (int i = 0; i < groupsList.size(); i++) {
                if (name.equals(groupsList.get(i).getName())) {
                    g = groupsList.get(i);
                }
            }
            if(g!=null){
            System.out.println(g);
            database.deleteGroup(g.getId());
            JSONObject jsonToReturn1 = new JSONObject();
            jsonToReturn1.put("answer", "ok");
            out.println(jsonToReturn1.toString());
            System.out.println(jsonToReturn1.toString());

            System.out.println(database.readGroups());
        } else {
            System.out.println("There is no group with such name");
        }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}