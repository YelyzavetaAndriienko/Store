package Servlet;

import Databases.Database;

import Models.Product;
import Models.ProductFilter;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet search");
        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost - search product");
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
            String manufacturer = jsonObject.getString("manufacturer");
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            String samountFromId = jsonObject.getString("amountFromId");
            String samountToId = jsonObject.getString("amountToId");
            String spriceFromId = jsonObject.getString("priceFromId");
            String spriceToId = jsonObject.getString("priceToId");
            Double priceFromId = null;
            Double priceToId = null;
            Integer amountFromId = null;
            Integer amountToId = null;
            if (samountFromId != null) {
                if (!samountFromId.equals("")) {
                    amountFromId = Integer.valueOf(samountFromId);
                }
            }
            if (samountToId != null) {
                if (!samountToId.equals("")) {
                    amountToId = Integer.valueOf(samountToId);
                }
            }
            if (spriceFromId != null) {
                if (!spriceFromId.equals("")) {
                    priceFromId = Double.valueOf(spriceFromId);
                }
            }
            if (spriceToId != null) {
                if (!spriceToId.equals("")) {
                    priceToId = Double.valueOf(spriceToId);
                }
            }
            ProductFilter filter = new ProductFilter(name, description, manufacturer, priceFromId, priceToId, amountFromId, amountToId);
            List<Product> products = database.listByCriteriaProduct(filter);
            StringBuilder outputStr = new StringBuilder();
            for (int i = 0; i < products.size(); i++) {
                Product pr = products.get(i);
                System.out.println(pr);
                outputStr.append(pr).append("\n");
            }
            try (FileOutputStream fos = new FileOutputStream("C:\\temp\\searchResults.txt")) {
                byte[] buffer = outputStr.toString().getBytes();
                fos.write(buffer, 0, buffer.length);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            //request.setAttribute("errorString", null);
            // request.setAttribute("productList", products);
            //RequestDispatcher dispatcher = request.getSession().getServletContext()
//              .getRequestDispatcher("/productList.jsp");
            //          dispatcher.forward(request, response);

            JSONObject jsonToReturn1 = new JSONObject();
            jsonToReturn1.put("answer", "ok");
            out.println(jsonToReturn1.toString());
            System.out.println(jsonToReturn1.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        database.close();
    }

}