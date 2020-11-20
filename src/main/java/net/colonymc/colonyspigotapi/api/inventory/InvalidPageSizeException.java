package net.colonymc.colonyspigotapi.api.inventory;

public class InvalidPageSizeException extends Exception {

    public InvalidPageSizeException(String error){
        super(error);
    }
}
