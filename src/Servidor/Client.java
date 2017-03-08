package Servidor;

import java.io.OutputStream;
import java.util.Objects;

/*@author Isidro*/

public class Client {

    String Nick;
    OutputStream os;

    public Client(String Nick, OutputStream os) {
        this.Nick = Nick;
        this.os = os;
    }

    public Client(String Nick) {
        this.Nick = Nick;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Client other = (Client) obj;
        if (!Objects.equals(this.Nick, other.Nick)) {
            return false;
        }
        return true;
    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String Nick) {
        this.Nick = Nick;
    }

    public OutputStream getOs() {
        return os;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    @Override
    public String toString() {
        return "Client{" + "Nick=" + Nick + '}';
    }
    
}
