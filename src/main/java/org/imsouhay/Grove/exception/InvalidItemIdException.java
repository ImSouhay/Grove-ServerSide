package org.imsouhay.Grove.exception;

public class InvalidItemIdException extends RuntimeException {
    public InvalidItemIdException(String itemID) {
        super("Can't find item with ID:"+itemID);
    }
}
