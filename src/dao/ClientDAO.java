package dao;

import dao.AbstractDAO;
import model.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to access the instructions and model them for the client operations
 */
public class ClientDAO extends AbstractDAO<Client> {
    /**
     * finds a client after its id
     * @param id client's id
     * @return the client
     */
    public Client findClientById(int id) {
        Client c = new Client();
        c=findById(id);
        return c;
    }
    /**
     * finds a client after its name
     * @param Name client's name
     * @return the client
     */
    public Client findClientByName(String Name) {
        Client c = new Client();
        c=findByName(Name);
        return c;
    }
    /**
     * finds all the clients
     * @return the clients
     */
    public List<Client> findAllClients(){
        List<Client> clients=new ArrayList<>();
        clients=findAll();
        return clients;
    }
    /**
     * inserts a client in the table, if it is already there the insertion will not take place
     * @param c the inserted client
     * @return the inserted client
     */
    public Client insertClient(Client c){
        if(duplicates(c)){
            insert(c);
        }
        else{
            System.out.println("Duplicate client!");
        }
        return c;
    }
    /**
     * deletes a client from the table
     * @param c the deleted client
     * @return the deleted client
     */
    public Client deleteClient(Client c){
        delete(c);
        return c;
    }
    /**
     * deletes a client from the table who has only the name known by the user
     * @param c the deleted client
     * @return the deleted client
     */
    public Client deleteClientAfterName(Client c){
        deleteAfterName(c);
        return c;
    }
}
