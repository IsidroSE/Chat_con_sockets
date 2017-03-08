package Client;

import Utils.IO;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/*@author Isidro*/

public class Recibir_Missatges extends Thread {
    
    private Principal_Client pare;
    private InputStream is;
    private boolean fin;
    private boolean comensar;
    private boolean tancat;
    
    public Recibir_Missatges (Principal_Client pare, InputStream is) {
        this.pare = pare;
        this.is = is;
        fin = false;
        tancat = false;
    }
    
    @Override
    public void run() {
        
        
        try {
            
            pare.nouMissatge("Connectat");
            
            while (!fin) {
                try {
                    String ordre = IO.leeLinea(is);
                    if (ordre.equalsIgnoreCase("MSN")) {
                        String missatge = IO.leeLinea(is);
                        pare.nouMissatge(missatge);
                    }
                    else if (ordre.equalsIgnoreCase("ADD")) {
                        String missatge = IO.leeLinea(is);
                        pare.anyadirUsuari(missatge);
                    }
                    else if (ordre.equalsIgnoreCase("DEL")) {
                        String missatge = IO.leeLinea(is);
                        pare.esborrarUsuari(missatge);
                    }
                    else if (ordre.equalsIgnoreCase("EXIT")) {
                        fin = true;
                    }
                } 
                catch (IOException ex) {
                    Logger.getLogger(Recibir_Missatges.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            is.close();
            tancat = true;
            
        } catch (IOException ex) {
            Logger.getLogger(Recibir_Missatges.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void setFin(boolean fin) {
        this.fin = fin;
    }

    public boolean isTancat() {
        return tancat;
    }
    
}
