package com.prime.util.excemptions;

@SuppressWarnings("serial")
public class PlayerNotFound extends IndexOutOfBoundsException {

    public PlayerNotFound(String messageString) {
        super(messageString);
    }
    
    

}
