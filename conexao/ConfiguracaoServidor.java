/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

/**
 *
 * @author vlagjuio
 */
public class ConfiguracaoServidor {
    
    private static String HOST = "127.0.0.1";
    private static Integer PORT = 8293;
    
    public static void setHost(String host) {
        HOST = host;
    }
    
    public static String getHost() {
        return HOST;
    }
    
    public static void setPort(Integer port) {
        PORT = port;
    }
    
    public static Integer getPort() {
        return PORT;
    }
    
}
