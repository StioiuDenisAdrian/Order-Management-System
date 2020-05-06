package bll;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.OrderItemDAO;
import model.OrderItem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
/**
 * Manages the instructions for the order
 */
public class OrderItemBll {

    private OrderItemDAO orderItemDAO=new OrderItemDAO();
    /**
     * inserts a new orderItem
     * @param oi the new row inserted
     * @return the values of the row
     */
    public OrderItem insertOrderItem(OrderItem oi) {
        orderItemDAO.insertOrderItem(oi);
        return oi;
    }
    /**
     * generates a report and shows all the orderItems in a tabular pdf
     * @throws FileNotFoundException if the path is incorrect and the file is not found
     * @throws DocumentException if the document has error during parsing/ addition of elements
     * @return a list of all orders
     */
    public List<OrderItem> findAllOrders() throws FileNotFoundException, DocumentException {
        List<OrderItem> orderItems = orderItemDAO.findAll();
        if(orderItems.isEmpty()){
            throw new NoSuchElementException("There are no products commanded!");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String data= dtf.format(now);
        StringBuilder path = new StringBuilder();
        path.append("C:\\Users\\Denis\\Desktop\\Assignment3_Order_Management\\Reports\\OrderItemReport_");
        path.append(data);
        path.append(".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path.toString()));

        document.open();

        PdfPTable table = new PdfPTable(3);
        addTableHeaderClients(table);
        addClientRows(table,orderItems);


        document.add(table);
        document.close();
        return orderItems;
    }
    /**
     *inserts the data in the table
     * @param table the name of the table where the data will be added
     * @param orderItems the values from the table whose values will be inserted
     */
    private static void addClientRows(PdfPTable table, List<OrderItem> orderItems){
        for(OrderItem o :orderItems){
            table.addCell(o.convertIdOrderItem());
            table.addCell(o.convertIdProduct());
            table.addCell(o.convertQuantity());
        }
    }
    /**
     *creates the header of the table
     * @param table the name of the table where the header will be added
     */
    private static void addTableHeaderClients(PdfPTable table){
        Stream.of("ID ORDERED ITEM", "ID PRODUCT", "QUANTITY ORDERED").forEach(columnTitle->{
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }
    /**
     *finds the id of a row
     * @param oi the row whose id will be searched
     * @return  the id
     */
    public int findID(OrderItem oi){
        int id;
        id=orderItemDAO.findID(oi);
        return id;
    }

}
