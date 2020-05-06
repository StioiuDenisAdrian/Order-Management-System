package bll.validators;
import model.Client;

import java.util.regex.Pattern;

public class ClientAddressValidator implements Validator<Client> {
    private static final String CLIENT_NAME_PATTERN="[a-zA-Z- ]+";
    @Override
    public void validate(Client client) {
        Pattern pattern =Pattern.compile(CLIENT_NAME_PATTERN);
        if(!pattern.matcher(client.getName()).matches()){
            throw new IllegalArgumentException("Not a valid name!");
        }
    }
}

