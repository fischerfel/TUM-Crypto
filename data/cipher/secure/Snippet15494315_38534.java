import java.awt.*;
import javax.swing.JFileChooser;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

public class AES extends java.applet.Applet {
    Label encry = new Label("Choose Algorithm:   ");
    Choice textAlignment = new Choice();
    TextField TextFieldName = new TextField("", 10);
    TextField TextFieldName2 = new TextField("", 10);

    Image I;
    Label label;
    Label label2;
    private static JFileChooser fc;

    // for AES
    Cipher ecipher;
    Cipher dcipher;

    // for BlowFish
    private static String cipher_key = "Blowfish";  
    private static Cipher cipher;  
    private static KeyGenerator k_generator;  
    private static SecretKey secret_key; 

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

  //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
             elapsed = (System.currentTimeMillis() - startTime);
        }
        else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }    

    private long startTime2 = 0;
    private long stopTime2 = 0;
    private boolean running2 = false;

    public void start2() {
        this.startTime2 = System.currentTimeMillis();
        this.running2 = true;
    }


    public void stop2() {
        this.stopTime2 = System.currentTimeMillis();
        this.running2 = false;
    }

  //elaspsed time in milliseconds
    public long getElapsedTime2() {
        long elapsed;
        if (running2) {
             elapsed = (System.currentTimeMillis() - startTime2);
        }
        else {
            elapsed = (stopTime2 - startTime2);
        }
        return elapsed;
    }    


    public AES(SecretKey key)
    {
        // Create an 8-byte initialization vector
        byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
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
        static byte[] buf = new byte[1024];

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

        // for BlowFish encryption
        public void encrypt_data(InputStream in, OutputStream out) throws InvalidKeyException, IOException
        {
            //initialize the cipher  for decryption  
               cipher.init(Cipher.ENCRYPT_MODE, secret_key);  

            // Bytes written to out will be encrypted
                out = new CipherOutputStream(out, cipher);

                // Read in the cleartext bytes and write to out to encrypt
                int numRead = 0;
                while ((numRead = in.read(buf)) >= 0)
                {
                    out.write(buf, 0, numRead);
                }
                out.close();
        }

        //for BlowFish decryption
         public void decrypt_data(InputStream in, OutputStream out) throws InvalidKeyException,  IOException {

             // initialize the cipher once again
              cipher.init(Cipher.DECRYPT_MODE, secret_key);      

                // Bytes read from in will be decrypted
                in = new CipherInputStream(in, cipher);


                int numRead = 0;
                while ((numRead = in.read(buf)) >= 0)
                {
                    out.write(buf, 0, numRead);
                }
                out.close();
         }



         public AES() {
                setLayout(new FlowLayout());
                //simpleMethod();
         } 

    public void init() {

                I=getImage(getCodeBase(),"lock.png");
                add(encry);
                label = new Label("Encryption Time ", Label.RIGHT);
                label2 = new Label("Decryption Time ", Label.RIGHT);

        //Add 4 different options to it:
                textAlignment.add("AES");
                textAlignment.add("BlowFish");
                textAlignment.add("PBE");

        //Then add the drop down list to the screen
                add(textAlignment);
                add(label);
                add(TextFieldName);
                add(label2);
                add(TextFieldName2);

        }


        //AES button caused the event
        public boolean action(Event event, Object object) {
             if (event.target == textAlignment) {
                  String selection = textAlignment.getSelectedItem();


                      if (fc == null) {
                            fc = new JFileChooser(".");
                        }
                        // Show it.
                        int returnVal = fc.showOpenDialog(this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) { 


                                  if (selection.equals("AES")){
                                       AES s1 = new AES();

                             s1.start(); // timer start
                            KeyGenerator kgen;
                            try {
                                kgen = KeyGenerator.getInstance("AES");

                                kgen.init(128);
                                SecretKey key           =   kgen.generateKey();

                                // Create encrypter/decrypter class
                                // Create encrypter/decrypter class
                                AES encrypter = new AES(key);

                                    try {
                                        encrypter.encrypt(new FileInputStream(fc.getSelectedFile().getPath()),new FileOutputStream("C:/Users/Acer/Desktop/encrypted"));
                                    s1.stop(); // timer stops

                                    } catch (FileNotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                       AES s2 = new AES();
                                    // Decrypt
                                    try {
                                        s2.start2(); // timer starts
                                        encrypter.decrypt(new FileInputStream("C:/Users/Acer/Desktop/encrypted"),new FileOutputStream("C:/Users/Acer/Desktop/decrypted"));
                                        s2.stop2(); // timer stops

                                    } catch (FileNotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    TextFieldName.setText(Long.toString(s1.getElapsedTime())); 
                                    TextFieldName2.setText(Long.toString(s2.getElapsedTime2())); }

                             catch (NoSuchAlgorithmException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }               
                    }
    }
                             if (selection.equals("BlowFish")){
                                   AES s1 = new AES();
                                   AES s2 = new AES();

                                     AES encryption = new AES(secret_key);


                                     s1.start(); // timer start
                                   //Generate encryption key based on codecypherkey code word  
                                   try {
                                    k_generator = KeyGenerator.getInstance(cipher_key);
                                } catch (NoSuchAlgorithmException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }  

                                   //Create a secret key  
                                   secret_key = k_generator.generateKey();  


                                   //Create Cipher based on the previous encryption key code  
                                   try {
                                    cipher = Cipher.getInstance(cipher_key);
                                } catch (NoSuchAlgorithmException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (NoSuchPaddingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } 

                                try {
                                    encryption.encrypt_data(new FileInputStream(fc.getSelectedFile().getPath()),new FileOutputStream("C:/Users/Acer/Desktop/encrypted"));
                                    s1.stop(); // timer stops
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                try {
                                       s2.start();
                                    encryption.decrypt_data(new FileInputStream("C:/Users/Acer/Desktop/encrypted"),new FileOutputStream("C:/Users/Acer/Desktop/decrypted"));
                                        s2.stop();

                                        TextFieldName.setText(Long.toString(s1.getElapsedTime())); 
                                        TextFieldName2.setText(Long.toString(s2.getElapsedTime2())); 

                                        } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                }




                             }

            return running;
            }


        public void paint(Graphics g)
        {
        g.drawImage(I,10,10,this);
        }}
