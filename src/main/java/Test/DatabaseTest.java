package Test;

import Databases.Database;
import Models.Group;
import Models.GroupFilter;
import Models.Product;
import Models.ProductFilter;
import org.junit.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseTest {

    private static Database database;
    private static Group group1 = new Group("fruits", "group");
    private static Group group2 = new Group("vegetables", "group");

    private static GroupFilter g1 = new GroupFilter(group1.getName(), null);
    private static GroupFilter g2 = new GroupFilter(null, group2.getDescription());
    private static GroupFilter g3 = new GroupFilter(null, null);

    private static Product product1 = new Product(1, "apple", "smth", "Ukraine", 5, 23.30);
    private static Product product2 = new Product(1, "banana", "smth", "Ecuador", 2, 33);
    private static Product product3 = new Product(2, "orange", "smth", "USA", 10, 104.21);

    private static ProductFilter p1 = new ProductFilter(product1.getName(), null, null, null, null, null, null);
    private static ProductFilter p2 = new ProductFilter(null, product2.getDescription(), null, null, null, null, null);
    private static ProductFilter p3 = new ProductFilter(null, null, product3.getManufacturer(), null, null, null, null);
    private static ProductFilter p4 = new ProductFilter(null, null, null, product1.getPrice(), null, null, null);
    private static ProductFilter p5 = new ProductFilter(null, null, null, null, product2.getPrice(), null, null);
    private static ProductFilter p6 = new ProductFilter(null, null, null, null, null, product3.getAmount(), null);
    private static ProductFilter p7 = new ProductFilter(null, null, null, null, null, null, product1.getAmount());
    private static ProductFilter p8 = new ProductFilter(null, null, null, null, null, null, null);

    @Before
    public void create() {
        database = new Database();

        database.createGroup(group1);
        database.createGroup(group2);

        try {
            System.out.println(group1.toString());
        }catch (NullPointerException e){
            System.out.println("NULL");
        }

        try {
        System.out.println(group1.getId());
        }catch (NullPointerException e){
            System.out.println("NULL");
        }

            try {
        System.out.println(database.readGroup(1).getId());
            }catch (NullPointerException e){
                System.out.println("NULL");
            }

                try {
        System.out.println(database.readGroup(group1.getId()).getId());
                }catch (NullPointerException e){
                    System.out.println("NULL");
                }

        product1.setGId(database.readGroup(group1.getId()).getId());
        product2.setGId(database.readGroup(group1.getId()).getId());
        product3.setGId(database.readGroup(group2.getId()).getId());

        database.createProduct(product1);
        database.createProduct(product2);
        database.createProduct(product3);
    }

    @After
    public void delete() {
        database.deleteProducts();
        database.deleteGroups();
    }

    @Test
    public void createGroupTest() {
        assertThat(database.readGroups()).containsExactly(group1, group2);

        Assert.assertTrue(database.readGroups().contains(group1));
        Assert.assertTrue(database.readGroups().contains(group2));
    }

    @Test
    public void readGroupsTest() {
        List<Group> listGroups = new ArrayList<>();
        listGroups.add(group1);
        listGroups.add(group2);

        List<Group> dataBaseGroups = database.readGroups();

        Assert.assertEquals(dataBaseGroups, listGroups);
    }

    @Test
    public void updateGroupTest() {
        Group group = new Group(group1.getId(), "newName", "newDes");
        database.updateGroup(group1.getId(), group.getName(), group.getDescription());

        Assert.assertEquals(database.readGroup(group1.getId()), group);
    }

    @Test
    public void deleteGroupTest() {
        database.deleteGroup(group1.getId());

        Assert.assertFalse(database.readGroups().contains(group1));
    }

    @Test
    public void deleteGroupsTest() {
        database.deleteGroups();

        Assert.assertTrue(database.isEmptyProduct());
    }

    @Test
    public void listByCriteriaGroupTest() {
        List<Group> dataBaseGroups = database.listByCriteriaGroup(g1);
        List<Group> groups = Arrays.asList(group1);
        assertThat(dataBaseGroups).containsExactlyInAnyOrderElementsOf(groups);

        dataBaseGroups = database.listByCriteriaGroup(g2);
        groups = Arrays.asList(group1, group2);
        assertThat(dataBaseGroups).containsExactlyInAnyOrderElementsOf(groups);

        dataBaseGroups = database.listByCriteriaGroup(g3);
        groups = Arrays.asList(group1, group2);
        assertThat(dataBaseGroups).containsExactlyInAnyOrderElementsOf(groups);
    }

    @Test
    public void createProductTest() {
        assertThat(database.readProducts()).containsExactly(product1, product2, product3);

        Assert.assertTrue(database.readProducts().contains(product1));
        Assert.assertTrue(database.readProducts().contains(product2));
        Assert.assertTrue(database.readProducts().contains(product3));
    }

    @Test
    public void readProductsTest() {
        List<Product> listProducts = new ArrayList<>();
        listProducts.add(product1);
        listProducts.add(product2);
        listProducts.add(product3);

        List<Product> dataBaseProducts = database.readProducts();

        Assert.assertEquals(dataBaseProducts, listProducts);
    }

    @Test
    public void updateProductTest() {
        Product product = new Product(product1.getId(), group2.getId(), "appleUPD", "smth1UPD", "UkraineUPD", 100, 0.1);
        database.updateProduct(product1.getId(), product.getGId(), product.getName(), product.getDescription(), product.getManufacturer(), product.getAmount(), product.getPrice());

        Assert.assertEquals(database.readProduct(product1.getId()), product);
    }

    @Test
    public void deleteProductTest() {
        database.deleteProduct(product1.getId());

        Assert.assertFalse(database.readProducts().contains(product1));
    }

    @Test
    public void deleteProductsTest() {
        database.deleteProducts();

        Assert.assertTrue(database.isEmptyProduct());
    }

    @Test
    public void listByCriteriaProductTest() {
        List<Product> dataBaseProducts = database.listByCriteriaProduct(p1);
        List<Product> products = Arrays.asList(product1);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p2);
        products = Arrays.asList(product1, product2, product3);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p3);
        products = Arrays.asList(product3);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p4);
        products = Arrays.asList(product1, product2, product3);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p5);
        products = Arrays.asList(product1, product2);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p6);
        products = Arrays.asList(product3);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p7);
        products = Arrays.asList(product1, product2);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);

        dataBaseProducts = database.listByCriteriaProduct(p8);
        products = Arrays.asList(product1, product2, product3);
        assertThat(dataBaseProducts).containsExactlyInAnyOrderElementsOf(products);
    }

}