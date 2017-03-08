package Servidor;

import Utils.IO;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@author Isidro*/

public class Intermediari extends Thread {
    
    Socket elSocket;
    Gestor gestor;
    boolean nickCorrecte;
    boolean fi;
    String nick;
    
    public Intermediari (Socket elSocket, Gestor gestor) {
        this.elSocket = elSocket;
        this.gestor = gestor;
        nickCorrecte = false;
        fi = false;
    }

    @Override
    public void run() {
        
        try {
            
            InputStream is = elSocket.getInputStream();
            OutputStream os = elSocket.getOutputStream();
            
            while (!nickCorrecte) {
                String nick = IO.leeLinea(is);
                
                try {
                    gestor.addClient(nick, os);
                    this.nick = nick;
                    nickCorrecte = true;
                    IO.escribeLinea("OK", os);
                }
                catch (NickInUseException ex) {
                    IO.escribeLinea("NICK", os);
                }
                catch (TooMuchUsersException ex) {
                    IO.escribeLinea("TOOMUCH", os);
                }
            }
            
            gestor.anyadirUsuaris(nick, os);
            gestor.difondre("Es conecta: "+nick);
            gestor.anyadirUsuari(nick);
            
            while (!fi) {
                
                String missatge = IO.leeLinea(is);
                if (missatge.equalsIgnoreCase("MSN")) {
                    missatge = IO.leeLinea(is);
                    gestor.difondre(missatge);
                }
                else if (missatge.equalsIgnoreCase("EXIT")) {
                    gestor.removeClient(nick);
                    System.out.println("S'ha desconnectat: "+elSocket.getRemoteSocketAddress().toString());
                    IO.escribeLinea("EXIT", os);
                    fi = true;
                }
                
            }
            
            
//            is.close();
//            os.close();
//            elSocket.close();
            
            
            
            
        } //end try
        catch (IOException ex) {
            Logger.getLogger(Intermediari.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Intermediari.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
