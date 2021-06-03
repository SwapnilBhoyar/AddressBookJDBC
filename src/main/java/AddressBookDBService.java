import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDBService {

    private PreparedStatement addressBookPreparedStatement;
    private static AddressBookDBService addressBookDBService;
    private List<ContactDetail> addressBookData;

    AddressBookDBService() {
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/address_book_db?useSSL=false";
        String username = "root";
        String password = "1111";
        Connection con;
        System.out.println("Connecting to database:" + jdbcURL);
        con = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful:" + con);
        return con;

    }

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    public List<ContactDetail> readData() throws ContactDetailException {
        String query = null;
        query = "select * from new_address_book";
        return getAddressBookDataUsingDB(query);
    }

    private List<ContactDetail> getAddressBookDataUsingDB(String sql) throws ContactDetailException {
        List<ContactDetail> addressBookData = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookData = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
        return addressBookData;
    }

    private void prepareAddressBookStatement() throws ContactDetailException {
        try {
            Connection connection = this.getConnection();
            String query = "select * from new_address_book where first_name = ?";
            addressBookPreparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
    }

    private List<ContactDetail> getAddressBookDetails(ResultSet resultSet) throws ContactDetailException {
        List<ContactDetail> addressBookData = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                String phoneNo = resultSet.getString("phone_number");
                String email = resultSet.getString("email");
                String date = resultSet.getString("date_added");
                addressBookData.add(new ContactDetail(firstName, lastName, address, city, state, zip, phoneNo, email, date));
            }
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
        return addressBookData;
    }

    public int updateAddressBookData(String firstname, String address) throws ContactDetailException {
        try (Connection connection = this.getConnection()) {
            String query = String.format("update new_address_book set address = '%s' where first_name = '%s';", address,
                    firstname);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeUpdate(query);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.UNABLE_TO_CONNECT);
        }
    }

    public List<ContactDetail> getAddressBookData(String firstname) throws ContactDetailException {
        if (this.addressBookPreparedStatement == null)
            this.prepareAddressBookStatement();
        try {
            addressBookPreparedStatement.setString(1, firstname);
            ResultSet resultSet = addressBookPreparedStatement.executeQuery();
            addressBookData = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.UNABLE_TO_CONNECT);
        }
        System.out.println(addressBookData);
        return addressBookData;
    }

    public List<ContactDetail> readData(LocalDate start, LocalDate end) throws ContactDetailException {
        String query = null;
        if (start != null)
            query = String.format("select * from new_address_book where date_added between '%s' and '%s';", start, end);
        if (start == null)
            query = "select * from new_address_book";
        List<ContactDetail> addressBookList = new ArrayList<>();
        try (Connection con = this.getConnection();) {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            addressBookList = this.getAddressBookDetails(rs);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
        return addressBookList;
    }

    public int readDataBasedOnCity(String total, String city) throws ContactDetailException {
        int count = 0;
        String query = String.format("select %s(state) from new_address_book where city = '%s' group by city;", total, city);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
        return count;
    }

    public ContactDetail addNewContact(String firstName, String lastName, String address, String city, String state,
                                       String zip, String phoneNo, String email, String date) throws ContactDetailException {
        int id = -1;
        Connection connection = null;
        ContactDetail contactDetail = null;
        try {
            connection =this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = String.format(
                "insert into new_address_book(first_name, last_name, address, city, state, zip, phone_number, email, date_added) values ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s','%s')",
                firstName, lastName, address, city, state, zip, phoneNo, email, date);
        try ( Statement statement = connection.createStatement()) {
            int rowChanged = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
            if (rowChanged == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    id = resultSet.getInt(1);
            }
            contactDetail = new ContactDetail(firstName, lastName, address, city, state, zip, phoneNo, email, date);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return contactDetail;
    }

    public void addMultipleContactsToDB(List<ContactDetail> record) {
        Map<Integer, Boolean> contactInsertionStatus = new HashMap<Integer, Boolean>();
        record.forEach(contactDetail -> {
            Runnable task = () -> {
                contactInsertionStatus.put(contactDetail.hashCode(), false);
                System.out.println("Contact Being Added:" + Thread.currentThread().getName());
                try {
                    this.addNewContact(contactDetail.getFirstName(), contactDetail.getLastName(),
                            contactDetail.getAddress(), contactDetail.getCity(), contactDetail.getState(),
                            contactDetail.getZip(), contactDetail.getPhoneNo(), contactDetail.getEmail(),
                            contactDetail.getDate());
                } catch (ContactDetailException e) {
                    e.printStackTrace();
                }
                contactInsertionStatus.put(contactDetail.hashCode(), true);
                System.out.println("Contact Added:" + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, contactDetail.getFirstName());
            thread.start();
        });
        while (contactInsertionStatus.containsValue(false)) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
    }
}