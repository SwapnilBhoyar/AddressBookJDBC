public class ContactDetailException extends Exception {

    enum ExceptionType {
        DB_EXCEPTION, NOT_FOUND, UNABLE_TO_CONNECT
    }

    public ExceptionType type;

    public ContactDetailException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}