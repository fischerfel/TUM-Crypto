import java.awt.Container;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class BrowseListener extends JFrame {

private JButton btn ;
private JTextField textField;
private JTextField textF;
private static JFileChooser fc;
Cipher ecipher;
Cipher dcipher;

private long startTime = 0;
private long stopTime = 0;
private boolean running = false;

public void start() {
    this.startTime = System.currentTimeMillis();
    this.running = true;
}


public void stop() {
    this.stopTime = System.currentTimeMillis();
    this.running = false;
}

//elaspsed time in seconds
public long getElapsedTimeSecs() {
    long elapsed;
    if (running) {
        elapsed = ((System.currentTimeMillis() - startTime) / 1000);
    }
    else {
        elapsed = ((stopTime - startTime) / 1000);
    }
    return elapsed;
}

public BrowseListener(SecretKey key)
{
    // Create an 8-byte initialization vector
    byte[] iv = new byte[]
    {
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
0x07, 0x08, 0x09,0x0a,            0x0b, 0x0c,
0x0d, 0x0e, 0x0f
    };

    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
    try
    {
        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // CBC requires an initialization vector
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

// Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[1024];

    public void encrypt(InputStream in, OutputStream out)
    {
        try
        {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0)
            {
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e)
        {
        }
    }

    public void decrypt(InputStream in, OutputStream out)
    {
        try
        {
            // Bytes read from in will be decrypted
            in = new CipherInputStream(in, dcipher);

        // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0)
            {
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e)
        {
        }
    }

public BrowseListener() {
    initComponents();
    simpleMethod();
}

private void initComponents() {
    setSize(300, 60);
    setTitle("My Window");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container c = getContentPane();
    c.setLayout(new FlowLayout());
    btn = new JButton("Browse");
    btn.setPreferredSize(new Dimension(100, 20));
    c.add(btn);
    textField = new JTextField();
    textField.setPreferredSize(new Dimension(160, 20));
    c.add(textField);
    textF = new JTextField();
    textF.setPreferredSize(new Dimension(160, 20));
    c.add(textF);
}

private void simpleMethod() {
    btn.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                btnBrowseActionPerformed(evt);
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}

private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt)
throws   NoSuchAlgorithmException, FileNotFoundException {                                               
    if (fc == null) {
        fc = new JFileChooser(".");
    }
    // Show it.
    int returnVal = fc.showOpenDialog(this);

    // Process the results.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        textField.setText(fc.getSelectedFile().getPath());

        KeyGenerator    kgen    =   KeyGenerator.getInstance("AES");
        kgen.init(128);
        SecretKey key           =   kgen.generateKey();

        // Create encrypter/decrypter class
        BrowseListener encrypter = new BrowseListener(key);


       BrowseListener s = new BrowseListener();

        s.start(); // timer starts
        encrypter.encrypt(new FileInputStream(fc.getSelectedFile().getPath()),
new  FileOutputStream("C:/Users/Acer/Desktop/encrypted"));
        // Decrypt
        encrypter.decrypt
(new FileInputStream("C:/Users/Acer/Desktop/encrypted"),
new FileOutputStream("C:/Users/Acer/Desktop/decrypted"));
        s.stop(); // timer stops

        textF.setText(Long.toString(s.getElapsedTimeSecs()));

    } else {
        textField.setText("");
    }

    // Reset the file chooser for the next time it's shown.
    fc.setSelectedFile(null);

}  
public static void main(String[] args) throws 
NoSuchAlgorithmException, FileNotFoundException {

    BrowseListener s = new BrowseListener();
    BrowseListener window = new BrowseListener();
    window.setVisible(true);
}
}
