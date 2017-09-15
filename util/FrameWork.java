/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author A135
 */
public class FrameWork {

    
    public static String criptografar(String senha) {
        String sign = senha;
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("MD5");
            md.update(sign.getBytes());
            byte[] hash = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append(
                            "0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
            sign = hexString.toString();
        } catch (Exception nsae) {
            nsae.printStackTrace();
        }
        return sign;
    }

    public static void limparCampos(JPanel jPanel) {
        Component[] components = jPanel.getComponents();
        JTextField jTextField = null;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JTextField) {
                jTextField = (JTextField) components[i];
                jTextField.setText("");
            }
        }
    }
}
