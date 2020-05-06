package dao;
import connection.ConnectionFactory;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class, used to model the other data access classes through reflection, so the connection to the database is made only once
 * @param <T> the generic class
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    /**
     * creates a selection query conditioned by a field
     * @param field on which the selection is made
     * @return the selectQuery
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM order_management.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }
    /**
     * finds the id of a row
     * @return the sql statement
     */
    private String findIDQuery(){
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();
        sb.append("SELECT * ");
        sb.append(" FROM order_management."+type.getSimpleName());
        sb.append(" WHERE ");
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].getName());
            sb.append("=?");
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
    /**
     * used to create a query to select the row whose parameters are all known
     * @return the sql statement
     */
    private String createSelectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM order_management.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        Field[] fields = type.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].getName());
            sb.append("=?");
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
    /**
     * used to create a query to select all the rows of a table
     * @return the sql statement
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM order_management.");
        sb.append(type.getSimpleName());
        return sb.toString();
    }
    /**
     * used to create a query to insert a row in a table
     * @return the sql statement
     */
    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO order_management.");
        sb.append(type.getSimpleName());
        sb.append(" (");
        Field[] fields = type.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].getName());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(") VALUES (");
        for (int i = 1; i < fields.length; i++) {

            sb.append("?");
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        sb.append(")");
        return sb.toString();
    }
    /**
     * used to create a query to delete a row from the table
     * @return the sql statement
     */
    private String createDeleteQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM order_management.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        Field[] fields = type.getDeclaredFields();
        for (int i = 1; i < fields.length; i++) {
            sb.append(fields[i].getName());
            sb.append("=?");
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
    /**
     * used to create a query to delete a row after the name
     * @return the sql statement
     */
    private String createDeleteAfterNameQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM order_management.");
        sb.append(type.getSimpleName());
        sb.append(" WHERE ");
        Field[] fields = type.getDeclaredFields();
        sb.append(fields[1].getName());
        sb.append("=?");
        return sb.toString();
    }
    /**
     * used to update a query
     * @return the sql statement
     */
    private String createUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE order_management.");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        Field[] fields = type.getDeclaredFields();
        sb.append(fields[fields.length - 1].getName());
        sb.append("=");
        sb.append(fields[fields.length - 1].getName());
        sb.append("+ ? ");
        sb.append(" WHERE ");

        for (int i = 1; i < fields.length - 1; i++) {
            sb.append(fields[i].getName());
            sb.append("=?");
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 4, sb.length());
        return sb.toString();
    }
    /**
     * finds all the elements of a table
     * @return the found elements
     */
    public List<T> findAll() {
        // TODO:
        //List<T> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * finds a row after its id
     * @param id the id used for the search
     * @return the found row
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id" + type.getSimpleName());
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * finds the id of a row
     * @param t the row whose id we want to know
     * @return the found id
     */
    public Integer findID(T t){
        int id;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = findIDQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            resultSet = statement.executeQuery();
            t=createObjects(resultSet).get(0);
            fields[0].setAccessible(true);
            id=(int) fields[0].get(t);
            return id;
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findId " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * find a row after its name
     * @param Name the name of the row to be searched
     * @return the found row
     */
    public T findByName(String Name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("Name");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, Name);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * checks if there are duplicates in a table( rows with the same fields except the id)
     * @param t the searched row
     * @return true if there are not duplicates, and false if there are
     */
    public boolean duplicates(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            resultSet = statement.executeQuery();
            List<T> l = createObjects(resultSet);
            if (!l.isEmpty()) {
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByOthers " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return true;
    }
    /**
     * finds the objects a class
     * @param resultSet the found result
     * @return the list of objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setNull(1, Types.INTEGER);
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            statement.executeUpdate();

        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * updates a row
     * @param t the row to be updated
     * @return the update row
     * @throws IllegalAccessException if the access cannot be realised
     * @throws SQLException if the query is not correctly written
     */
    public T update(T t) throws IllegalAccessException, SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field[] fields = t.getClass().getDeclaredFields();
            fields[fields.length-1].setAccessible(true);
            statement.setObject(1, fields[fields.length - 1].get(t));
            for (int i = 1; i < fields.length - 1; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i + 1, fields[i].get(t));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * deletes a row
     * @param t the row to be deleted
     * @return the delete row
     */
    public T delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field[] fields = t.getClass().getDeclaredFields();
            statement.setObject(1, null);
            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                statement.setObject(i, fields[i].get(t));
            }
            statement.executeUpdate();


        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {

            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * deletes a row of which we know only the name
     * @param t the row to be deleted
     * @return the delete row
     */
    public T deleteAfterName(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteAfterNameQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field[] fields = t.getClass().getDeclaredFields();
            fields[1].setAccessible(true);
            statement.setObject(1, fields[1].get(t));
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {

            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
}
