
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

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
    JTextArea txt;
    BufferedReader bf;
    String sender;
    String receiver;
    final JFileChooser fc = new JFileChooser();

    public OutputThread(Socket s, JTextArea txt, String sender, String receiver) {
        super();
        this.socket = s;
        this.txt = txt;
        this.sender = sender;
        this.receiver = receiver;
        try {
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Network Error!");
            System.exit(0);
        }
    }

    private void saveFile() {
        int yes = JOptionPane.showConfirmDialog(null, "Your friend sent you a text file, do you want to save?", "Important!", JOptionPane.YES_NO_OPTION);
        if (yes == JOptionPane.YES_OPTION) {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setDialogTitle("Choose a Directory to save your Text File");
            int result = fc.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File dir = fc.getSelectedFile();
                    String fileName = bf.readLine();
                    FileWriter fw = new FileWriter(dir.getAbsolutePath() + "\\" + fileName);
                    PrintWriter pw = new PrintWriter(fw);
                    JOptionPane.showMessageDialog(null, fileName);
                    String s;
                    String end = "###EOF###";
                    while (!(s = bf.readLine()).equalsIgnoreCase(end)) {
                        pw.println(s);
                        pw.flush();
                    }
                    pw.close();
                    fw.close();
                    txt.append("\n" + this.receiver + ": " + bf.readLine());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }

            }
        }
        
        else {
                try {
                    String s;
                    String end = "###EOF###";
                    while (!(s = bf.readLine()).equalsIgnoreCase(end)) {
                    }
                    bf.readLine();
                    txt.append("\n" + this.receiver + ": " + bf.readLine());
                } catch (Exception e) {
                }

            }

    }

    @Override
    public void run() {
        while (true) {
            try {
                if (socket != null) {
                    String msg = bf.readLine();
                    if (msg.equalsIgnoreCase("##@@$$")) {
                        saveFile();
                    } else {
                        txt.append("\n" + this.receiver + ": " + msg);
                    }
                    sleep(50);
                }
            } catch (Exception e) {
            }
        }
    }

}
