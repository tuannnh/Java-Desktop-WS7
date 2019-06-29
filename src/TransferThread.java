
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
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
public class TransferThread extends Thread {

    Socket socket;
    JTextPane txt;
    InputStream is;
    OutputStream os;
    String sender;
    String receiver;
    BufferedReader bf;
    DataOutputStream dos = null;
    final JFileChooser fc = new JFileChooser();
    Document doc;

    public TransferThread(Socket s, JTextPane txt, String sender, String receiver) {
        super();
        this.socket = s;
        this.txt = txt;
        this.sender = sender;
        this.receiver = receiver;
        try {
            is = socket.getInputStream();
            bf = new BufferedReader(new InputStreamReader(is));
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Network Error!");
            System.exit(0);
        }
        doc = this.txt.getDocument();

    }

    private void saveFile(String fileName) {

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Choose a Directory to save your File");
        int result = fc.showSaveDialog(null);
        try {
            if (result == JFileChooser.APPROVE_OPTION) {
                File dir = fc.getSelectedFile();
                File receiveFile = new File(dir.getAbsolutePath() + "\\" + fileName);
                byte[] mybytearray = new byte[16 * 1024];
                FileOutputStream fos = new FileOutputStream(receiveFile, false);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                bos.write(mybytearray, 0, bytesRead);
                bos.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Download File Error!");
            System.exit(0);

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket != null) {
                    String fileName = bf.readLine();
                    if (fileName != null) {
                        saveFile(fileName);
                        doc.insertString(doc.getLength(), "\nDownloaded a file " + fileName + " from " + this.sender + "!", null);
                    }
                    sleep(500);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

}
