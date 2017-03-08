package Client;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*@author Isidro*/

public class Panel_Inici extends JPanel {
    
    private JLabel lNick;
    private JTextField tfNick;
    private JLabel lPort;
    private JTextField tfPort;
    private JLabel error;

    public Panel_Inici () {
        setLayout(new GridLayout(5, 1));
        
        lNick = new JLabel("Nick:");
        tfNick = new JTextField();
        add(lNick);
        add(tfNick);
        
        lPort = new JLabel("Port:");
        tfPort = new JTextField();
        add(lPort);
        add(tfPort);
        
        error = new JLabel();
        add(error);
    }
    
    public String getNick () {
        String nick;
        
        nick = tfNick.getText();
        
        return nick;
    }
    
    public String getPort () {
        String port;
        
        port = tfPort.getText();
        
        return port;
    }
    
    public void setError (String error) {
        this.error.setText(error);
    }
    
    public void netejarText () {
        tfNick.setText("");
        tfPort.setText("");
    }
    
}
