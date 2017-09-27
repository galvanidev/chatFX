/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Galvani Júnior
 */
public class ValidadorEmail {
    
    /**
     * 
     * @param email
     * @return verdadeiro se o e-mail for válido
     */
    public static boolean validar(String email) {
        Pattern regexEmail
                = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexEmail.matcher(email);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
