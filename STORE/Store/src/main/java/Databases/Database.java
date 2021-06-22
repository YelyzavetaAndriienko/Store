package Databases;

import Models.Group;
import Models.GroupFilter;
import Models.Product;
import Models.ProductFilter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Connection con;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite::memory:");

            PreparedStatement groupST = con.prepareStatement(
                    "create table if not exists 'groups' (" +
                            "'groupId' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "'groupName' text, " +
                            "'groupDescription' text);");

            PreparedStatement productST = con.prepareStatement(
                    "create table if not exists 'product' (" +
                            "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "'gId' INTEGER, " +
                            "'name' text, " +
                            "'description' text, " +
                            "'manufacturer' text, " +
                            "'amount' INTEGER, " +
                            "'price' double);");

            groupST.executeUpdate();
            productST.executeUpdate();
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find the JDBC driver");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            e.printStackTrace();
        }
    }

    public static Group createGroup(Group group) {
        try (PreparedStatement statement = con.prepareStatement("INSERT INTO groups(groupName, groupDescription) VALUES (?, ?)")) {
            if (group.getName().equals(""))
                throw new NullPointerException("Enter correct name of the group");
            else
                statement.setString(1, group.getName());
            statement.setString(2, group.getDescription());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            group.setId(resultSet.getInt("last_insert_rowid()"));

            return group;
        } catch (SQLException e) {
            System.out.println("Can't insert group");
            e.printStackTrace();
            throw new RuntimeException("Can't insert group", e);
        }
    }

    public static Product createProduct(Product product) {
        try (PreparedStatement statement = con.prepareStatement("INSERT INTO product(gId, name, description, manufacturer, amount, price) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, product.getGId());
            if (product.getName().equals(""))
                throw new NullPointerException("Enter correct name of the product");
            else
                statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setString(4, product.getManufacturer());
            if (product.getAmount() >= 0)
                statement.setInt(5, product.getAmount());
            else
                throw new NullPointerException("Enter correct amount of the product");
            if (product.getPrice() >= 0)
                statement.setDouble(6, product.getPrice());
            else
                throw new NullPointerException("Enter correct price of the product");

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            product.setId(resultSet.getInt("last_insert_rowid()"));

            return product;
        } catch (SQLException e) {
            System.out.println("Can't insert product");
            e.printStackTrace();
            throw new RuntimeException("Can't insert product", e);
        }
    }

    public static Group readGroup(int id) {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM groups");) {
            List<Group> groups = new ArrayList<>();
            Group group = null;
            int i = 0;

            while (resultSet.next()) {
                groups.add(resultSetToGroup(resultSet));
                if (id == groups.get(i).getId())
                    group = groups.get(i);
                i++;
            }

            return group;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static Product readProduct(int id) {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM product");) {
            List<Product> products = new ArrayList<>();
            Product product = null;
            int i = 0;

            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
                if (id == products.get(i).getId())
                    product = products.get(i);
                i++;
            }

            return product;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public static List<Group> readGroups() {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM groups");) {
            List<Group> groupsList = new ArrayList<>();
            while (resultSet.next()) {
                groupsList.add(resultSetToGroup(resultSet));
            }
            return groupsList;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static List<Product> readProducts() {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM product");) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public static void updateGroup(int id, String name, String description) {
        String sql = "UPDATE groups SET groupName = ? , "
                + "groupDescription = ? "
                + "WHERE groupId = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            List<Group> groups = readGroups();
            Group group = null;
            boolean groupExist = false;

            for (int i = 0; i < groups.size(); i++) {
                if (id == groups.get(i).getId())
                    groupExist = true;
            }

            if (name.equals(""))
                throw new NullPointerException("Enter correct name of the group");
            else
                st.setString(1, name);
            st.setString(2, description);
            if (groupExist == true)
                st.setInt(3, id);
            else
                throw new NullPointerException("Enter correct id of the group");

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update group", e);
        }
    }

    public static void updateProduct(int id, int groupId, String name, String description, String manufacturer, int amount, double price) {
        String sql = "UPDATE product SET gId = ?, "
                + "name = ? , "
                + "description = ? , "
                + "manufacturer = ? , "
                + "amount = ? , "
                + "price = ? "
                + "WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            List<Product> products = readProducts();
            boolean productExist = false;

            for (int i = 0; i < products.size(); i++) {
                if (id == products.get(i).getId())
                    productExist = true;
            }

            List<Group> groups = readGroups();
            Group group = null;
            boolean groupExist = false;

            for (int i = 0; i < groups.size(); i++) {
                if (groupId == groups.get(i).getId())
                    groupExist = true;
            }

            if (groupExist == true)
                st.setInt(1, groupId);
            else
                st.setInt(1, readProduct(id).getGId());
            if (name.equals(""))
                throw new NullPointerException("Enter correct name of the product");
            else
                st.setString(2, name);
            st.setString(3, description);
            st.setString(4, manufacturer);
            if (amount >= 0)
                st.setInt(5, amount);
            else
                throw new NullPointerException("Enter correct amount of the product");
            if (amount >= 0)
                st.setDouble(6, price);
            else
                throw new NullPointerException("Enter correct price of the product");
            if (productExist == true)
                st.setInt(7, id);
            else
                throw new NullPointerException("Enter correct id of the product");

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update product", e);
        }
    }

    public static void deleteGroup(String name) {
        Group group = null;
        try (PreparedStatement st = con.prepareStatement("DELETE FROM groups WHERE groupName = ?")) {
            List<Group> groups = readGroups();
            boolean groupExist = false;

            for (int i = 0; i < groups.size(); i++) {
                if (name.equals(groups.get(i).getName())) {
                    group = groups.get(i);
                    groupExist = true;
                }
            }

            if (groupExist == false)
                throw new NullPointerException("Enter correct name of the group");
            else
                st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete group", e);
        }

        try (PreparedStatement st = con.prepareStatement("DELETE FROM product WHERE gId = ?")) {
            List<Product> products = readProducts();
            boolean productExist = false;

            st.setInt(1, group.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete product", e);
        }
    }

    public static void deleteProduct(String name) {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM product WHERE name = ?")) {
            List<Product> products = readProducts();
            boolean productExist = false;

            for (int i = 0; i < products.size(); i++) {
                if (name.equals(products.get(i).getName()))
                    productExist = true;
            }

            if (productExist == false)
                throw new NullPointerException("Enter correct name of the product");
            else
                st.setString(1, name);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete product", e);
        }
    }

    public static void deleteGroups() {
        try (PreparedStatement st = con.prepareStatement("delete from groups")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete groups", e);
        }

        try (PreparedStatement st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'groups';")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete groups", e);
        }
        deleteProducts();
    }

    public static void deleteProducts() {
        try (PreparedStatement st = con.prepareStatement("delete from product")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete products", e);
        }

        try (PreparedStatement st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'product';")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete products", e);
        }
    }

    public static List<Group> listByCriteriaGroup(GroupFilter groupFilter) {
        List<String> filters = new ArrayList<>();

        if (groupFilter.getName() != null)
            filters.add(" groupName like '%" + groupFilter.getName() + "%' ");

        if (groupFilter.getDescription() != null)
            filters.add(" groupDescription like '%" + groupFilter.getDescription() + "%' ");

        String where = String.join(" and ", filters);
        String sql = filters.isEmpty() ? "SELECT * FROM groups" :
                "SELECT * FROM groups where " + where;

        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery(sql);) {
            List<Group> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(resultSetToGroup(resultSet));
            }
            return groups;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static List<Product> listByCriteriaProduct(ProductFilter productFilter) {
        List<String> filters = new ArrayList<>();

        if (productFilter.getName() != null)
            filters.add(" name like '%" + productFilter.getName() + "%' ");

        if (productFilter.getDescription() != null)
            filters.add(" description like '%" + productFilter.getDescription() + "%' ");

        if (productFilter.getManufacturer() != null)
            filters.add(" manufacturer like '%" + productFilter.getManufacturer() + "%' ");

        if (productFilter.getAmountFrom() != null)
            filters.add(" amount >=" + productFilter.getAmountFrom() + "");

        if (productFilter.getAmountTo() != null)
            filters.add(" amount <=" + productFilter.getAmountTo() + " ");

        if (productFilter.getPriceFrom() != null)
            filters.add(" price >=" + productFilter.getPriceFrom() + "");

        if (productFilter.getPriceTo() != null)
            filters.add(" price <=" + productFilter.getPriceTo() + " ");

        String where = String.join(" and ", filters);
        String sql = filters.isEmpty() ? "SELECT * FROM product" :
                "SELECT * FROM product where " + where;

        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery(sql);) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            return products;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    private static Group resultSetToGroup(ResultSet resultSet) throws SQLException {
        return new Group(
                resultSet.getInt("groupId"),
                resultSet.getString("groupName"),
                resultSet.getString("groupDescription"));
    }

    private static Product resultSetToProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getInt("gId"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("manufacturer"),
                resultSet.getInt("amount"),
                resultSet.getDouble("price"));
    }

    public static boolean isEmptyGroup() {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM groups");) {
            List<Group> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(resultSetToGroup(resultSet));
            }
            if (groups.isEmpty())
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static boolean isEmptyProduct() {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM product");) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            if (products.isEmpty())
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public static void main(String[] args) {
        Database database = new Database();

        Group group1 = new Group("fruits", "smth1group");
        Group group2 = new Group("vegetables", "smth2group");
        Group group3 = new Group("clothes", "smth3group");


        database.createGroup(group1);
        database.createGroup(group2);
        database.createGroup(group3);

        database.updateGroup(group3.getId(), "shoes", "12345");

        GroupFilter filterG = new GroupFilter(null, "smth");
        System.out.println(database.listByCriteriaGroup(filterG));

        System.out.println(database.listByCriteriaGroup(new GroupFilter(null, null)));

        //database.deleteGroup(database.readGroup(group3.getId()).getName());
        //database.deleteGroups();

        //System.out.println(database.readGroups());

        Product product1 = new Product(group1.getId(), "prod1", "smth", "Ukr", 5, 23.30);
        Product product2 = new Product(group2.getId(), "prod2", "smth2", "Ekvador", 2, 33.0);
        Product product3 = new Product(group1.getId(), "other", "smthothe", "ua", 10, 104.21);

        database.createProduct(product1);
        database.createProduct(product2);
        database.createProduct(product3);

        System.out.println(database.readProducts());
        database.updateProduct(product3.getId(), 4, "appleUPD", "smth1UPD", "UkraineUPD", 100, 0.1);

        ProductFilter filter = new ProductFilter("prod", null, null, null, null, null, null);
        System.out.println(database.listByCriteriaProduct(filter));

        System.out.println(database.listByCriteriaProduct(new ProductFilter(null, null, null, null, null, null, null)));
        //database.deleteProduct(database.readGroup(group3.getId()).getName());
        //database.deleteGroups();

        System.out.println(database.readProducts());
        System.out.println("------------------------------------");

        database.deleteGroup(database.readGroup(group1.getId()).getName());
        System.out.println(database.readProducts());
    }

}