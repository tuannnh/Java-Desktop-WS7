
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tuannnh
 */
public class EmoThread extends Thread {

    JTextPane txtMessage;
    JTextPane txtContent;
    String message, content;
    Document doc;
    Hashtable<String, String> emos = new Hashtable<>();
    String text;
    Style labelStyle;

    public EmoThread(JTextPane message, JTextPane content) {
        this.txtMessage = message;
        this.txtContent = content;
        for (int i = 1; i < 21; i++) {
            if(i<10)emos.put("#emo0" + i, "" + i);
            else emos.put("#emo" + i, "" + i);
        }

        doc = this.txtContent.getDocument();
        text = this.txtContent.getText();
        StyleContext context = new StyleContext();
        labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);

    }

    public void run() {

        while (true) {
            try {
                Set<String> key = emos.keySet();
                Iterator<String> itr = key.iterator();

                while (itr.hasNext()) {
                    String symbol = itr.next();
                    String icon = emos.get(symbol);
                    text = this.txtContent.getText();
                    
                    while (true) {
                        int pos = doc.getText(0, doc.getLength()).indexOf(symbol);
                        if (pos < 0) {
                            break;
                        }
                        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/emo/" + icon + ".png")));
                        StyleConstants.setComponent(labelStyle, label);
                        doc.remove(pos, symbol.length());
                        doc.insertString(pos, "emo", labelStyle);
                        
                    }
//                    this.txtContent.setCaretPosition(doc.getLength());
                }
                sleep(50);
            } catch (Exception e) {
            }

        }
    }
}
