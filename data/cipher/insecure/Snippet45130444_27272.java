import javax.crypto.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Test");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton button = new JButton("Encrypt");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    String filepath = selectedFile.getAbsolutePath();
                    try {
                        encrypt(filepath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void encrypt(String path) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
                KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
                SecretKey myDesKey = keygenerator.generateKey();
                Cipher desalgCipher;
                desalgCipher = Cipher.getInstance("AES");
                desalgCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

                try (InputStream is = Files.newInputStream(Paths.get(path));
                     CipherInputStream cipherIS = new CipherInputStream(is, desalgCipher);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(cipherIS));) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            }
        });

        frame.getContentPane().add(button);
        frame.setSize(300, 400);
        frame.setVisible(true);
    }
}
