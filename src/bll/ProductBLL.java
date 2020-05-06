package bll;

import bll.validators.NameValidator;
import bll.validators.ProductNumberValidator;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ProductDAO;
import model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Performs the instructions for the product, and validates its fields
 */
public class ProductBLL {
    private ProductDAO productDAO;
    private NameValidator nameValidator;
    private ProductNumberValidator productNumberValidator;

    public ProductBLL() {
        nameValidator = new NameValidator();
        productNumberValidator = new ProductNumberValidator();

        productDAO = new ProductDAO();
    }
    /**
     *find all the products inside the table and generate a pdf report in tabular form
     * @return all the products into a list
     * @throws FileNotFoundException if the path is incorrect
     * @throws DocumentException if the format is not correct
     */
    public List<Product> findAllProducts() throws FileNotFoundException, DocumentException {
        List<Product> products = new ArrayList<>();
        products=productDAO.findAll();
        if (products.isEmpty()) {
            throw new NoSuchElementException("There are no clients!");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data= dtf.format(now);
        StringBuilder path = new StringBuilder();
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Reports\\ProductReport_");
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));

        document.open();

        PdfPTable table = new PdfPTable(4);
        addTableHeaderClients(table);
        addClientRows(table,products);


        document.add(table);
        document.close();
        return products;
    }
    /**
     * deletes a product from the database
     * @param p the data of the product which will deleted
     * @return the product data which was deleted
     */
    public Product deleteProduct(Product p){
        nameValidator.validate(p);
        productDAO.deleteProduct(p);
        return p;
    }
    /**
     * insert a product into the database
     * @param p the data of the client which will be inserted
     * @return the product data which was inserted, except the product's id, since it is generated automated by the database
     */
    public Product insertProduct(Product p) {
        nameValidator.validate(p);
        productDAO.insertProduct(p);
        return p;
    }
    /**
     * update the quantity of products
     * @param p the product whose quantity will be updated
     * @return the updated product
     */
    public Product updateProduct(Product p){
        productDAO.updateProduct(p);
        return p;
    }
    /**
     * creates a header for the table
     * @param table the table where the headers will be inserted
     */
    private static void addTableHeaderClients(PdfPTable table) {
        Stream.of("ID", "Name", "Price","Quantity").forEach(columnTitle -> {
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
     * @param products the products whose data will be inserted in the table
     */
    private static void addClientRows(PdfPTable table,List<Product> products) {
        for(Product p:products) {
            table.addCell(p.convertID());
            table.addCell(p.getName());
            table.addCell(p.convertPrice());
            table.addCell(p.convertQuantity());
        }
    }
    /**
     * used for finding the information of a product whose name is known
     * @param name name of the product we will search
     * @return the found product
     */
    public Product findProductByName(String name) {
        Product p=new Product();
        try {
            p = productDAO.findProductByName(name);
        } catch (NullPointerException e) {
            System.out.println("The product with Name=" + name + "was not found!");
        }
        return p;
    }
}
