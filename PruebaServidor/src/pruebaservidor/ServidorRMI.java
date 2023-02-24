/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaservidor;

import chat.IServidor;
import chat.Mensaje;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rene
 */
public class ServidorRMI implements IServidor {

    private Map<Integer, String> sesion_nombre = new HashMap<>();
    private Map<String, Integer> nombre_sesion = new HashMap<>();
    private Map<Integer, List<Integer>> contactos = new HashMap<>();
    private Map<Integer, List<Mensaje>> buffer = new HashMap<>();

    @Override
    public int autenticar(String nombre) throws RemoteException {
        System.out.println(nombre+" esta intentando autenticarse");
        int sesionUsuario = getSesion();
        sesion_nombre.put(sesionUsuario, nombre);
        nombre_sesion.put(nombre, sesionUsuario);
        return sesionUsuario;
    }

    @Override
    public int agregar(String nombre, int Sesion) throws RemoteException {
        if (!sesion_nombre.containsKey(Sesion)) {
            throw new RuntimeException("Sesion invalida");
        }
        if (!nombre_sesion.containsKey(nombre)) {
            throw new RuntimeException(nombre + " no esta conectado");
        }
        List<Integer> misContactos = contactos.get(Sesion);
        if (misContactos == null) {
            misContactos = new LinkedList<>();
            contactos.put(Sesion, misContactos);
        }
        misContactos.add(nombre_sesion.get(nombre));
        System.out.println(sesion+" agrego al contacto de nombre "+nombre);
        return nombre_sesion.get(nombre);
    }

    @Override
    public void enviar(String mensaje, int SesionDe, int SesionA) throws RemoteException {
        if (!sesion_nombre.containsKey(SesionDe)) {
            throw new RuntimeException("Sesion invalida");
        }
        if (!sesion_nombre.containsKey(SesionA)) {
            throw new RuntimeException("Contacto no esta conectado");
        }
        if (!contactos.get(SesionDe).contains(SesionA)) {
            throw new RuntimeException(sesion_nombre.get(SesionA) + " No es parte de tus contactos");
        }
        List<Mensaje> mensajes = buffer.get(SesionA);
        if (mensajes == null) {
            mensajes = new LinkedList<>();
            buffer.put(SesionA, mensajes);
        }
        mensajes.add(new Mensaje(mensaje, sesion_nombre.get(SesionDe)));
        
        System.out.println(sesion_nombre.get(SesionDe)+" envio mensaje a "+sesion_nombre.get(SesionA));
    }

    @Override
    public List<Mensaje> recibir(int Sesion) throws RemoteException {
        if (!sesion_nombre.containsKey(Sesion)) {
            throw new RuntimeException("Sesion invalida");
        }
        return buffer.get(Sesion);
    }

    @Override
    public void limpiarBuffer(int Sesion) throws RemoteException {
        if (!sesion_nombre.containsKey(Sesion)) {
            throw new RuntimeException("Sesion invalida");
        }
        List<Mensaje> mensajes = buffer.get(Sesion);
        if (mensajes == null) {
            mensajes = new LinkedList<>();
            buffer.put(Sesion, mensajes);
        } else {
            mensajes.clear();
        }
    }
    private static int sesion = new Random().nextInt();

    public static int getSesion() {
        return sesion++;
    }

    public static class Proceso {

        private ServidorRMI servidor;
        private Registry registry;

        public Proceso() {
            run();
        }

        public void run() {
            try {
                System.out.println("comienza RMI");
                chat.Utils.setCodeBase(IServidor.class);
                servidor = new ServidorRMI();
                System.out.println("RMI paso 1");
                IServidor remote = (IServidor) UnicastRemoteObject.exportObject(servidor, 8888);

//            Registry registry=LocateRegistry.getRegistry();
                System.out.println("RMI paso 2");
                registry = LocateRegistry.createRegistry(8888);
                System.out.println("RMI paso 3");
                registry.rebind("ServidorRMI", remote);
                System.out.println("RMI creo servidor");
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        public void stop() {
            try {
                System.out.println("deteniendo servidor RMI");
                registry.unbind("ServidorRMI");
                 System.out.println("RMI paso 1");
                UnicastRemoteObject.unexportObject(servidor, true);
                System.out.println("se detuvo el servidor RMI");
            } catch (Exception ex) {
                ex.printStackTrace();
            } 
        }
    }

}
