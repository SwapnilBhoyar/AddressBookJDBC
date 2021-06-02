import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressBookService {

    public enum IOService {
        DB_IO
    }

    private List<ContactDetail> contactList = new ArrayList<>();
    private Map<String, Integer> contactByCityList;
    private AddressBookDBService addressBookDBService;

    AddressBookService () {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public List<ContactDetail> readContactDetail() throws DatabaseException {
        List<ContactDetail> contactList = new ArrayList<>();
        AddressBookDBService addressBookDBService = new AddressBookDBService();
        contactList = addressBookDBService.readData();
        return contactList;
    }

    public List<ContactDetail> readContactDBData() throws DatabaseException {
        this.contactList = addressBookDBService.readData();
        return contactList;
    }

    public void updateContactData(String firstname, String lastname, String phone) throws SQLException, DatabaseException {
        int result = addressBookDBService.updateContactData(firstname, lastname, phone);
        if (result == 0) {
            return;
        }
        ContactDetail contact = this.getContact(firstname, lastname);
        if (contact != null)
            contact.phoneNumber = phone;
    }

    private ContactDetail getContact(String firstname, String lastname) {
        ContactDetail contact = this.contactList.stream()
                .filter(contactDetail -> contactDetail.firstname.equals(firstname) && contactDetail.lastname.equals(lastname))
                .findFirst()
                .orElse(null);
        return contact;
    }

    public boolean checkContactDataSync(String firstname, String lastname) throws DatabaseException {
        List<ContactDetail> contactList = addressBookDBService.getContactData(firstname, lastname);
        return contactList.get(0).equals(getContact(firstname, lastname));
    }

    public List<ContactDetail> readContactDataForGivenDateRange(LocalDate startDate, LocalDate endDate) {
        List<ContactDetail> contactListData = addressBookDBService.getContactForGivenDateRange(startDate, endDate);
        return contactListData;
    }

    public Map<String, Integer> readContactByCity() throws DatabaseException {
        Map<String, Integer> contactByCityListList = addressBookDBService.getContactByCity();
        return contactByCityListList;
    }

    public void addContact(String firstname, String lastname, String address, String city, String state, int zip, String phonenumber, String email, LocalDate date) throws DatabaseException {
        contactList.add(addressBookDBService.addContact(firstname, lastname, address, city, state, zip, phonenumber, email, date));
    }
}
