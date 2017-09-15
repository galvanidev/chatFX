package util;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DaoException extends RuntimeException {

    private final int erroCode;

    public int getErroCode() {
        return erroCode;
    }
    
    /**
     *
     * @param mensagem retorna a mensagem de erro
     * @param erroCode retorna o código do erro
     */
    public DaoException(String mensagem, int erroCode) {
        super(mensagem);
        this.erroCode = erroCode;
        // TODO Auto-generated constructor stub
    }

}
