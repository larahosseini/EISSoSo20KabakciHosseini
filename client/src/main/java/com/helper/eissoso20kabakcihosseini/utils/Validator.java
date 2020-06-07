package com.helper.eissoso20kabakcihosseini.utils;

import org.apache.commons.validator.routines.EmailValidator;

public class Validator {
    // Hilfsfunktion, um zu überprüfen ob das eine richtige email ist
    public static boolean isValidEmail(String email){
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }
}
