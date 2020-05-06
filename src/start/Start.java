package start;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.OrderItemBll;
import bll.ProductBLL;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import model.Client;
import model.Order;
import model.OrderItem;
import model.Product;


import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.stream.Stream;

/**
 * The Main class of the program
 */
public class Start {
    protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());
    protected static ClientBLL clientBLL = new ClientBLL();
    protected static ProductBLL productBLL = new ProductBLL();
    protected static OrderItemBll orderItemBll = new OrderItemBll();
    protected static OrderBLL orderBLL = new OrderBLL();

    /**
     * The parser for the file, read as the first argument of the jar file
     * @param args the arguments used when running from the jar
     * @throws Exception in case there are instructions incorrectly parsed
     */
    public static void main(String[] args) throws Exception {
        String inputLine = "";
        try {
            Scanner sc = new Scanner(new File(args[0]));
            while (sc.hasNextLine()) {
                inputLine = sc.nextLine();
                if (inputLine.toLowerCase().equals("report client")) {
                    List<Client> clients = clientBLL.findAllClients();
                } else if (inputLine.toLowerCase().equals("report product")) {
                    List<Product> products = productBLL.findAllProducts();
                } else if (inputLine.toLowerCase().equals("report order")) {
                    List<Order> orders = orderBLL.findAllOrders();
                    List<OrderItem> orderItems = orderItemBll.findAllOrders();
                } else {
                    String[] command = inputLine.split(":");
                    if (command.length != 2) {
                        throw new IOException("Wrong input format!");
                    }
                    if (command[0].toLowerCase().equals("insert client")) {
                        String[] clientDetails = command[1].split(",");
                        if (clientDetails.length != 2) {
                            throw new IOException("Too many client details! Only the name and address please!");
                        }
                        Client c = new Client(clientDetails[0], clientDetails[1]);
                        clientBLL.insertClient(c);
                    } else if (command[0].toLowerCase().equals("delete client")) {
                        String[] clientDetails = command[1].split(",");
                        if (clientDetails.length < 1 || clientDetails.length > 2) {
                            throw new IOException("Wrong client format!");
                        }
                        if (clientDetails.length == 2) {
                            Client c = new Client(clientDetails[0], clientDetails[1]);
                            clientBLL.deleteClient(c);
                        } else {
                            Client c = new Client(clientDetails[0]);
                            clientBLL.deleteClientAfterName(c);
                        }
                    } else if (command[0].toLowerCase().equals("delete product")) {
                        String productDetails = command[1];
                        Product p = new Product(productDetails);
                        productBLL.deleteProduct(p);

                    } else if (command[0].toLowerCase().equals("insert product")) {
                        String[] productDetails = command[1].split(",");
                        if (productDetails.length != 3) {
                            throw new IOException("Too many/few product details! Only the name and address please!");
                        }
                        for (int i = 0; i < productDetails[1].length(); i++) {
                            if (productDetails[1].charAt(i) == ' ')
                                productDetails[1] = new StringBuilder(productDetails[1]).deleteCharAt(i).toString();
                        }
                        int quantity = Integer.parseInt(productDetails[1]);
                        double price = Double.parseDouble(productDetails[2]);
                        Product p = new Product(productDetails[0], price, quantity);
                        productBLL.insertProduct(p);

                    } else if (command[0].toLowerCase().equals("order")) {
                        String[] orderDetails = command[1].split(",");
                        if (orderDetails.length != 3) {
                            throw new IOException("Insufficient arguments!");
                        }
                        for (int i = 0; i < orderDetails[2].length(); i++) {
                            if (orderDetails[2].charAt(i) == ' ')
                                orderDetails[2] = new StringBuilder(orderDetails[2]).deleteCharAt(i).toString();
                        }
                        int quantity = Integer.parseInt(orderDetails[2]);
                        Product p = productBLL.findProductByName(orderDetails[1]);
                        OrderItem oi = new OrderItem(p.getIdProduct(), quantity);
                        Client c = clientBLL.findClientByName(orderDetails[0]);
                        Order o = new Order();
                        double price;
                        int idOrderItem;
                        if (p.getQuantity() < quantity) {
                            orderBLL.insufficientQuantity(c.getName(), p.getName(), p.getQuantity(), quantity);
                        } else {
                            oi = orderItemBll.insertOrderItem(oi);
                            idOrderItem = orderItemBll.findID(oi);
                            o.setIdClient(c.getIdClient());
                            o.setOrderItem(idOrderItem);
                            o = orderBLL.insertOrder(o);
                            price = p.getPrice() * quantity;
                            orderBLL.generateBill(o, c.getName(), p.getName(), price, quantity);
                            p.setQuantity(- quantity);
                            if (p.getQuantity() == 0) {
                                productBLL.deleteProduct(p);
                            }
                            productBLL.updateProduct(p);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
