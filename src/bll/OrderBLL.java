package bll;

import bll.validators.NameValidator;
import bll.validators.ProductNumberValidator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.OrderDAO;
import model.Order;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Manages the instructions for the order
 */
public class OrderBLL {
    private OrderDAO orderDAO = new OrderDAO();
    /**
     *creates a pdf when the quantity is insufficient
     * @param cn the name of the client
     * @param pn the name of the commanded product
     * @param pq the  available quantity
     * @param q the commanded quantity
     * @throws FileNotFoundException if the path is incorrect
     * @throws DocumentException if there are problems with the document
     */
    public void insufficientQuantity(String cn, String pn, int pq, int q) throws FileNotFoundException, DocumentException {
        StringBuilder path = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data = dtf.format(now);
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Bills\\Understock_Client_");
        path.append(cn);
        path.append("_Product_");
        path.append(pn);
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));
        document.open();
        Font font1 = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, BaseColor.BLACK);
        Font font2 = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        String para1 = "Insufficient Quantity";
        StringBuilder para2 = new StringBuilder();
        para2.append("Client: " + cn);
        StringBuilder para3 = new StringBuilder();
        para3.append("Product: " + pn);
        StringBuilder para4 = new StringBuilder();
        para4.append("Quantity Available: " + pq);
        StringBuilder para5 = new StringBuilder();
        para5.append("Ordered Quantity: " + q);
        Paragraph paragraph1 = new Paragraph(para1, font1);
        Paragraph paragraph2 = new Paragraph(para2.toString(), font2);
        Paragraph paragraph3 = new Paragraph(para3.toString(), font2);
        Paragraph paragraph4 = new Paragraph(para4.toString(), font2);
        Paragraph paragraph5 = new Paragraph(para5.toString(), font2);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.close();
    }
    /**
     * generates a bill for each order
     * @param o the row of the order table
     * @param cn the commander's name
     * @param pn the product commanded
     * @param price the price of the product
     * @param quantity the commanded quantity
     * @throws FileNotFoundException if the path is incorrect
     * @throws DocumentException if there are problems with the document
     */
    public void generateBill(Order o, String cn, String pn, double price, int quantity) throws FileNotFoundException, DocumentException {
        StringBuilder path = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data = dtf.format(now);
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Bills\\BILL_Client_");
        path.append(cn);
        path.append("_Product_");
        path.append(pn);
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));
        document.open();
        Font font1 = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, BaseColor.BLACK);
        Font font2 = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
        String para1 = "BILL";
        StringBuilder para2 = new StringBuilder();
        para2.append("Order Number: "+getID(o));
        StringBuilder para3 = new StringBuilder();
        para3.append("Client: " + cn);
        StringBuilder para4 = new StringBuilder();
        para4.append("Product: " + pn);
        StringBuilder para5 = new StringBuilder();
        para5.append("Quantity: " + quantity);
        StringBuilder para6 = new StringBuilder();
        para6.append("Total price:" + price);
        Paragraph paragraph1 = new Paragraph(para1, font1);
        Paragraph paragraph2 = new Paragraph(para2.toString(), font2);
        Paragraph paragraph3 = new Paragraph(para3.toString(), font2);
        Paragraph paragraph4 = new Paragraph(para4.toString(), font2);
        Paragraph paragraph5 = new Paragraph(para5.toString(), font2);
        Paragraph paragraph6 = new Paragraph(para6.toString(), font2);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.close();
    }

    /**
     * inserts a new order
     * @param o the new row inserted
     * @return the values of the row
     */
    public Order insertOrder(Order o) {
        orderDAO.insertOrder(o);
        return o;
    }
    /**
     * generates a report and shows all the orders in a tabular pdf
     * @return a list of all orders
     * @throws FileNotFoundException if the path is incorrect
     * @throws DocumentException if there are problems with the document
     */
    public List<Order> findAllOrders() throws FileNotFoundException, DocumentException {
        List<Order> orders = orderDAO.findAll();
        if (orders.isEmpty()) {
            throw new NoSuchElementException("There are no orders placed!");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data = dtf.format(now);
        StringBuilder path = new StringBuilder();
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Reports\\OrderReport_");
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));

        document.open();

        PdfPTable table = new PdfPTable(3);
        addTableHeaderClients(table);
        addClientRows(table, orders);


        document.add(table);
        document.close();
        return orders;
    }
    /**
     *inserts the data in the table
     * @param table the name of the table where the data will be added
     * @param orders the values from the table whose values will be inserted
     */
    private static void addClientRows(PdfPTable table, List<Order> orders) {
        for (Order o : orders) {
            table.addCell(o.convertIdOrder());
            table.addCell(o.convertIdClient());
            table.addCell(o.convertIdOrderItem());
        }
    }
    /**
     *creates the header of the table
     * @param table the name of the table where the header will be added
     */
    private static void addTableHeaderClients(PdfPTable table) {
        Stream.of("ID ORDER", "ID CLIENT", "ID ORDERED ITEM").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }
    /**
     *finds the id of a row
     * @param o the row whose id will be searched
     * @return  the id
     */
    public int getID(Order o){
        int id=orderDAO.findID(o);
        return id;
    }

}
