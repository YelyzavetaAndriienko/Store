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
    public static final String databaseFile = "data.db";
    public static final String groupsTable = "groups";
    public static final String productsTable = "products";

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            //con = DriverManager.getConnection("jdbc:sqlite::memory:");
            this.con = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            PreparedStatement groupST = con.prepareStatement(
                    "create table if not exists " + groupsTable +
                            " ('groupId' INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " 'groupName' text," +
                            " 'groupDescription' text);");

            PreparedStatement productST = con.prepareStatement(
                    "create table if not exists " + productsTable +
                            "('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    // check whether the group name is unique
    public static boolean groupNameIsUnique(final String name) {
        try {
            final Statement statement = con.createStatement();
            final ResultSet resultSet = statement.executeQuery(
                    String.format("select count(*) as num_of_groups from " + groupsTable + " where groupName = '%s'", name)
            );
            resultSet.next();
            return resultSet.getInt("num_of_groups") == 0;
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
            return resultSet.getInt("num_of_products") == 0;
        } catch (SQLException e) {
            throw new RuntimeException("Can't find product", e);
        }
    }

    public static Group createGroup(Group group) {
        if (group.getName().equals(""))
            throw new NullPointerException("Enter correct name of the group");
        if (groupNameIsUnique(group.getName())) {
            try (PreparedStatement statement = con.prepareStatement("INSERT INTO " + groupsTable + " (groupName, groupDescription) VALUES (?, ?)")) {
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
        Group readGroup = readGroup(product.getGId());
        if (readGroup == null) {
            throw new NullPointerException("There is no group with such id!");
        }
        if (productNameIsUnique(product.getName())) {
            try (PreparedStatement statement = con.prepareStatement("INSERT INTO " + productsTable + "(gId, name, description, manufacturer, amount, price) VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setInt(1, product.getGId());
                statement.setString(2, product.getName());
                statement.setString(3, product.getDescription());
                statement.setString(4, product.getManufacturer());
                statement.setInt(5, product.getAmount());
                statement.setDouble(6, product.getPrice());
                statement.executeUpdate();
                ResultSet resultSet = statement.getGeneratedKeys();
                product.setId(resultSet.getInt("last_insert_rowid()"));
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

    // return group object
    // if there is no group with such id - return null
    public static Group readGroup(int id) {
        try {
            final PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + groupsTable + " where groupId = ?");
            selectStatement.setInt(1, id);
            selectStatement.execute();
            final ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                return resultSetToGroup(resultSet);
            } else return null;
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException("Can't get group", e);
        }
    }

    // return product object
    // if there is no product with such id - return null
    public static Product readProduct(int id) {
        try {
            final PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + productsTable + " where id = ?");
            selectStatement.setInt(1, id);
            selectStatement.execute();
            final ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                return resultSetToProduct(resultSet);
            } else return null;
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException("Can't get product", e);
        }
    }

    public static List<Group> readGroups() {
        try (Statement st = con.createStatement();
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + groupsTable)) {
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
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + productsTable)) {
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
        if (name.equals("")){
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
            } else {
                System.out.println("Name for group is not unique!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update group", e);
        }
    }

    public static void updateProduct(int id, int groupId, String name,
                                     String description, String manufacturer, int amount,
                                     double price) {
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
            } else {
                System.out.println("Name for product is not unique!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't update product", e);
        }
    }

    public static void deleteAllProductsInGroup(final int groupId) {
        try {
            final Statement statement = con.createStatement();
            String query = String.format("delete from " + productsTable + " where gId = '%s'", groupId);
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete products", e);
        }
    }

    public static void deleteGroup(int id) {
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + groupsTable + " WHERE groupId = ?")) {
            st.setInt(1, id);
            int isDeleted = st.executeUpdate();
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
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + productsTable + " WHERE id = ?")) {
            st.setInt(1, id);
            int isDeleted = st.executeUpdate();
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
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + groupsTable)) {
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
        try (PreparedStatement st = con.prepareStatement("DELETE FROM " + productsTable)) {
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
        String sql = filters.isEmpty() ? "SELECT * FROM " + productsTable :
                "SELECT * FROM " + productsTable + " where " + where;

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

    public List<Product> getAllProductsFromGroup(int groupId){
        List<Product> productsFromGroup = new ArrayList<>();
        try {
            PreparedStatement selectStatement = con.prepareStatement(
                    "select * from " + productsTable + " where group_id = ?");
            selectStatement.setInt(1, groupId);
            selectStatement.execute();
            final ResultSet resultSet = selectStatement.executeQuery();
            while (resultSet.next()) {
                productsFromGroup.add(resultSetToProduct(resultSet));
            }
            return productsFromGroup;
        } catch (SQLException e) {
            throw new RuntimeException("Can't get products", e);
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
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + groupsTable);) {
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
             ResultSet resultSet = st.executeQuery("SELECT * FROM " + productsTable);) {
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

    public void dropProductsTable() {
        try {
            final Statement statement = con.createStatement();
            String query = "drop table if exists " + productsTable;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Can't drop products table", e);
        }
    }

    public void dropGroupsTable() {
        try {
            final Statement statement = con.createStatement();
            String query = "drop table if exists " + groupsTable;
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException("Can't drop groups table", e);
        }
    }

    public void close() {
        try {
            con.close();
            System.out.println("Connection closed");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Database database = new Database();

        Group group1 = new Group(1, "fruits", "smth1group");
        Group group2 = new Group(2, "vegetables", "smth2group");
        Group group3 = new Group(3, "clothes", "smth3group");


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

        database.deleteGroup(group1.getId());
        System.out.println(database.readProducts());
    }

}
