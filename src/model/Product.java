package model;
/**
 * A class modelled after the Product table of the database, with getters, setters, constructors and converters from numbers to string
 */
public class Product {
    private int idProduct;
    private String Name;
    private double Price;
    private int Quantity;


    public Product() {

    }

    public Product(int idProduct, String name, double price, int quantity) {
        this.idProduct = idProduct;
        Name = name;
        Price = price;
        Quantity = quantity;
    }

    public Product(String name, double price, int quantity) {
        super();
        Name = name;
        Price = price;
        Quantity = quantity;
    }

    public Product(String name) {
        super();
        Name = name;
    }

    public String convertID() {
        String s = String.valueOf(this.idProduct);
        return s;
    }

    public String convertPrice() {
        String s = String.valueOf(this.Price);
        return s;
    }

    public String convertQuantity() {
        String s = String.valueOf(this.Quantity);
        return s;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

}
