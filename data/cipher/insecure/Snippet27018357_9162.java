package crypt;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This program imports exports encrypts and decrypts any text file.
 * The known bugs are that you can't reuse exported and imported files
 * outside of the current application session.
 * This may be cause by the cipher having different random values in place
 * when it restarts the program. 2 ciphers are never the same.
 * This encryption application is nearly impossible to decrypt.
 * Yet do to the lack of being able to reuse the same cipher over 2 applications
 * the program is all but useless.
 * @author qward
 *
 */
@SuppressWarnings("serial")
public class Crypt extends JFrame {

    final private Cipher cipher = Cipher.getInstance("AES");

    /**
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public Crypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        super("Cypt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // sets one cipher for encode and decode

        // Initiating Components
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System
                .getProperty("user.home")));
        JPanel left = new JPanel();
        JPanel right = new JPanel();
        JPanel bottom = new JPanel();
        JPanel top = new JPanel();
        left.setLayout(new BorderLayout());
        right.setLayout(new BorderLayout());
        bottom.setLayout(new FlowLayout());
        top.setLayout(new FlowLayout());
        JLabel inputLabel = new JLabel("Input");
        JLabel outputLabel = new JLabel("Output");
        JButton generateKey = new JButton("Generate Key");
        final JTextArea input = new JTextArea(20, 25);
        final JTextArea output = new JTextArea(20, 25);
        JScrollPane inputScroll = new JScrollPane(input);
        JScrollPane outputScroll = new JScrollPane(output);
        final JTextField key = new JTextField(20);
        JButton encode = new JButton("encode");
        JButton decode = new JButton("decode");
        JButton swap = new JButton("Swap");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = export(fileChooser, output);
        JMenuItem imports = imports(fileChooser, input);
        file.add(imports);
        file.add(export);
        bar.add(file);

        // Position Components
        bottom.add(generateKey);
        bottom.add(key);
        bottom.add(encode);
        bottom.add(decode);
        top.add(swap);
        left.add(inputScroll, BorderLayout.CENTER);
        left.add(inputLabel, BorderLayout.NORTH);
        right.add(outputScroll, BorderLayout.CENTER);
        right.add(outputLabel, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        setJMenuBar(bar);

        // Add events to buttons
        encode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (key.getText().length() == 16) {
                    output.setText(encrypt(input.getText(), new SecretKeySpec(
                            key.getText().getBytes(), "AES"), cipher));
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please input a valid 16 character key");
                }
            }
        });
        generateKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                key.setText(generateKey());
            }
        });
        decode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (key.getText().length() == 16) {
                    output.setText(decrypt(input.getText(), new SecretKeySpec(
                            key.getText().getBytes(), "AES"), cipher));
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please input a valid 16 character key");
                }
            }
        });
        swap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String temp = input.getText();
                input.setText(output.getText());
                output.setText(temp);
            }
        });

        // begin
        pack();
        setVisible(true);
    }

    /**
     * @param fileChooser
     * @param output
     * @return
     */
    private JMenuItem export(final JFileChooser fileChooser,
            final JTextArea output) {
        JMenuItem export = new JMenuItem("Export");
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int result = fileChooser.showOpenDialog(Crypt.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
                        PrintWriter out = null;
                        try {
                            out = new PrintWriter(selectedFile
                                    .getAbsolutePath());
                            out.print(new String(cipher.doFinal(output
                                    .getText().getBytes())));
                        } catch (FileNotFoundException
                                | IllegalBlockSizeException
                                | BadPaddingException e) {
                            out.print(output.getText());
                        } finally {
                            out.close();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please Select a .txt File");
                    }
                }
            }
        });
        return export;
    }

    /**
     * @param fileChooser
     * @param input
     * @return
     */
    private JMenuItem imports(final JFileChooser fileChooser,
            final JTextArea input) {
        JMenuItem imports = new JMenuItem("Import");
        imports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int result = fileChooser.showOpenDialog(Crypt.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
                        Scanner in = null;
                        try {
                            in = new Scanner(new File(selectedFile
                                    .getAbsolutePath()));
                            String str = "";
                            while (in.hasNextLine()) {
                                str += in.nextLine() + "\n";
                            }
                            byte[] encrypted = cipher.doFinal(input.getText()
                                    .getBytes());
                            input.setText(new String(encrypted, "ISO-8859-1"));
                        } catch (Exception e) {
                            try {
                                in = new Scanner(new File(selectedFile
                                        .getAbsolutePath()));
                                String str = "";
                                while (in.hasNextLine()) {
                                    str += in.nextLine() + "\n";
                                }
                                input.setText(new String(str));
                            } catch (FileNotFoundException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        } finally {
                            in.close();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Please Select a .txt File");
                    }
                }
            }
        });
        return imports;
    }

    /**
     * @return
     */
    private String generateKey() {
        try {
            SecureRandom random = new SecureRandom();
            return new String(random.generateSeed(16), "ISO-8859-1");
        } catch (UnsupportedEncodingException u) {

        }
        return "";
    }

    /**
     * @param input
     * @param key
     * @param cipher
     * @return
     */
    private String encrypt(String input, Key key, Cipher cipher) {

        try {
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(input.getBytes());

            return new String(encrypted, "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param input
     * @param key
     * @param cipher
     * @return
     */
    private String decrypt(String input, Key key, Cipher cipher) {
        try {
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, key);
            String decrypted = new String(cipher.doFinal(input
                    .getBytes("ISO-8859-1")));

            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        new Crypt();
    }
}
