  package com.example.aesandroidsecurity;

    import java.security.NoSuchAlgorithmException;
    //import java.security.spec.AlgorithmParameterSpec;

    import javax.crypto.Cipher;
    import javax.crypto.NoSuchPaddingException;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

    import android.os.Bundle;
    import android.app.Activity;
    import android.view.Menu;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    public class MainActivity extends Activity {

    private Cipher cipher;
    private SecretKeySpec keyspec;
    private IvParameterSpec ivspec;
    private String iv = "fedcba9876543210";
    private String SecretKey = "1234567890123456";  

    Button encrypt = (Button)findViewById(R.id.button1);
    Button decrypt = (Button)findViewById(R.id.button2);    
    EditText data = (EditText)findViewById(R.id.button2);
    String plainText = data.getText().toString();

    byte[] encrypted = null;
    byte[] decrypted = null;    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //byte[] keyBytes = new byte[32];

        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");                    
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try{
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            encrypted = cipher.doFinal(plainText.getBytes());           
        }       
        catch(Exception e)
        {
            e.printStackTrace();
        }

        encrypt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), ""+encrypted, 
Toast.LENGTH_LONG).show();
            }
        });     

        decrypt.setOnClickListener(new View.OnClickListener() {         
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try{
                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                    decrypted = cipher.doFinal(encrypted);
                }       
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), ""+decrypted, 
Toast.LENGTH_LONG).show();
            }
        });     
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is 
present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    }
