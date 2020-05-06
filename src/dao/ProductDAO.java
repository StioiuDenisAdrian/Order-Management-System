package dao;


import model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to access the instructions and model them for the product operations
 */
public class ProductDAO extends AbstractDAO<Product> {
    /**
     * deletes a product from the table
     * @param p the deleted client
     * @return the deleted product
     */
    public Product deleteProduct(Product p) {
        deleteAfterName(p);
        return p;
    }
    /**
     * finds all the products
     * @return the products
     */
    public List<Product> findAllProducts() {
        List<Product> products;
        products = findAll();
        return products;
    }
    /**
     * inserts a product in the table, if it is already there we will update the quantity
     * @param p the inserted product
     * @return the inserted product
     */
    public Product insertProduct(Product p)  {
        if (duplicates(p)) {
            insert(p);
        } else {
            try {
                update(p);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return p;
    }

    /**
     * updates a product
     * @param p the product to be update
     * @return the update product
     */
    public Product updateProduct(Product p){
        try {
            update(p);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }

    /**
     * finds a product whose only name is known
     * @param Name the name of the searched product
     * @return the found product
     */
    public Product findProductByName(String Name) {
        Product p = new Product();
        p=findByName(Name);
        return p;
    }

}
