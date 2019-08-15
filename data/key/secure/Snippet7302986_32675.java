import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

public class AESGUI extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame("AES Encryption");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600,300));

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        AESGUI p = new AESGUI();

        frame.getContentPane().add(p);
        frame.pack();
        frame.setVisible(true);
    }

    private JTextField in;
    private JTextArea out;

    public AESGUI() {
        JLabel info = new JLabel("Type any String");
        in = new JTextField(20);
        JButton encrypt = new JButton("Encrypt");
        out = new JTextArea(10,40);

        out.setEditable(false);

        encrypt.addActionListener(new encryptListener());
        in.addActionListener(new encryptListener());

        add(info);
        add(in);
        add(encrypt);
        add(out);
        add(new JScrollPane(out));
    }

    private class encryptListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String data = in.getText();
            if (data.length() == 0) { }
            else
                try {
                    String en = encrypt(data);
                    out.append("Encrypted string: " + en + "\n");
                    out.append("Original String: " + decrypt(en) + "\n\n");
                } catch(Exception ex) { }
        }
    }

    public String asHex(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    private SecretKeySpec skeySpec;
    private Cipher cipher;
    private byte[] encrypted;

    public String encrypt(String str) throws Exception {
        // Get the KeyGenerator
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128); // 192 and 256 bits may not be available

        // Generate the secret key specs.
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        skeySpec = new SecretKeySpec(raw, "AES");

        // Instantiate the cipher
        cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        encrypted = cipher.doFinal(str.getBytes());
        return asHex(encrypted);
    }

    public String decrypt(String str) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original);
        return originalString;
    }

}
