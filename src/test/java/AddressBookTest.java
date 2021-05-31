import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddressBookTest {
    @Test
    public void givenContactDataWhenRetrievedShouldMatchContactCount() throws DatabaseException {
        AddressBookService addressBookService = new AddressBookService();
        List<ContactDetail> contactList = addressBookService.readContactDetail();
        Assertions.assertEquals(3, contactList.size());
    }
}
