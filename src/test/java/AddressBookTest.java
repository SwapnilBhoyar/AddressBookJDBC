import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AddressBookTest {
    @Test
    public void givenContactDataWhenRetrievedShouldMatchContactCount() throws DatabaseException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactList = addressBookService.readContactDetail();
        Assertions.assertEquals(3, contactList.size());
    }

    @Test
    public void givenNewDataForContactWhenUpdatedShouldBeInSync() throws DatabaseException, SQLException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactDetailList = addressBookService.readContactDBData();
        addressBookService.updateContactData("swapnil", "bhoyar", "9087654321");
        boolean result = addressBookService.checkContactDataSync("swapnil", "bhoyar");
        Assertions.assertEquals(true, result);
    }

    @Test
    public void givenDateRangeWhenRetrievedShouldMatchContactCount() throws DatabaseException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactList = addressBookService.readContactDetail();
        LocalDate startDate = LocalDate.of(2020, 02, 02);
        LocalDate endDate = LocalDate.now();
        List<ContactDetail> contactListData = addressBookService.readContactDataForGivenDateRange(startDate, endDate);
        Assertions.assertEquals(3, contactList.size());
    }
}
