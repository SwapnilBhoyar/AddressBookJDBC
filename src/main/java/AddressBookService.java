import java.util.ArrayList;
import java.util.List;

public class AddressBookService {

    public List<ContactDetail> readContactDetail() throws DatabaseException {
        List<ContactDetail> contactList = new ArrayList<ContactDetail>();
        AddressBookDBService addressBookDBService = new AddressBookDBService();
        contactList = addressBookDBService.readData();
        return contactList;
    }
}
