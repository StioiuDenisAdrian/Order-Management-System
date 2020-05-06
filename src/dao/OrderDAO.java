package dao;
import model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to access the instructions and model them for the order operations
 */
public class OrderDAO extends AbstractDAO<Order> {
    /**
     * inserts an order in the table
     * @param o the inserted order
     * @return the inserted order
     */
    public Order insertOrder(Order o) {
        insert(o);
        return o;
    }
    /**
     * finds all the orders in the table
     * @return the found orders
     */
    public List<Order> findAllOrders(){
        List<Order> orders;
        orders=findAll();
        return orders;
    }
    /**
     * gets the id of an order
     * @param o the order whose id we want to know
     * @return the found id
     */
    public int getID(Order o){
        int id=findID(o);
        return id;
    }
}
