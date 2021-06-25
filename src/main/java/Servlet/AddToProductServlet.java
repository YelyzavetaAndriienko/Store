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

@WebServlet("/addToProduct")
public class AddToProductServlet extends HttpServlet {
    //private Database database;
    //private static Database database = new Database();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("There is doGet addToProduct");

        response.setContentType("text/html");
        // List<Product> products = Database.readProducts();
        // request.setAttribute("products", products);
        RequestDispatcher dispatcher = request.getRequestDispatcher("addToProduct.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("There is doPost - add to product");
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
            int amountToAdd = jsonObject.getInt("amountToAdd");

            List<Product> products = database.readProducts();
            Product p = null;
            for (int i = 0; i < products.size(); i++) {
                if (name.equals(products.get(i).getName()))
                    p = products.get(i);
            }
            if(p!=null){
            database.updateProduct(p.getId(), p.getGId(), p.getName(), p.getDescription(), p.getManufacturer(), (p.getAmount()+amountToAdd), p.getPrice());
            System.out.println(p.toString());
            JSONObject jsonToReturn1 = new JSONObject();
            jsonToReturn1.put("answer", "ok");
            out.println(jsonToReturn1.toString());
            System.out.println(jsonToReturn1.toString());

            System.out.println(database.readProducts());

            String text = jsonToReturn1.toString() + "\n  ------- \n" + p.toString();

            try (FileOutputStream fos = new FileOutputStream("C:\\Users\\Liza\\Documents\\notes.txt")) {
                byte[] buffer = text.getBytes();

                fos.write(buffer, 0, buffer.length);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            } else {
                System.out.println("There is no product with such name");
            }
    } catch(Exception e) {
        System.out.println(e.toString());
    }
        database.close();
}
}