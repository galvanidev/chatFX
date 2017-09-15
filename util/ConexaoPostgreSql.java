package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pierre Viana
 * JDBC
 */
public class ConexaoPostgreSql {
    public static Connection getConexao() throws SQLException {
        Connection connection = null;
        String url = "org.postgresql.Driver";
        String bd = "jdbc:postgresql://192.168.33.10:5433/estudos";
        String usuario = "postgres";
        String senha = "postgres";
        try {
            Class.forName(url);
            connection = DriverManager.getConnection(bd, usuario, senha);
            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void fechar(Statement stm) {
        try {
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fechar(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fechar(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static void fechar(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexaoPostgreSql.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

