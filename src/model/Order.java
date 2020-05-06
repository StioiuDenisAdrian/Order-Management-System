package model;
/**
 * A class modelled after the Order table of the database, with getters, setters, constructors and converters from numbers to string
 */
public class Order {
    private int idOrder;
    private int idClient;
    private int orderItem;

    public Order(int idOrder, int idClient, int orderItem) {
        this.idOrder = idOrder;
        this.idClient = idClient;
        this.orderItem = orderItem;
    }

    public Order(int idClient, int orderItem) {
        super();
        this.idClient = idClient;
        this.orderItem = orderItem;
    }

    public Order() {

    }

    public String convertIdOrder(){
        String s= String.valueOf(this.idOrder);
        return s;
    }

    public String convertIdClient(){
        String s= String.valueOf(this.idClient);
        return s;
    }

    public String convertIdOrderItem(){
        String s= String.valueOf(this.orderItem);
        return s;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(int orderItem) {
        this.orderItem = orderItem;
    }
}
