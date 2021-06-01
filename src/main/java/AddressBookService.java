import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookService {

    private List<ContactDetail> contactList = new ArrayList<>();
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
}
