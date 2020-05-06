package bll.validators;


import model.Client;
import model.Product;

import java.util.regex.Pattern;

public class NameValidator implements Validator<Client> {
    private static final String CLIENT_NAME_PATTERN="[a-zA-Z ]+";
    private static final String PRODUCT_NAME_PATTERN="[ a-zA-Z]+";
    @Override
    public void validate(Client client) {
        Pattern pattern =Pattern.compile(CLIENT_NAME_PATTERN);
        if(!pattern.matcher(client.getName()).matches()){
            throw new IllegalArgumentException("Not a valid name!");
        }
    }

    public void validate(Product product) {
        Pattern pattern =Pattern.compile(PRODUCT_NAME_PATTERN);
        if(!pattern.matcher(product.getName()).matches()){
            throw new IllegalArgumentException("Not a valid name!");
        }
    }
}
