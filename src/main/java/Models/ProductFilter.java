package Models;

public class ProductFilter {

    private String name;
    private String description;
    private String manufacturer;
    private Double priceFrom;
    private Double priceTo;
    private Integer amountFrom;
    private Integer amountTo;

    public ProductFilter(String name, String description, String manufacturer, Double priceFrom, Double priceTo, Integer amountFrom, Integer amountTo) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

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

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Integer amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Integer getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Integer amountTo) {
        this.amountTo = amountTo;
    }

    @Override
    public String toString() {
        return "ProductFilter{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                ", amountFrom=" + amountFrom +
                ", amountTo=" + amountTo +
                '}';
    }
}
