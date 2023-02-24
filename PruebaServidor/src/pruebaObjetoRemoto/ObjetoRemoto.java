/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaObjetoRemoto;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Rene
 */
public interface ObjetoRemoto extends Remote{
    public String getMensaje(String m) throws RemoteException;
}
