package com.example.megacodownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.MegaCrypt;
import org.MegaFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity  implements View.OnClickListener{
    int timerimas = 0;
    double speed;
    int file_sizes;
    String file_name;
    int dimrim = 0;
    float Percent;
    private String email, password, sid;
    private int sequence_number;
    private long[] master_key;
    private BigInteger[] rsa_private_key;
    private long[] password_aes;
    HashMap<String, long[]> user_keys = new HashMap<String, long[]>();
    static final String MY_PATH = "/MegaDownload";
    File pathsd = Environment.getExternalStorageDirectory();
    File directory = new File(pathsd.getAbsolutePath() + MY_PATH);
    String pathfinal;
    public void prepare_download(){
        if(!directory.exists())
            directory.mkdir();
        pathfinal = directory.getAbsolutePath()+"/";

    }
    Button btn_download;
    EditText txt_url;
    TextView txt_cartella;

    public void download(String url, String path) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException, JSONException, InterruptedException {

        print("Download started");
        Toast.makeText(this,"Download Iniziato",Toast.LENGTH_SHORT).show();

        String[] s = url.split("!");
        String file_id = s[1];
        byte[] file_key = MegaCrypt.base64_url_decode_byte(s[2]);

        int[] intKey = MegaCrypt.aByte_to_aInt(file_key);
        JSONObject json = new JSONObject();
        try {
            try {
                json.put("a", "g");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json.put("g", "1");
            json.put("p", file_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject file_data;
        file_data = new JSONObject(api_request(json.toString()));

        int[] keyNOnce = new int[]{intKey[0] ^ intKey[4], intKey[1] ^ intKey[5], intKey[2] ^ intKey[6], intKey[3] ^ intKey[7], intKey[4], intKey[5]};
        byte[] key = MegaCrypt.aInt_to_aByte(keyNOnce[0], keyNOnce[1], keyNOnce[2], keyNOnce[3]);

        int[] iiv = new int[]{keyNOnce[4], keyNOnce[5], 0, 0};
        byte[] iv = MegaCrypt.aInt_to_aByte(iiv);

        @SuppressWarnings("unused")
        int file_size = file_data.getInt("s");
        file_sizes = (((file_size) / 1024) / 1024);
       // filesize.setText("Dimensione file:"+(file_sizes) + "MB");
       // filesize.setVisible(true);

        String attribs = (file_data.getString("at"));
        attribs = new String(MegaCrypt.aes_cbc_decrypt(MegaCrypt.base64_url_decode_byte(attribs), key));

        file_name = attribs.substring(10, attribs.lastIndexOf("\""));
        print(file_name);
        //filename.setText("Nome File:" + file_name);
        //filename.setVisible(true);
        final IvParameterSpec ivSpec = new IvParameterSpec(iv);
        final SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/nopadding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        InputStream is = null;
        String file_url = file_data.getString("g");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + File.separator + file_name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final OutputStream cos = new CipherOutputStream(fos, cipher);
        final Cipher decipher = Cipher.getInstance("AES/CTR/NoPadding");
        decipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
        int read = 0;
        final byte[] buffer = new byte[32767];
        try {
            HttpURLConnection urlConn = (HttpURLConnection) new URL(file_url).openConnection();
            urlConn.setRequestProperty("Content-length" , "0");
            urlConn.setAllowUserInteraction(false);
            urlConn.setRequestMethod("POST");
            urlConn.connect();

            print(file_url);
            is = urlConn.getInputStream();

            long start = System.nanoTime();
            long totalRead = 0;
            final double NANOS_PER_SECOND = 1000000000.0;
            final double BYTES_PER_MIB = 1024 * 1024;

            while ((read = is.read(buffer, 0, 1024)) > 0) {

                totalRead += read;
                cos.write(buffer, 0, read);
                int ore,minuti,secondi;
                double speed;
                speed = NANOS_PER_SECOND / BYTES_PER_MIB * totalRead / (System.nanoTime() -  start + 1);
                dimrim = ((((file_sizes * 1024) * 1024) - ((int) (totalRead))) / 1024) / 1024; //

                DecimalFormat df = new DecimalFormat("#0.###");
                //dwspd.setText("Velocità Download:" + df.format(speed) + "Mbyte/s");
               // dimrimasta.setText("Dimensione Rimanente:" + dimrim + "MB");
                //numlistrim.setText("File Rimanenti da Scaricare:"+ numrim);
                timerimas = (int) ((dimrim ) / speed);
                ore = timerimas / 3600;
                minuti = (timerimas % 3600) / 60;
                secondi = timerimas - (ore * 3600) - (minuti * 60);
               // timerimasto.setText("Tempo Rimanente:"+ore+"H| "+ minuti +"M| " + secondi+"S ");
                Percent = (((totalRead) * 100) / (file_sizes));
                System.out.println(((Percent / 1024) / 1024));
                //pbFile.setValue((int) ((Percent / 1024) / 1024));
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                cos.close();
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        print("Download finished");

    }

    public static boolean isInteger(String string) {
        if (string == null || string.isEmpty()) {
            return false;
        }
        int length = string.length();
        int i = 0;
        if (string.charAt(i) == '[') {
            if (length == 1) {
                return false;
            }
            i++;
        }
        if (string.charAt(i) == '-') {
            if (length == 1 + i) {
                return false;
            }
            i++;
        }
        for (; i < length; i++) {
            char c = string.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public String api_request(String data) {
        HttpURLConnection connection = null;
        try {
            String urlString = "https://g.api.mega.co.nz/cs?id=" + sequence_number;
            if (sid != null) {
                urlString += "&sid=" + sid;
            }

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); //use post method
            connection.setDoOutput(true); //we will send stuff
            connection.setDoInput(true); //we want feedback
            connection.setUseCaches(false); //no caches
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "text/xml");

            OutputStream out = connection.getOutputStream();
            try {
                OutputStreamWriter wr = new OutputStreamWriter(out);
                wr.write("[" + data + "]"); //data is JSON object containing the api commands
                wr.flush();
                wr.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally { //in this case, we are ensured to close the output stream
                if (out != null) {
                    out.close();
                }
            }

            InputStream in = connection.getInputStream();
            StringBuffer response = new StringBuffer();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                }
                rd.close(); //close the reader
            } catch (IOException e) {
                e.printStackTrace();
            } finally {  //in this case, we are ensured to close the input stream
                if (in != null) {
                    in.close();
                }
            }

            return response.toString().substring(1, response.toString().length() - 1);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public ArrayList<MegaFile> get_files() throws UnsupportedEncodingException {
        JSONObject json = new JSONObject();
        try {
            json.put("a", "f");
            json.put("c", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String files = api_request(json.toString());
        // TODO check for negativ error
        //print(json.toString());
        ArrayList<MegaFile> megaFiles = new ArrayList<MegaFile>();

        JSONArray array = null;
        try {
            json = new JSONObject(files);
            array = json.getJSONArray("f");
            for (int i = 0; i < array.length(); i++) {
                //print(array.get(i).toString());
                megaFiles.add(process_file(new JSONObject(array.get(i).toString())));

            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return megaFiles;
    }

    private MegaFile process_file(JSONObject jsonFile) throws UnsupportedEncodingException {

        MegaFile file = new MegaFile();
        try {

            if (jsonFile.getInt("t") < 2) {

                String key = "";
                String uid = jsonFile.getString("u");
                String h = (jsonFile.getString("h"));
                file.setUID(uid);
                file.setHandle(h);
                //print (h);
                if (jsonFile.getString("k").contains("/")) {
                    String[] keys = jsonFile.getString("k").split("/");
                    int start = keys[0].indexOf(":") + 1;
                    key = keys[0].substring(start);

                }

                String attributes = MegaCrypt.base64_url_decode(jsonFile.getString("a"));

                long[] k = new long[4];
                if (!key.isEmpty()) {
                    long[] keys_a32 = MegaCrypt.decrypt_key(MegaCrypt.base64_to_a32(key), master_key);
                    if (jsonFile.getInt("t") == 0) {

                        k[0] = keys_a32[0] ^ keys_a32[4];
                        k[1] = keys_a32[1] ^ keys_a32[5];
                        k[2] = keys_a32[2] ^ keys_a32[6];
                        k[3] = keys_a32[3] ^ keys_a32[7];


                    } else {
                        k[0] = keys_a32[0];
                        k[1] = keys_a32[1];
                        k[2] = keys_a32[2];
                        k[3] = keys_a32[3];
                        file.setDirectory(true);

                    }

                    file.setKey(k);
                    file.setAttributes(MegaCrypt.decrypt_attr(attributes, k));
                } else if (!jsonFile.isNull("su") && !jsonFile.isNull("sk") && jsonFile.getString("k").contains(":")) {
                    long[] keyS;

                    user_keys.put(jsonFile.getString("u"), MegaCrypt.decrypt_key(MegaCrypt.base64_to_a32(jsonFile.getString("sk")), master_key));
                    //print("ShareKey->"+jsonFile.getString("sk"));
                    int dd1 = jsonFile.getString("k").indexOf(':');
                    String sk = jsonFile.getString("k").substring(dd1 + 1);

                    keyS = MegaCrypt.decrypt_key(MegaCrypt.base64_to_a32(sk), user_keys.get(jsonFile.getString("u")));
                    if (jsonFile.getInt("t") == 0) {
                        long[] keys_a32S = keyS;
                        k[0] = keys_a32S[0] ^ keys_a32S[4];
                        k[1] = keys_a32S[1] ^ keys_a32S[5];
                        k[2] = keys_a32S[2] ^ keys_a32S[6];
                        k[3] = keys_a32S[3] ^ keys_a32S[7];
                    } else {
                        k = keyS;
                        file.setDirectory(true);
                    }

                    file.setKey(k);
                    file.setAttributes(MegaCrypt.decrypt_attr(attributes, k));

                } else if (!jsonFile.isNull("u") && jsonFile.getString("k").contains(":") && user_keys.containsKey(jsonFile.getString("u"))) {

                    int dd1 = jsonFile.getString("k").indexOf(':');
                    String sk = jsonFile.getString("k").substring(dd1 + 1);
                    //print(user_keys.get(jsonFile.getString("u")));
                    long[] keyS = MegaCrypt.decrypt_key(MegaCrypt.base64_to_a32(sk), user_keys.get(jsonFile.getString("u")));
                    if (jsonFile.getInt("t") == 0) {
                        long[] keys_a32S = keyS;
                        k[0] = keys_a32S[0] ^ keys_a32S[4];
                        k[1] = keys_a32S[1] ^ keys_a32S[5];
                        k[2] = keys_a32S[2] ^ keys_a32S[6];
                        k[3] = keys_a32S[3] ^ keys_a32S[7];
                    } else {
                        k = keyS;
                        file.setDirectory(true);
                    }

                    file.setKey(k);
                    file.setAttributes(MegaCrypt.decrypt_attr(attributes, k));

                } else if (!jsonFile.isNull("k")) {
                    int dd1 = jsonFile.getString("k").indexOf(':');
                    key = jsonFile.getString("k").substring(dd1 + 1);
                    long[] keys_a32S = MegaCrypt.decrypt_key(MegaCrypt.base64_to_a32(key), master_key);
                    if (jsonFile.getInt("t") == 0) {

                        k[0] = keys_a32S[0] ^ keys_a32S[4];
                        k[1] = keys_a32S[1] ^ keys_a32S[5];
                        k[2] = keys_a32S[2] ^ keys_a32S[6];
                        k[3] = keys_a32S[3] ^ keys_a32S[7];
                        file.setDirectory(true);


                    }/*else{
                     k = keys_a32S;

                     file.setDirectory(true);

                     }*/
                    file.setKey(k);

                    file.setAttributes(MegaCrypt.decrypt_attr(attributes, k));
                } else {
                    file.setAttributes(jsonFile.toString());
                }

            } else if (jsonFile.getInt("t") == 2) {
                file.setName("Cloud Drive");
            } else if (jsonFile.getInt("t") == 3) {
                file.setName("Cloud Inbox");
            } else if (jsonFile.getInt("t") == 4) {
                file.setName("Rubbish Bin");
            } else {
                file.setName(jsonFile.toString());
            }
            return file;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //file.setAttributes(jsonFile.toString());
        return file;
    }

    public String get_url(MegaFile f) {

        if (f.getHandle() == null || f.getKey() == null) {
            return "Error";
        }
        JSONObject json = new JSONObject();
        try {
            json.put("a", "l");
            json.put("n", f.getHandle());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String public_handle;
        public_handle = api_request(json.toString());
        if (public_handle.equals("-11")) {
            return "Shared file, no public url";
        }
        return "https://mega.co.nz/#!" + public_handle.substring(1, public_handle.length() - 1) + "!" + MegaCrypt.a32_to_base64(f.getKey());

    }

    public static void print(Object o) {
        System.out.println(o);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_url = (EditText) this.findViewById(R.id.txt_url);
        txt_cartella = (TextView) this.findViewById(R.id.txt_cartella);

        btn_download = (Button) this.findViewById(R.id.btn_down);
        btn_download.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onClick(View arg0){
        switch(arg0.getId()) {
            case R.id.btn_down:
                String Url;
                Url = txt_url.getText().toString();
                if(txt_url.getText().toString().compareTo("") != 0)
                {
                prepare_download();
                    try {
                        download(Url,pathfinal);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(this,"Inserisci Un link",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
