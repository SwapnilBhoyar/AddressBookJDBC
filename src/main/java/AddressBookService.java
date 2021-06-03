import java.time.LocalDate;
import java.util.List;

public class AddressBookService {

    public enum IOService {
        DB_IO
    }

    private List<ContactDetail> addressBookList;
    private static AddressBookDBService addressBookDBService;

    public AddressBookService() {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public List<ContactDetail> readAddressBookData(IOService ioservice) throws ContactDetailException {
        if (ioservice.equals(IOService.DB_IO))
            return this.addressBookList = addressBookDBService.readData();
        return this.addressBookList;
    }

    public void updateRecord(String firstname, String address) throws ContactDetailException {
        int result = addressBookDBService.updateAddressBookData(firstname, address);
        if (result == 0)
            return;
        ContactDetail contactDetail = this.getAddressBookData(firstname);
        if (contactDetail != null)
            contactDetail.address = address;
    }

    public boolean checkUpdatedRecordSyncWithDatabase(String firstname) throws ContactDetailException {
        List<ContactDetail> addressBookData = addressBookDBService.getAddressBookData(firstname);
        return addressBookData.get(0).equals(getAddressBookData(firstname));
    }

    private ContactDetail getAddressBookData(String firstname) {
        return this.addressBookList.stream().filter(addressBookItem -> addressBookItem.firstName.equals(firstname))
                .findFirst().orElse(null);
    }

    public List<ContactDetail> readAddressBookData(IOService ioService, String start, String end)
            throws ContactDetailException {
        try {
            LocalDate startLocalDate = LocalDate.parse(start);
            LocalDate endLocalDate = LocalDate.parse(end);
            if (ioService.equals(IOService.DB_IO))
                return addressBookDBService.readData(startLocalDate, endLocalDate);
            return this.addressBookList;
        } catch (ContactDetailException e) {
            throw new ContactDetailException(e.getMessage(), ContactDetailException.ExceptionType.DB_EXCEPTION);
        }
    }

    public int readAddressBookData(String function, String city) throws ContactDetailException {
        return addressBookDBService.readDataBasedOnCity(function, city);
    }

    public void addNewContact(String firstName, String lastName, String address, String city, String state, String zip,
                              String phoneNo, String email, String date) throws ContactDetailException {
        addressBookList.add(addressBookDBService.addNewContact(firstName, lastName, address, city, state, zip, phoneNo,
                email, date));
    }

    public void addMultipleContactsToDB(List<ContactDetail> record) {
        AddressBookDBService addressBookDBService = new AddressBookDBService();
        addressBookDBService.addMultipleContactsToDB(record);
    }
}