  import java.io.BufferedReader;
 import java.io.File;
     import java.io.FileInputStream;
     import java.io.FileOutputStream;
     import java.io.FilterWriter;
     import java.io.InputStream;
     import java.io.InputStreamReader;

     import javax.crypto.Cipher;
     import javax.crypto.CipherInputStream;
     import javax.crypto.KeyGenerator;
     import javax.crypto.SecretKey;

 import android.os.Bundle;
 import android.app.Activity;
 import android.view.Menu;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.Button;
 import android.widget.EditText;
 import org.apache.commons.codec.binary.Base64;

  public class EncryptionTest1 extends Activity {
EditText output, outputDecrypt;
EditText input;
String plainData = "";
String cipherText, decryptedText;
KeyGenerator keyGen;
SecretKey secretKey;

Cipher aesCipher;
FileOutputStream fos;

byte[] byteDataToEncrypt, byteCipherText, byteDecryptedText;
byte[] xmlStream;

@Override
   protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_encryption_test1);
    input = (EditText) findViewById(R.id.text_inputText1);
    output = (EditText) findViewById(R.id.text_Result1);
    outputDecrypt = (EditText) findViewById(R.id.text_decrypt_Result1);

    Button btn_encrypt = (Button) findViewById(R.id.btn_encrpyt1);

    btn_encrypt.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                plainData = input.getText().toString();
                System.out.println("input==>>" + plainData);
                byte[] fileStreams = fileOpening("SaleReport.xml");
                byte[] DataEncrypt = encrypt(fileStreams);
                String DataDecrypt = decrypt(DataEncrypt);

            System.out.println("Decrypted Text:===>>" + DataDecrypt);
                outputDecrypt.setText(DataDecrypt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    });
  }

private byte[] fileOpening(String fileName) throws Exception {
    InputStream is = getAssets().open(fileName);
    int size = is.available();
    xmlStream = new byte[size];
    is.read(xmlStream);
    System.out.println("xmlstream length==>>" + xmlStream.length);
    return xmlStream;
}

private byte[] encrypt(byte[] xmlStream) throws Exception {

    keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(128);
    secretKey = keyGen.generateKey();
    aesCipher = Cipher.getInstance("AES");
    aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
    // byteDataToEncrypt = plainData.getBytes();

    byteCipherText = aesCipher.doFinal(xmlStream);
    cipherText = new String(new Base64().encodeBase64(byteCipherText));
    output.setText(cipherText);
    System.out.println(cipherText);

    return byteCipherText;

}

public String decrypt(byte[] DataEncrypt) throws Exception {
    aesCipher.init(Cipher.DECRYPT_MODE, secretKey,
    aesCipher.getParameters());
    byteDecryptedText = aesCipher.doFinal(DataEncrypt);
    decryptedText = new String(byteDecryptedText);
    return decryptedText;
  }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.encryption_test1, menu);
    return true;
}

   }
