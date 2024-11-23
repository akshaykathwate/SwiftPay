package com.paye.whitelabel.Exception;  // Package name lowercase mein rakhein as per Java conventions

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public CustomNotFoundException(String msg) {
        super(msg);
    }
}
