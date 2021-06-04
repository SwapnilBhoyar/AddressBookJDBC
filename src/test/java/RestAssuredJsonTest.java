import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class RestAssuredJsonTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:3000";
    }
    private ContactInfo[] getContactDetails() {
        Response response = RestAssured.get(RestAssured.baseURI + "/contacts");
        System.out.println("Employees Data: \n" + response.asString());
        return new Gson().fromJson(response.asString(), ContactInfo[].class);
    }

    private Response addContactToJSONServer(ContactInfo contactInfo) {
        String employeeJSON = new Gson().toJson(contactInfo);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        requestSpecification.body(employeeJSON);
        return requestSpecification.post(RestAssured.baseURI + "/contacts");
    }

    @Test
    void givenContactsInJSONServer_WhenRetrived_ShouldMatchCount() {
        ContactInfo[] contactData = getContactDetails();
        ContactRestAPI contactRestAPI = new ContactRestAPI(Arrays.asList(contactData));
        long entries = contactRestAPI.countContact();
        Assertions.assertEquals(1, entries);
    }

    @Test
    void givenANewContact_WhenAdded_ShouldMatchCount() {
        ContactRestAPI contactRestAPI;
        ContactInfo[] dataArray = getContactDetails();
        contactRestAPI = new ContactRestAPI(Arrays.asList(dataArray));

        ContactInfo contactInfo;
        contactInfo = new ContactInfo(0, "himanshu", "rane", "hadpsar", "pune", "maharashtra", 785412, "1478523690", "himanshu@gmail.com");
        Response response = addContactToJSONServer(contactInfo);

        contactInfo = new Gson().fromJson(response.asString(), ContactInfo.class);
        contactRestAPI.addEmployeeToList(contactInfo);
        System.out.println("Result after adding to json\n" + getContactDetails());
        long entries = contactRestAPI.countContact();
        Assertions.assertEquals(2, entries);
    }

    @Test
    void givenUpdateQuery_WhenUpdated_ShouldPassTest() {
        ContactRestAPI contactRestAPI;
        ContactInfo[] dataArray = getContactDetails();
        contactRestAPI = new ContactRestAPI(Arrays.asList(dataArray));

        contactRestAPI.updateContact("swapnil", "4563217890");
        ContactInfo contactInfo = contactRestAPI.getContact("swapnil");

        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        String contactJSON = new Gson().toJson(contactInfo);
        requestSpecification.body(contactJSON);
        Response response = requestSpecification.put(RestAssured.baseURI + "/contacts/" + contactInfo.id);

        System.out.println("After Updating json: \n" + response.asString());
        int statusCode = response.statusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    void givenDeleteQuery_WhenDeleted_ShouldPassTest() {
        ContactRestAPI contactRestAPI;
        ContactInfo[] dataArray = getContactDetails();
        contactRestAPI = new ContactRestAPI(Arrays.asList(dataArray));

        ContactInfo contactInfo = contactRestAPI.getContact("swapnil");
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type", "application/json");
        Response response = requestSpecification.delete(RestAssured.baseURI + "/contacts/" + contactInfo.id);

        System.out.print("After Deleting data from json ");
        getContactDetails();
        int statusCode = response.statusCode();
        Assertions.assertEquals(200, statusCode);
    }
}