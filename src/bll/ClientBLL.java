package bll;

import bll.validators.NameValidator;
import bll.validators.Validator;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ClientDAO;
import model.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Performs the instructions for the client, and validates its fields
 */
public class ClientBLL {
    private ClientDAO clientDAO;
    private NameValidator validator;


    public ClientBLL() {
        validator = new NameValidator();
        clientDAO = new ClientDAO();
    }

    /**
     * Find a client using only the id.
     *
     * @param id the id on which we will find the client
     * @return the found client
     */
    public Client findClientByID(int id) {
        Client c = new Client();
        try {
            c = clientDAO.findClientById(id);
            System.out.println(c.toString());
        } catch (NullPointerException e) {
            System.out.println("The client with id=" + id + "was not found!");
        }
        return c;
    }

    /**
     * used for finding the information of a client whose name is known
     * @param name name of the client we will search
     * @return the found client
     */

    public Client findClientByName(String name) {
        Client c = new Client();
        try {
            c = clientDAO.findClientByName(name);
        } catch (NullPointerException e) {
            System.out.println("The client with Name=" + name + "was not found!");
        }
        return c;
    }

    /**
     *find all the clients inside the table and generate a pdf report in tabular form
     * @return all the clients
     * @throws FileNotFoundException if the path is incorrect
     * @throws DocumentException if the insertion in the document has problems
     */

    public List<Client> findAllClients() throws FileNotFoundException, DocumentException {
        List<Client> clients = new ArrayList<>();
        clients = clientDAO.findAll();
        if (clients.isEmpty()) {
            throw new NoSuchElementException("There are no clients!");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data = dtf.format(now);
        StringBuilder path = new StringBuilder();
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Reports\\ClientReport_");
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));

        document.open();

        PdfPTable table = new PdfPTable(3);
        addTableHeaderClients(table);
        addClientRows(table, clients);


        document.add(table);
        document.close();
        return clients;
    }

    /**
     * insert a client into the database
     * @param c the data of the client which will be inserted
     * @return the client data which was inserted, except the client's id, since it is generated automated by the database
     */

    public Client insertClient(Client c) {
        validator.validate(c);
        clientDAO.insertClient(c);
        return c;
    }

    /**
     * deletes a client from the database
     * @param c the data of the client which will deleted
     * @return the client data which was deleted
     */
    public Client deleteClient(Client c) {
        validator.validate(c);
        clientDAO.deleteClient(c);
        return c;
    }

    /**
     * deletes a client which has only the name known
     * @param c the data of the client which will be deleted, only the name field is mandatory to not be empty
     * @return the client data which was deleted
     */
    public Client deleteClientAfterName(Client c) {
        validator.validate(c);
        clientDAO.deleteClientAfterName(c);
        return c;
    }

    /**
     * creates a header for the table
     *
     * @param table the table where the headers will be inserted
     */
    private static void addTableHeaderClients(PdfPTable table) {
        Stream.of("ID", "Name", "Address").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }

    /**
     * adds rows to the table, a row for each client
     * @param table the table where the rows will be inserted
     * @param clients the clients whose data will be inserted in the table
     */
    private static void addClientRows(PdfPTable table, List<Client> clients) {
        for (Client c : clients) {
            table.addCell(c.convertID());
            table.addCell(c.getName());
            table.addCell(c.getAddress());
        }
    }

}
