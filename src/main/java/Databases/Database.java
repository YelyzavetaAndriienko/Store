package Databases;

import Models.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Connection con;
    public static final String databaseFile = "C:\\temp\\db\\storedata.db";
    public static final String groupsTable = "groups";
    public static final String productsTable = "products";

    public Database() {
        Conn();
        initTables();
    }

    public static void Conn() {
        System.out.println("get connection");
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            con.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find the JDBC driver");
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void initTables() {
        System.out.println("init tables");
        try {
            final Statement groupST = con.createStatement();
            String query = "create table if not exists " + groupsTable +
                    " ('groupId' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " 'groupName' text," +
                    " 'groupDescription' text);";
            groupST.execute(query);
            final Statement productST = con.createStatement();
            query = "create table if not exists " + productsTable +
                    "('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "'gId' INTEGER, " +
                    "'name' text, " +
                    "'description' text, " +
                    "'manufacturer' text, " +
                    "'amount' INTEGER, " +
                    "'price' double);";
            productST.execute(query);
            groupST.close();
            productST.close();

            con.setAutoCommit(false);
            System.out.println("set false to commit");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("fail");
        }
    }

    // check whether the group name is unique
    public static boolean groupNameIsUnique(final String name) {
        try {
            final Statement statement = con.createStatement();
            final ResultSet resultSet = statement.executeQuery(
                    String.format("select count(*) as num_of_groups from " + groupsTable + " where groupName = '%s'", name)
            );
            resultSet.next();
            boolean nameUnique = (resultSet.getInt("num_of_groups") == 0);
            statement.close();
            resultSet.close();
            return nameUnique;
        } catch (SQLException e) {
            throw new RuntimeException("Can't find group", e);
        }
    }

    // check whether the product name is unique
    public static boolean productNameIsUnique(final String name) {
        try {
            final Statement statement = con.createStatement();
            final ResultSet resultSet = statement.executeQuery(
                    String.format("select count(*) as num_of_products from " + productsTable + " where name = '%s'", name)
            );
            resultSet.next();
            boolean nameUnique = (resultSet.getInt("num_of_products") == 0);
            statement.close();
            resultSet.close();
            return nameUnique;
        } catch (SQLException e) {
            throw new RuntimeException("Can't find product", e);
        }
    }

    public static Group createGroup(Group group) {
        if (group.getName().equals(""))
            throw new NullPointerException("Enter correct name of the group");
        if (groupNameIsUnique(group.getName())) {
            try (PreparedStatement statement = con.prepareStatement("INSERT INTO " + groupsTable + " ('groupName', 'groupDescription') VALUES (?, ?)")) {
                statement.setString(1, group.getName());
                statement.setString(2, group.getDescription());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                group.setId(resultSet.getInt("last_insert_rowid()"));
                con.commit();
                statement.close();
                resultSet.close();
                return group;
            } catch (SQLException e) {
                System.out.println("Can't insert group");
                e.printStackTrace();
                throw new RuntimeException("Can't insert group", e);
            }
        } else {
            System.out.println("Name is not unique for group!");
            return null;
        }
    }

    public static Product createProduct(Product product) {
        if (product.getName().equals(""))
            throw new NullPointerException("Enter correct name of the product");
        if (product.getAmount() < 0)
            throw new NullPointerException("Enter correct amount of the product");
        if (product.getPrice() < 0)
            throw new NullPointerException("Enter correct price of the product");

        System.out.println("Passed group ID" + product.getGId());
        Group readGroup = readGroup(product.getGId());
        if (readGroup == null) {
            throw new NullPointerException("There is no group with such id!");
        }
        if (productNameIsUnique(product.getName())) {
            try (PreparedStatement statement = con.prepareStatement("INSERT INTO " + productsTable + "('gId', 'name', 'description', 'manufacturer', 'amount', 'price') VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setInt(1, product.getGId());
                statement.setString(2, product.getName());
                statement.setString(3, product.getDescription());
                statement.setString(4, product.getManufacturer());
                statement.setInt(5, product.getAmount());
                statement.setDouble(6, product.getPrice());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                product.setId(resultSet.getInt("last_insert_rowid()"));
                con.commit();
                statement.close();
                resultSet.close();
                return product;
            } catch (SQLException e) {
                System.out.println("Can't insert product");
                e.printStackTrace();
                throw new RuntimeException("Can't insert product", e);
            }
        } else {
            System.out.println("Name is not unique for product!");
            return null;
        }
    }

    public static User readUserByLogin(String login) {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM users where login = '" + login + "'");) {
            if (resultSet.next()) {
                User user = new User(resultSet.getInt("userId"), resultSet.getString("login"), resultSet.getString("password"));
                st.close();
                resultSet.close();
                return user;
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select user", e);
        }
    }

    // return group object
    // if there is no group with such id - return null
    public static Group readGroup(int id) {
        //Conn();
        try {
            final PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + groupsTable + " where groupId = ?");
            selectStatement.setInt(1, id);
            System.out.println("selectStatement " + selectStatement);
            //selectStatement.execute();

            final ResultSet resultSet = selectStatement.executeQuery();
            System.out.println("selectStatement executed");

            System.out.println("Step 130 - read all groups from database");
            if (resultSet.next()) {
                Group group = resultSetToGroup(resultSet);
                resultSet.close();
                selectStatement.close();;
                return group;
            } else return null;
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException("Can't get group", e);
        }
    }

    // return product object
    // if there is no product with such id - return null
    public static Product readProduct(int id) {
        //Conn();
        try {
            final PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + productsTable + " where id = ?");
            selectStatement.setInt(1, id);
            //selectStatement.execute();
            final ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                Product product = resultSetToProduct(resultSet);
                resultSet.close();
                selectStatement.close();
                return product;
            } else return null;
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException("Can't get product", e);
        }
    }

    public static List<Group> readGroups() {
        //Conn();
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + groupsTable)) {
            System.out.println("Try: Select all groups from database");
            List<Group> groupsList = new ArrayList<>();
            while (resultSet.next()) {

                Group g = resultSetToGroup(resultSet);
                groupsList.add(g);
                System.out.println("Next group :" + g);

            }
            st.close();
            System.out.println("Try: all groups from database Selected");
            resultSet.close();

            return groupsList;

        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static List<Product> readProducts() {
        //Conn();
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + productsTable)) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            st.close();
            resultSet.close();
            return products;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public static void updateGroup(int id, String name, String description) {
        //Conn();
        if (name.equals("")) {
            throw new NullPointerException("Enter correct name of the group");
        }
        Group readGroup = readGroup(id);
        if (readGroup == null) {
            throw new NullPointerException("There is no group with such id!");
        }
        String sql = "UPDATE " + groupsTable + " SET groupName = ? , "
                + "groupDescription = ? "
                + "WHERE groupId = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, name);
            st.setString(2, description);
            st.setInt(3, id);
            if (groupNameIsUnique(name) || readGroup.getName().equals(name)) {
                st.executeUpdate();
                con.commit();
                st.close();
            } else {
                System.out.println("Name for group is not unique!");
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update group", e);
        }
    }

    public static void updateProduct(int id, int groupId, String name,
                                     String description, String manufacturer, int amount,
                                     double price) {
        //Conn();
        if (name.equals(""))
            throw new NullPointerException("Enter correct name of the product");
        if (amount < 0)
            throw new NullPointerException("Enter correct amount of the product");
        if (price < 0)
            throw new NullPointerException("Enter correct price of the product");
        Product readProduct = readProduct(id);
        if (readProduct == null) {
            throw new NullPointerException("There is no product with such id!");
        }
        Group readGroup = readGroup(groupId);
        if (readGroup == null) {
            throw new NullPointerException("There is no group with such id!");
        }
        String sql = "UPDATE " + productsTable + " SET gId = ?, "
                + "name = ? , "
                + "description = ? , "
                + "manufacturer = ? , "
                + "amount = ? , "
                + "price = ? "
                + "WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, groupId);
            st.setString(2, name);
            st.setString(3, description);
            st.setString(4, manufacturer);
            st.setInt(5, amount);
            st.setDouble(6, price);
            st.setInt(7, id);
            if (productNameIsUnique(name) || readProduct.getName().equals(name)) {
                st.executeUpdate();
                con.commit();
                st.close();
            } else {
                System.out.println("Name for product is not unique!");
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update product", e);
        }
    }

    public static void deleteAllProductsInGroup(final int groupId) {
        // Conn();
        try {
            final Statement statement = con.createStatement();
            String query = String.format("delete from " + productsTable + " where gId = '%s'", groupId);
            statement.execute(query);
            con.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete products", e);
        }
    }

    public static void deleteGroup(int id) {
        // Conn();
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + groupsTable + " WHERE groupId = ?")) {
            st.setInt(1, id);
            int isDeleted = st.executeUpdate();
            con.commit();
            st.close();
            if (isDeleted != 1) {
                System.out.println("Group doesn't exist with such id");
                //throw new NullPointerException("Group doesn't exist with such id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete group", e);
        }
        deleteAllProductsInGroup(id);
    }

    public static void deleteProduct(int id) {
        // Conn();
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + productsTable + " WHERE id = ?")) {
            st.setInt(1, id);
            int isDeleted = st.executeUpdate();
            con.commit();
            st.close();
            if (isDeleted != 1) {
                System.out.println("Product doesn't exist with such id");
                //throw new NullPointerException("Product doesn't exist with such id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete product", e);
        }
    }

    public static void deleteGroups() {
        // Conn();
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + groupsTable)) {
            st.executeUpdate();
            con.commit();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete groups", e);
        }
        try (PreparedStatement st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'groups';")) {
            st.executeUpdate();
            con.commit();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete groups", e);
        }
        deleteProducts();
    }

    public static void deleteProducts() {
        // Conn();
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + productsTable)) {
            st.executeUpdate();
            con.commit();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete products", e);
        }

        try (PreparedStatement st = con.prepareStatement("UPDATE 'sqlite_sequence' SET 'seq' = 0 WHERE name = 'product';")) {
            st.executeUpdate();
            con.commit();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't delete products", e);
        }
    }

    public static List<Group> listByCriteriaGroup(GroupFilter groupFilter) {
        // Conn();
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
            st.close();
            resultSet.close();
            return groups;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select groups", e);
        }
    }

    public static List<Product> listByCriteriaProduct(ProductFilter productFilter) {
        // Conn();
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
        String sql = filters.isEmpty() ? "SELECT * FROM " + productsTable :
                "SELECT * FROM " + productsTable + " where " + where;

        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery(sql);) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            st.close();
            resultSet.close();
            return products;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public static ArrayList<Product> getAllProductsFromGroup(int groupId) {
        // Conn();
        ArrayList<Product> productsFromGroup = new ArrayList<>();
        try {
            PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + productsTable + " where gId = ?");
            selectStatement.setInt(1, groupId);
            final ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                productsFromGroup.add(resultSetToProduct(resultSet));
            }
            selectStatement.close();
            resultSet.close();
            return productsFromGroup;
        } catch (SQLException e) {
            throw new RuntimeException("Can't get products", e);
        }
    }

    private static Group resultSetToGroup(ResultSet resultSet) throws SQLException {
        Group g = new Group(
                resultSet.getInt("groupId"),
                resultSet.getString("groupName"),
                resultSet.getString("groupDescription"));
        // resultSet.close();
        return g;
    }

    private static Product resultSetToProduct(ResultSet resultSet) throws SQLException {
        Product p = new Product(
                resultSet.getInt("id"),
                resultSet.getInt("gId"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("manufacturer"),
                resultSet.getInt("amount"),
                resultSet.getDouble("price"));
        // resultSet.close();
        return p;
    }

    public static boolean isEmptyGroup() {
        // Conn();
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + groupsTable);) {
            List<Group> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(resultSetToGroup(resultSet));
            }
            st.close();
            resultSet.close();
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
        // Conn();
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + productsTable);) {
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSetToProduct(resultSet));
            }
            st.close();
            resultSet.close();
            if (products.isEmpty())
                return true;
            else
                return false;
        } catch (SQLException e) {
            System.out.println("Invalid SQL request");
            throw new RuntimeException("Can't select products", e);
        }
    }

    public void dropProductsTable() {
        // Conn();
        try {
            final Statement statement = con.createStatement();
            String query = "drop table if exists " + productsTable;
            statement.executeUpdate(query);
            con.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Can't drop products table", e);
        }
    }

    public void dropGroupsTable() {
        // Conn();
        try {
            final Statement statement = con.createStatement();
            String query = "drop table if exists " + groupsTable;
            statement.executeUpdate(query);
            con.commit();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Can't drop groups table", e);
        }
    }

    public static void close() {
        try {
            con.close();
            System.out.println("Connection closed");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Database database = new Database();

        Group group1 = new Group(1, "fruits", "fruits");
        Group group2 = new Group(2, "vegetables", "smth2group");
        Group group3 = new Group(3, "clothes", "smth3group");


        //database.createGroup(group1);
        // database.createGroup(group2);
        // database.createGroup(group3);

/*
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
        database.createProduct(product3);*/
/*
        System.out.println(database.readProducts());
        database.updateProduct(product3.getId(), 4, "appleUPD", "smth1UPD", "UkraineUPD", 100, 0.1);

        ProductFilter filter = new ProductFilter("prod", null, null, null, null, null, null);
        System.out.println(database.listByCriteriaProduct(filter));

        System.out.println(database.listByCriteriaProduct(new ProductFilter(null, null, null, null, null, null, null)));
        //database.deleteProduct(database.readGroup(group3.getId()).getName());
        //database.deleteGroups();

        System.out.println(database.readProducts());
        System.out.println("------------------------------------");

        database.deleteGroup(group1.getId());
        System.out.println(database.readProducts());*/
    }

}
