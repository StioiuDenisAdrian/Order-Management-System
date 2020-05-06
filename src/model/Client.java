package model;

import com.itextpdf.text.Image;

/**
 * A class modelled after the Client table of the database, with getters, setters, constructors and converters from numbers to string
 */
public class Client {
    private int idClient;
    private String Name;
    private String Address;

    public Client() {

    }

    public Client(int idClient, String name, String address) {
        this.idClient = idClient;
        Name = name;
        Address = address;
    }

    public Client(String name, String address) {
        super();
        Name = name;
        Address = address;
    }

    public Client(String name){
        super();
        Name=name;
    }

   public String convertID(){
        String s=String.valueOf(this.idClient);
        return s;
   }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



}
