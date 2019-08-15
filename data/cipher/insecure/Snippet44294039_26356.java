import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.image.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESGui extends Application{

    TextArea inputArea = new TextArea();
    TextArea outputArea = new TextArea();
    Main object1 = new Main();

    public static void main(String [] args){
        launch (args);
    }


    @Override
    public void start(Stage stage){

        HBox hbox1 = new HBox(50);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().addAll(inputArea, outputArea);

        Button encrypt = new Button("Encrypt");
        encrypt.setOnAction (event -> {
            object1.aesEncryption();
        });

        Button decrypt = new Button("Decrypt");

        HBox hbox2 = new HBox(50);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(encrypt, decrypt);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hbox1, hbox2);

        Scene scene = new Scene(vbox, 900, 700);

        stage.setScene(scene);
        stage.setTitle("aes gui");
        stage.show();
    }

    public class AESAlgorithm{
        public String algo = "AES";
        public byte[] keyValue;

        public AESAlgorithm(String key){
            keyValue = key.getBytes();
        }

        public Key generateKey() throws Exception{
            Key key = new SecretKeySpec(keyValue, algo);
            return key;
        }

        public String encrypt(String msg) throws Exception{

            Key key = generateKey();
            Cipher c = Cipher.getInstance(algo);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(msg.getBytes());
            String encryptedValue = new BASE64Encoder().encode(encVal);
            return encryptedValue;

        }

        public String decrypt(String msg) throws Exception{

            Key key = generateKey();
            Cipher c = Cipher.getInstance(algo);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(msg);
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        }
    }

    public class Main {
        AESAlgorithm aesAlgo;
        private byte[] keyValue = new byte[]{'t','h','e','b','e','s','t','s','e','c','r','e','t','k','e','y'};
        public Main(){
            //initComponents();
            //here we passed key to constructor
            aesAlgo = new AESAlgorithm("MySecretKey");
        }

        private void aesEncryption(){
            try{

                String plainText = inputArea.getText();
                String encryptedText = aesAlgo.encrypt(plainText);
                outputArea.setText(encryptedText);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}
