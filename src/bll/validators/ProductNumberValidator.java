package bll.validators;

import model.Product;

public class ProductNumberValidator implements Validator<Product> {



    @Override
    public void validate(Product product) {
        if (product.getPrice()<=0){
            throw new IllegalArgumentException("The price of a product cannot be negative or 0!");
        }
        if(product.getQuantity()<=0){
            throw new IllegalArgumentException("Invalid quantity!");
        }
    }
}
