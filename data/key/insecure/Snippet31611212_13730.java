package com.example.dell_pc.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class MainActivity extends ActionBarActivity {
    Intent intent;
    int CAPTURE_IMAGE = 100;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    Uri capturedImageUri;
    public String TAG = "Tag";
    Bitmap bitmap = null;
    public String imagename = "";
    File directory, directory1;
    int value, value1, value2 = 0;
    Random rand;
    String final_string = "";
    String final_string1 = "";
    String final_string2 = "";
    SharedPreferencesClass spc;
    String var = "", serverstr = "";
    String st = "";
    File sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spc = new SharedPreferencesClass(getApplicationContext());
        //spc.clearsharedpreference();
        var = spc.getdata();

        rand = new Random();
        if (var == null) {
            generatekey();
            spc.putdata(final_string);
        }
    //System.out.println("is rooted : " + isRooted());
        createdirectory_internalmemory("subdirectory");
        ////createfile_internalmemory("myfirstfile1.txt");
        ////create_writefile_internalmemory("myfirstfile1.txt");
        ////readtextfile_internalmemory("myfirstfile1.txt");

        setContentView(R.layout.activity_main);
    }

    /**
     * *method to create directory in internal storage**
     */
    public void createdirectory_internalmemory(String directory_name) {

        sub = new File(getFilesDir(), directory_name);
        if (!sub.exists())
            sub.mkdirs();
    }

    /**
     * *method to simply create an empty file in internal storage***
     */
    public void createfile_internalmemory(String filename) {
        //File f = new File(sub.getAbsolutePath() + "/myfirstfile1.txt");
        File f = new File(sub.getAbsolutePath(),  "/myfirstfile1.txt");
        try {
            if (!f.exists())
                f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * *method to create as well as write in a file in internal storage***
     */
    public void create_writefile_internalmemory(String filename) {
        File f = new File(sub.getAbsolutePath() + filename);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(f.getAbsolutePath());
            outputStream.write("Hello There My Name is Rahul12345".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * *method to read a text file from internal memory***
     */
    public void readtextfile_internalmemory(String filename) {
        File f = new File(sub.getAbsolutePath() + filename);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            System.out.println("text is: " + text);
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }   


    public void opencamera(View v) {
        switch (v.getId()) {
            case R.id.opencamera_button:
                Calendar cal = Calendar.getInstance();
                imagename = cal.getTimeInMillis() + ".jpg";
                declareimagepath();
                takepicture();
                break;

            case R.id.showimages_button:
                showallimages();
                break;
        }
    }

    public void declareimagepath() {

        File file = new File(path, imagename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(file);

    }

    public void takepicture() {

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(i, CAPTURE_IMAGE);
    }

    public void compressandsave() {
        OutputStream output;
        try {
            File file = new File(sub, imagename);

            output = new FileOutputStream(file);
            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deletefile() {
        String filenames[] = directory.list();

        if (filenames.length == 0) {
            directory.delete();
        } else {
            for (int i = 0; i < filenames.length; i++) {
                if (filenames[i].equals(imagename)) {
                    new File(directory, filenames[i]).delete();
                }
            }
        }
    }

    public void encrypt() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        File file = new File(directory,imagename);
        // Here you read the cleartext.

        FileInputStream fis = new FileInputStream(directory.getAbsolutePath() + "/" + imagename);

        // This stream write the encrypted text. This stream will be wrapped by another stream.
                FileOutputStream fos = new FileOutputStream(directory.getAbsolutePath() + "/enc_"+imagename);

        // Length is 16 byte
        // Careful when taking user input!!! http://stackoverflow.com/a/3452620/1188357
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");

        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, sks);

        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        // Write bytes
        int b;

        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    public void showallimages() {
        intent = new Intent(this, ImageActivity.class);
        intent.putExtra("directorypath",sub.getAbsolutePath());
        startActivity(intent);
    }

    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("running safe in activityResult : "+requestCode+"  "+CAPTURE_IMAGE);
        if (requestCode == CAPTURE_IMAGE) {

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);

                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("running safe : "+resultCode+"  "+RESULT_OK);
            if (resultCode == RESULT_OK) {

                //compressandsave();
                String p = saveToInternalSorage(bitmap);
                new File(capturedImageUri.getPath()).delete();


                try {
                    encrypt();                    
                    new File(directory, imagename).delete();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                loadImageFromStorage(p);

            }
        }
    }

    private String saveToInternalSorage(Bitmap bitmapImage){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,imagename);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);           
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try {            
            File f=new File(path, "dec_"+imagename);
            FileInputStream fis = new FileInputStream(path+"/enc_"+imagename);

            FileOutputStream fos = new FileOutputStream(f);

            SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, sks);

            CipherInputStream cis = new CipherInputStream(fis, cipher);

            int b;
            byte[] d = new byte[8];
            while((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }

            fos.flush();
            fos.close();
            cis.close();


            //Bitmap bit = BitmapFactory.decodeByteArray(d,0,d.length);
            //Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            Bitmap bitm = BitmapFactory.decodeStream(new FileInputStream(f));

            ImageView img=(ImageView)findViewById(R.id.myimage);

            img.setImageBitmap(bitm);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
