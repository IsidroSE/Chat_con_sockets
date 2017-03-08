package Servidor;

import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@author Isidro*/

public class Principal_Servidor {
    
    static final int PORT = 5000;

    public static void main(String[] args) {
        
        
        try {
            
            Gestor gestor = new Gestor();
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Esperant connexions en "+PORT);
            
            while (true) {
                Socket elSocket = ss.accept();
                System.out.println("S'ha connectat: "+elSocket.getRemoteSocketAddress().toString());
                Intermediari it = new Intermediari(elSocket, gestor);
                it.start();
            }
        }
        catch (Exception ex) {
            Logger.getLogger(Principal_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
