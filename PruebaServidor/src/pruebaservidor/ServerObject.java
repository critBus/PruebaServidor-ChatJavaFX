/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaservidor;

import java.rmi.registry.*;
import java.rmi.server.*;
import java.rmi.*;
import pruebaObjetoRemoto.ObjetoRemoto;

/**
 *
 * @author Rene
 */
public class ServerObject extends UnicastRemoteObject implements ObjetoRemoto{

    ServerObject() throws RemoteException{
    super();
    }  
    @Override
    public String getMensaje(String m) throws RemoteException {
      return "hola "+m;
    }
    
    public static void run() {
   try{
       System.out.println("conienza servidor");
        ServerObject calculator;
        LocateRegistry.createRegistry(1099);
        calculator=new ServerObject();
        Naming.bind("ElObjetoRemoto", calculator);
        System.out.println("el servidor esta listo");
   }catch(Exception ex){
   ex.printStackTrace();
   }
    }
    
}
