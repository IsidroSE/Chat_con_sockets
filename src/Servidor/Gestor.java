package Servidor;

import Utils.IO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@author Isidro*/

public class Gestor {
    
    private int maxConn;
    private ArrayList <Client> clients;
    
    public Gestor () {
        clients = new ArrayList();
        maxConn = 10;
    }
    
    public void addClient (String client, OutputStream os) throws NickInUseException, TooMuchUsersException {
        if (clients.contains(new Client(client))) {
            throw new NickInUseException();
        }
        else if (clients.size() > maxConn) {
            throw new TooMuchUsersException();
        }
        else {
            clients.add(new Client(client, os));
        }
    }
    
    public synchronized void removeClient (String client) {
        clients.remove(new Client(client));
        esborrarUsuari(client);
        difondre(client+" abandona la sala.");
    }
    
    public ArrayList <Client> llistarClients () {
        return clients;
    }
    
    public synchronized void difondre (String missatge) {
        
        for (int i=0 ; i<clients.size() ; i++) {
            try {
                if (clients.get(i).getOs() != null) {
                    IO.escribeLinea("MSN", clients.get(i).getOs());
                    IO.escribeLinea(missatge, clients.get(i).getOs());
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    //Inserix tots els clients conectats al jlist del usuari, este métode es gastará quant un usuari es conecte
    public synchronized void anyadirUsuaris (String client, OutputStream os) {
        for (int i=0 ; i<clients.size() ; i++) {
            if (!clients.get(i).getNick().equalsIgnoreCase(client)) {
                try {
                    if (os != null) {
                        IO.escribeLinea("ADD", os);
                        IO.escribeLinea(clients.get(i).getNick(), os);
                    }
                }
                catch (IOException ex) {
                    Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    //Inserix en el jlist dels usuaris el client pasat com a parámetre
    public synchronized void anyadirUsuari (String usuari) {
        for (int i=0 ; i<clients.size() ; i++) {
            try {
                if (clients.get(i).getOs() != null) {
                    IO.escribeLinea("ADD", clients.get(i).getOs());
                    IO.escribeLinea(usuari, clients.get(i).getOs());
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Esborra del jlist dels clients el usuari pasat com a parámetre
    public synchronized void esborrarUsuari (String usuari) {
        for (int i=0 ; i<clients.size() ; i++) {
            try {
                if (clients.get(i).getOs() != null) {
                    IO.escribeLinea("DEL", clients.get(i).getOs());
                    IO.escribeLinea(usuari, clients.get(i).getOs());
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Gestor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
