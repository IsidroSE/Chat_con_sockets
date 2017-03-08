package Client;

import Servidor.Gestor;
import Utils.IO;
import com.sun.glass.events.KeyEvent;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

/*@author Isidro*/

public class Principal_Client extends javax.swing.JFrame {
    
    private Panel_Inici pInici;
    private boolean conectat;
    private String nick;
    private Socket elSocket;
    private String portSeleccionat;
    private int port;
    private OutputStream os;
    private Recibir_Missatges rm;
    private DefaultListModel modelo;

    public Principal_Client() {
        
        conectat = false;
        
        pInici = new Panel_Inici();
        
        //Antes de carregar el chat, preguntarem per el nick, el port i crearem la connexio amb el servidor
        while (!conectat) {
            if(JOptionPane.showConfirmDialog(this, pInici, "Introduix les següents dades", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
                try {
                    nick = pInici.getNick();
                    portSeleccionat = pInici.getPort();
                    if (nick.length() < 3) {
                        pInici.setError("Error, no has especificat el nick o has escrit un nick amb menys de 3 caracters.");
                    }
                    if (nick.contains(" ")) {
                        pInici.setError("Error, el nick no pot contindre espalls en blanc.");
                    }
                    else if (portSeleccionat.length() < 1) {
                        pInici.setError("Error, tens que especificar un port.");
                    }
                    else {
                        port = Integer.parseInt(portSeleccionat);
                        elSocket = new Socket("localhost", port);
                        InputStream is = elSocket.getInputStream();
                        os = elSocket.getOutputStream();
                        
                        IO.escribeLinea(nick, os);
                        String resposta = IO.leeLinea(is);
                        
                        if (resposta.equalsIgnoreCase("OK")) {
                            rm = new Recibir_Missatges(this, is);
                            conectat = true;
                        }
                        else if (resposta.equalsIgnoreCase("NICK")) {
                            pInici.setError("El nick introduit ja ha sigut seleccionat, trian un altre.");
                        }
                        else if (resposta.equalsIgnoreCase("TOOMUCH")) {
                            pInici.setError("Hi han masa usuaris conectats en este moment, torna més tard.");
                        }
                        else {
                            pInici.setError("S'ha produit un error al conectar amb el servidor, proba amb un port diferent.");
                        }
                    }
                    pInici.netejarText();
                }
                catch (IOException ex) {
                    Logger.getLogger(Principal_Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                System.exit(0);
            }
        }
        
        initComponents();
        //Fem que la finestra ocupe tota la pantalla
        Rectangle bounds = getMaximizedBounds();
        setMaximizedBounds(bounds);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //Inicialitzem el Jlist coloquem el model
        modelo = new DefaultListModel();
        lNomUsuaris.setModel(modelo);
        
        

        //Aço fará que, quant tanques la finestra, també es paré el recibidor de missatges del client i el intermediari
        //que mos aten
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                try {
                    IO.escribeLinea("EXIT", os);
                    rm.setFin(true);
                    while (!rm.isTancat());
                    os.close();
                    elSocket.close();
                }
                catch (IOException ex) {
                    Logger.getLogger(Principal_Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        //Iniciant el recibidor de missatges
        nouMissatge("Connectant-se");
        rm.start();
        
        //Fem que el botó de enviar tinga com acces directe la tecla enter
        getRootPane().setDefaultButton(bEnviar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pPrincipal = new javax.swing.JPanel();
        spMissatges = new javax.swing.JScrollPane();
        taMissatges = new javax.swing.JTextArea();
        spNomUsuaris = new javax.swing.JScrollPane();
        lNomUsuaris = new javax.swing.JList();
        pSud = new javax.swing.JPanel();
        tfMissatge = new javax.swing.JTextField();
        bEnviar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pPrincipal.setLayout(new java.awt.BorderLayout(2, 2));

        taMissatges.setEditable(false);
        taMissatges.setColumns(20);
        taMissatges.setRows(5);
        spMissatges.setViewportView(taMissatges);

        pPrincipal.add(spMissatges, java.awt.BorderLayout.CENTER);

        spNomUsuaris.setViewportView(lNomUsuaris);

        pPrincipal.add(spNomUsuaris, java.awt.BorderLayout.LINE_END);

        pSud.setLayout(new java.awt.BorderLayout(2, 2));

        tfMissatge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfMissatgeKeyReleased(evt);
            }
        });
        pSud.add(tfMissatge, java.awt.BorderLayout.CENTER);

        bEnviar.setText("Enviar");
        bEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnviarActionPerformed(evt);
            }
        });
        pSud.add(bEnviar, java.awt.BorderLayout.LINE_END);

        pPrincipal.add(pSud, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEnviarActionPerformed
        try {
            String missatge = tfMissatge.getText();
            if (missatge.equalsIgnoreCase("/EXIT")) {
                acabar();
            }
            else {
                IO.escribeLinea("MSN", os);
                IO.escribeLinea(nick+": "+missatge, os);
            }
            tfMissatge.setText("");
        } catch (IOException ex) {
            Logger.getLogger(Principal_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bEnviarActionPerformed

    private void tfMissatgeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMissatgeKeyReleased
//        int key = evt.getKeyCode();
//        
//        if (key == KeyEvent.VK_LEFT) {
//            
//        }
    }//GEN-LAST:event_tfMissatgeKeyReleased

    //Actualizem el tex area amb el nou missatge que ha escrit algún usuari
    public void nouMissatge (String missatge) {
        if (taMissatges != null) {
            taMissatges.setText(taMissatges.getText()+missatge+"\n");
        }
    }
    
    //Anyadix un nou usuari al jlist
    public void anyadirUsuari (String usuari) {
        modelo.addElement(usuari);
    }
    
    //Esborra un usuari del jlist
    public void esborrarUsuari (String usuari) {
        if (modelo.getSize() > 0) {
            modelo.removeElement(usuari);
        }
    }
    
    //Acaba el programa cridant al listener per a que es tanquen els dos fils del client
    public void acabar () {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Principal_Client().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEnviar;
    private javax.swing.JList lNomUsuaris;
    private javax.swing.JPanel pPrincipal;
    private javax.swing.JPanel pSud;
    private javax.swing.JScrollPane spMissatges;
    private javax.swing.JScrollPane spNomUsuaris;
    private javax.swing.JTextArea taMissatges;
    private javax.swing.JTextField tfMissatge;
    // End of variables declaration//GEN-END:variables
}
