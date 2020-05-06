package model;
/**
 * A class modelled after the OrderItem table of the database, with getters, setters, constructors and converters from numbers to string
 */
public class OrderItem {
    private int idorderItem;
    private int idProduct;
    private int quantity;

    public OrderItem(int idorderItem, int idProduct, int quantity) {
        this.idorderItem = idorderItem;
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public OrderItem(int idProduct, int quantity){
        super();
        this.idProduct = idProduct;
        this.quantity = quantity;
    }

    public OrderItem(){

    }

    public String convertIdOrderItem(){
        String s= String.valueOf(this.idorderItem);
        return s;
    }

    public String convertIdProduct(){
        String s= String.valueOf(this.idProduct);
        return s;
    }

    public String convertQuantity(){
        String s= String.valueOf(this.quantity);
        return s;
    }

    public int getIdorderItem() {
        return idorderItem;
    }

    public void setIdorderItem(int idorderItem) {
        this.idorderItem = idorderItem;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
