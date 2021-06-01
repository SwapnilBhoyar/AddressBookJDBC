import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private static AddressBookDBService addressBookDBService;

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    private Connection getConnection() throws DatabaseException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_db?useSSL=false";
        String username = "root";
        String password = "1111";
        Connection connection;
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
                String phonenumber = resultSet.getString("phone_number");
                String emial = resultSet.getString("email");
                contactList.add(new ContactDetail(firstName, lastName, address, city, state, zip, phonenumber, emial));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public List<ContactDetail> getContactData(String first_name, String last_name) throws DatabaseException {
        try {
            getPreparedStatement();
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            return getContactData(preparedStatement.executeQuery());
        }catch (SQLException e) {
            throw new DatabaseException("unable to get contact data");
        }
    }

    private List<ContactDetail> getContactData(ResultSet resultSet) throws SQLException {
        List<ContactDetail> contactList = new ArrayList<>();
        while (resultSet.next()) {
            String firstname = resultSet.getString("first_name");
            String lastname = resultSet.getString("last_name");
            String address = resultSet.getString("address");
            int zip = resultSet.getInt("zip");
            String city = resultSet.getString("city");
            String state = resultSet.getString("state");
            String phonenumber = resultSet.getString("phone_number");
            String email = resultSet.getString("email");
            contactList.add(new ContactDetail(firstname, lastname, address, city, state, zip, phonenumber, email));
        }
        return contactList;
    }

    private void getPreparedStatement() throws DatabaseException, SQLException {
        this.getConnection();
        if (preparedStatement == null) {
            String sql = "SELECT * FROM People p INNER JOIN Address a ON p.id = a.id INNER JOIN address_book ab ON ab.id = a.id WHERE first_name = ? AND last_name = ?;";
            preparedStatement = connection.prepareStatement(sql);
        }
    }

    public List<ContactDetail> readData() throws DatabaseException {
        String sql = "SELECT * FROM People p INNER JOIN Address a ON p.id = a.id INNER JOIN address_book ab ON ab.id = a.id;";
        return this.getContactData(sql);
    }

    public int updateContactData(String firstname, String lastname, String phone) throws DatabaseException, SQLException {
        connection = this.getConnection();
        String sql = "UPDATE People SET phone_number = ? WHERE first_name = ? AND last_name = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,phone);
        preparedStatement.setString(2, firstname);
        preparedStatement.setString(3,lastname);
        return preparedStatement.executeUpdate();
    }

    public List<ContactDetail> getContactForGivenDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = String.format("SELECT * FROM People p INNER JOIN Address a ON p.id = a.id INNER JOIN address_book ab ON ab.id = a.id WHERE date_added BETWEEN '%s' AND '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getContactData(sql);
    }
}
