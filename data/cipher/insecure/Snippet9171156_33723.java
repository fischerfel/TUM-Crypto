package com.filepermition.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AndroidFilePermitionActivity extends Activity 
{
    Button btn_button;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn_button = (Button)findViewById(R.id.btn_button);

        btn_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    FileInputStream fis = new FileInputStream(
                        new File("/mnt/sdcard/testfile/file.wav"));
                    File outfile = new File("/mnt/sdcard/testfile/encTest1234.wav");

                    int read;
                    if(!outfile.exists())
                        outfile.createNewFile();

                    File decfile = new File("/mnt/sdcard/testfile/dec123.wav");
                    if(!decfile.exists())
                        decfile.createNewFile();

                    FileOutputStream fos = new FileOutputStream(outfile);
                    FileInputStream encfis = new FileInputStream(outfile);
                    FileOutputStream decfos = new FileOutputStream(decfile);

                    Cipher encipher = Cipher.getInstance("AES");
                    Cipher decipher = Cipher.getInstance("AES");

                    KeyGenerator kgen = KeyGenerator.getInstance("AES");
                    SecretKey skey = kgen.generateKey();
                    encipher.init(Cipher.ENCRYPT_MODE, skey);
                    CipherInputStream cis = new CipherInputStream(fis, encipher);
                    decipher.init(Cipher.DECRYPT_MODE, skey);
                    CipherOutputStream cos = new CipherOutputStream(decfos,decipher);

                    while((read = cis.read())!=-1)
                    {
                        fos.write((char)read);
                        fos.flush();
                    }   
                    fos.close();
                    while((read=encfis.read())!=-1)
                    {
                        cos.write(read);
                        cos.flush();
                    }
                    cos.close();

                }catch (Exception e) {
                    // TODO: handle exceptione
                    e.printStackTrace();
                }
            }
        });
    }
}
