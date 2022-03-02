package com.customercard.customercard.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String message) {
        super("Could not find item: " + message);
    }

}
