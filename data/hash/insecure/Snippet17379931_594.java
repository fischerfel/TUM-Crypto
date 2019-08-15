import org.apache.commons.codec.binary.Hex;

import javax.swing.*;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result){
            File file = chooser.getSelectedFile();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            try (InputStream is = new FileInputStream(file)) {
                DigestInputStream dis = new DigestInputStream(new BufferedInputStream(is), digest);
                while (dis.read() != -1){}
            }
            JOptionPane.showMessageDialog(null, Hex.encodeHexString(digest.digest()));
        }
    }
}
