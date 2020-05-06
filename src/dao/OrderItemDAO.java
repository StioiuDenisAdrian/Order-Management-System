package dao;

import model.OrderItem;

import java.util.List;

/**
 * Used to access the instructions and model them for the order operations
 */
public class OrderItemDAO extends AbstractDAO<OrderItem>{
    /**
     * inserts an orderItem in the table
     * @param oi the inserted order
     * @return the inserted orderItem
     */
    public OrderItem insertOrderItem(OrderItem oi)  {
        insert(oi);
        return oi;
    }
    /**
     * finds all the orderItems in the table
     * @return the found orderItems
     */
    public List<OrderItem> findAllOrderItem(){
        List<OrderItem> orderItems=findAll();
        return orderItems;
    }
    /**
     * finds the id of an orderItem
     * @param oi the orderItem whose id we want to find
     * @return the found oid
     */
    public int getID(OrderItem oi){
        int id;
        id=findID(oi);
        return id;
    }
}
