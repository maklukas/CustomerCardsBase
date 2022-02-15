package com.customercard.customercard.exception;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String id) {
        super("Could not find item: " + id);
    }

}
