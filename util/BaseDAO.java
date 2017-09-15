/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Collection;

public interface BaseDAO{
    public Object inserir (Object obj) throws DaoException;
    public Object alterar (Object obj) throws DaoException;
    public Boolean excluir (Integer codigo) throws DaoException;
    public Collection listar() throws DaoException;
    public Object selecionar (Integer codigo) throws DaoException;
}
