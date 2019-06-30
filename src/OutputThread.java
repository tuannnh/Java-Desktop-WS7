
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tuannnh
 */
public class OutputThread extends Thread {

    Socket socket;
    JTextPane txt;
    BufferedReader bf;
    String sender;
    String receiver;
    Document doc;

    final JFileChooser fc = new JFileChooser();

    public OutputThread(Socket s, JTextPane txt, String sender, String receiver) {
        super();
        this.socket = s;
        this.txt = txt;
        this.sender = sender;
        this.receiver = receiver;
        try {
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF8"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Network Error!");
            System.exit(0);
        }
        doc = this.txt.getDocument();

    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket != null) {
                    String msg = "";
                    if ((msg = bf.readLine()) != null && msg.length() > 0) {
                        doc.insertString(doc.getLength(), this.receiver + ": " + msg+"\n", null);
                    }
                    sleep(50);
                }
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(null, receiver + " is Offline.");
                try {
                    doc.insertString(doc.getLength(), this.receiver + " is Offline!\n", null);
                } catch (BadLocationException ex) {
                }
                 return;
            }
        }
    }

}
