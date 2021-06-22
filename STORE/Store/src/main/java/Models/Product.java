package Models;

import java.util.Objects;

public class Product {

    private Integer id;
    private Integer gId;
    private String name;
    private String description;
    private String manufacturer;
    private int amount;
    private double price;

    public Product(Integer id, Integer gId, String name, String description, String manufacturer, int amount, double price) {
        this.id = id;
        this.gId = gId;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.amount = amount;
        this.price = price;
    }

    public Product(Integer gId, String name, String description, String manufacturer, int amount, double price) {
        this.id = null;
        this.gId = gId;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return amount == product.amount &&
                Double.compare(product.price, price) == 0 &&
                Objects.equals(id, product.id) &&
                Objects.equals(gId, product.gId) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(manufacturer, product.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, manufacturer, amount, price);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGId() { return gId; }

    public void setGId(Integer gId) { this.gId = gId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", gId=" + gId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }
}
