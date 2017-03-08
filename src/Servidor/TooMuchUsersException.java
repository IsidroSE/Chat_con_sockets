package Servidor;

/*@author Isidro*/

public class TooMuchUsersException extends Exception {

    public TooMuchUsersException() {}
    
    public String excErrorPersonalizado () {
        return "TOOMUCH";
    }
    
}
