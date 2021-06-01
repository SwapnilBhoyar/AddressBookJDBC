public class ContactDetail {
    public String firstname, lastname;
    public String address, city, state;
    public int zip;
    public String phoneNumber;
    public String email;

    public ContactDetail(String firstname, String lastname, String address, String city, String state, int zip, String phoneNumber, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public ContactDetail() {}

    public ContactDetail(String firstname, String lastname, String address, String city, String state, int zip) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getFirstName() {
        return firstname;
    }
    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }
    public String getLastName() {
        return lastname;
    }
    public void setLastName(String lastName) {
        this.lastname = lastName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getZip() {
        return zip;
    }
    public void setZip(int zip) {
        this.zip = zip;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNo(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return " First Name:" + firstname +
                " Last Name:" + lastname +
                " Address:" + address +
                " City:" + city +
                " State:" + state +
                " Zip:" + zip +
                " PhoneNumber:" + phoneNumber +
                " EmailId:" + email + '\n';
    }
}
