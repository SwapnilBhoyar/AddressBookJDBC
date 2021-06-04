import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class AddressBookTest {
    static AddressBookService addressBookService;

    @Test
    public void givenContactDataWhenRetrievedShouldMatchContactCount() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactDetailList = addressBookService.readAddressBook(AddressBookService.IOService.DB_IO);
        Assertions.assertEquals(11, contactDetailList.size());
    }

    @Test
    public void givenNewDataForContactWhenUpdatedShouldBeInSync() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactDetailList = addressBookService.readAddressBook(AddressBookService.IOService.DB_IO);
        addressBookService.updateRecord("swapnil", "katraj");
        boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("swapnil");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenDateRangeWhenRetrievedShouldMatchContactCount() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactDetailList = addressBookService.readAddressBook(AddressBookService.IOService.DB_IO, "2020-01-01", "2020-12-12");
        Assertions.assertEquals(4, contactDetailList.size());
    }

    @Test
    public void givenContactRetrieveNumberOfContactByCity() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        Assertions.assertEquals(2, addressBookService.readAddressBook("count", "pune"));
    }

    @Test
    public void givenNewContactShouldAddToAddressBook() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBook(AddressBookService.IOService.DB_IO);
        addressBookService.addNewContact("akshay", "khiari", "kondhwa", "pune", "maharashtra", "147852", "2587413690", "akshay@gmail.com", "2020-06-06");
        boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("akshay");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenMultipleContactWhenAddedShouldSyncWithDB() throws ContactDetailException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactDetailList = addressBookService.readAddressBook(AddressBookService.IOService.DB_IO);
        ContactDetail[] contactArray = {
                new ContactDetail("prasad", "abnave", "hadapsar", "pune", "maharashtra", "369852", "7532418960",
                        "prasad@gmail.com", "2020-07-07"),
                new ContactDetail("shubham", "chand", "hadapsar", "pune", "maharashtra", "369852", "7532418960",
                        "shubham@gmail.com", "2020-08-08") };
        addressBookService.addMultipleContacts(Arrays.asList(contactArray));
        boolean result1 = addressBookService.checkUpdatedRecordSyncWithDatabase("prasad");
        boolean result2 = addressBookService.checkUpdatedRecordSyncWithDatabase("shubham");
        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
    }
}