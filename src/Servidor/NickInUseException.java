package Servidor;

/*@author Isidro*/

public class NickInUseException extends Exception{

    public NickInUseException() {}
    
    public String excErrorPersonalizado () {
        return "NICK";
    }
    
}
