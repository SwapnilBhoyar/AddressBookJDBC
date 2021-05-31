import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

    private Connection getConnection() throws DatabaseException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_db?useSSL=false";
        String username = "root";
        String password = "1111";
        Connection connection;
        //Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
        }catch (Exception e) {
            throw new DatabaseException("Connection Unsuccessful");
        }
        return connection;
    }

    private List<ContactDetail> getContactData(String sql) {
        List<ContactDetail> contactList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                int zip = resultSet.getInt("zip");
                contactList.add(new ContactDetail(firstName, lastName, address, city, state, zip));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public List<ContactDetail> readData() throws DatabaseException {
        String sql = "SELECT * FROM People p INNER JOIN Address a ON p.id = a.id INNER JOIN address_book ab ON ab.id = a.id;";
        return this.getContactData(sql);
    }
}
