package Catalog.Struct;

public class GeneralStruct {
    public String name;
    public Double price;
    public String stock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public GeneralStruct(String name, Double price, String stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
