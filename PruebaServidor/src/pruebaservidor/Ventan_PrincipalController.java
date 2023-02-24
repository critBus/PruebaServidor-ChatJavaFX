/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaservidor;

import Utiles.FX.Ventanas.Controladores.ControladorResizable;
import Utiles.FX.VisualFX;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class Ventan_PrincipalController extends ControladorResizable {
private ServidorRMI.Proceso procesoRMI;
    @FXML
    private AnchorPane PAnchorInferior;

    @FXML
    private AnchorPane PAnchorSuperior;

    @FXML
    private JFXButton BClearTodo;

    @FXML
    private JFXButton BActualizarTodo;

    @FXML
    private TextArea TA;

    @FXML
    private JFXTextField TEnviar;

    @FXML
    private JFXButton BEnviar;

    private ServerSocket servidor;
private LinkedList<Socket> clientes=new LinkedList<>();
    @FXML
    void apretoActualizarTodo(MouseEvent event) {

    }

    @FXML
    void apretoBEnviar(ActionEvent event) {

    }

    @FXML
    void apretoClearTodo(MouseEvent event) {
procesoRMI.stop();
    }

   

    @Override
    public void iniStage(Parent p) {
        super.iniStage(p); //To change body of generated methods, choose Tools | Templates.
         addOnClosed(() -> {
            try {
                System.out.println("cierra servidor");
                servidor.close();
                System.exit(0);
            } catch (Exception ex) {
               responerException(ex);
            }
        });
        try {
            int size = 5;
            servidor = new ServerSocket(3000, size);
            ExecutorService ex = Executors.newCachedThreadPool();
          //  for (int i = 0; i < size; i++) {
                ex.execute(() -> {
                    while (true) {
                        try {
                           final Socket su = servidor.accept();
                            clientes.add(su);
                          
                            ex.execute(()->{
                             ObjectInputStream O = null;
                               try {
                                   O = new ObjectInputStream(su.getInputStream());
                                   while(true){
                                   addTextLn(TA, O.readObject() + "");
                                   System.out.println("leyo");
                                   }
                                   
                                 
                               } catch (Exception ex1) {
                                   ex1.printStackTrace();
                                 try {
                                     su.close();
                                 } catch (IOException ex2) {
                                     Logger.getLogger(Ventan_PrincipalController.class.getName()).log(Level.SEVERE, null, ex2);
                                 }
                               } finally {
                                   try {
                                       O.close();
                                   } catch (IOException ex1) {
                                       Logger.getLogger(Ventan_PrincipalController.class.getName()).log(Level.SEVERE, null, ex1);
                                   }
                               }
                            
                            });
                           
                            
                           // su.close();
                        } catch (Exception ex2) {
                            
                            ex2.printStackTrace();
                            break;
                          // responerException(ex2);
                        }
                    }
                });
                
                ServerObject.run();
                procesoRMI=new ServidorRMI.Proceso();
           // }
        } catch (Exception ex) {
            responerException(ex);
        }
    }

    @Override
    public Parent[] darMovimiento() {
        return new Parent[]{PAnchorInferior, PAnchorSuperior}; //To change body of generated methods, choose Tools | Templates.
    }

}
