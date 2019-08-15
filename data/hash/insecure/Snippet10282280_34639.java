import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AndroidApp.R;
import com.AndroidApp.domain.Utente;
import com.AndroidApp.pagine.MenuPagina;


public class LoginActivity extends Activity {

    final String TAG = "LogIN";
    ArrayList<HashMap<String, String>> mylist;

    private Button bLogin, bExit;
    private EditText utente, passwd;
    private MediaPlayer mpButtonClick = null;
    private SharedPreferences mPreferences; 
    public Thread sessionTimer;
    public long tId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();

        String nome = mPreferences.getString("nome", "Nessuno");
        setTitle("Sessione di : " + nome);
        Log.w("TotThreads", Integer.toString(Thread.activeCount()));
        if (MenuPagina.reset){
            Log.w("LogIn", "trying to interrupt");


            //this is where im trying to interrypt the thread



            MenuPagina.reset = false;
        }

        if (!checkLoginInfo()) {

            mpButtonClick = MediaPlayer.create(this, R.raw.button);

            bLogin = (Button)findViewById(R.id.bLogin);
            bLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {

                    mpButtonClick.start();

                    Log.v(TAG, "Trying to Login");
                    utente = (EditText)findViewById(R.id.etUtente);
                    passwd = (EditText)findViewById(R.id.etPassword);

                    String username = utente.getText().toString();
                    username = ("aaa@ffff.it");
                    String password = md5(passwd.getText().toString());
                    Log.v(TAG, password);

                    XMLFunctionsLogin.getInstance().setNewURL("u=" + username + "&p=" + password);
                    String xml = XMLFunctionsLogin.getXML();
                    Document doc = XMLFunctionsLogin.xmlFromString(xml); 
                    int status = XMLFunctionsLogin.errStatus(doc); 

                    Log.v("status", Integer.toString(status));
                    if ((status == 0)) {
                        NodeList nodes = doc.getElementsByTagName("login"); 
                        Element e = (Element) nodes.item(0);
                        Utente utente = new Utente();
                        utente.setIdUtente(XMLFunctionsLogin.getValue(e, "idUtente"));
                        utente.setNome(XMLFunctionsLogin.getValue(e, "nome"));
                        utente.setCognome(XMLFunctionsLogin.getValue(e, "cognome"));
                        Log.v("utente", utente.getCognome().toString());

                        Log.v(TAG, "5");

                        List<NameValuePair> nvps = new ArrayList<NameValuePair>(2);
                        nvps.add(new BasicNameValuePair("utente", username));
                        nvps.add(new BasicNameValuePair("password", password));
                        Log.v(TAG, nvps.get(0).toString());
                        Log.v(TAG, nvps.get(1).toString());

                        // Store the username and password in SharedPreferences after the successful login
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString("userName", username);
                        editor.putString("password", password);
                        editor.putString("idUtente", utente.getIdUtente());
                        editor.putString("nome", utente.getNome());
                        editor.putString("cognome", utente.getCognome());
                        editor.commit();


                        Log.v(TAG, "Successo");

                        sessionTimer = new Thread() {
                            @Override
                            public void run() {
                                long tId = Thread.currentThread().getId();
                                Log.w("TTthread Id", Long.toString(tId));
                                for (int i = 30; i >= 0; i -= 1) {
                                    if (i == 0) {
                                        System.out.print("timer finito");
                                        Log.i("Timer", "timer finito");
                                        LoginActivity.this.runOnUiThread(new Runnable() { 
                                            public void run() { 
                                                Toast.makeText(LoginActivity.this, "ti si è scaduta la sessione", Toast.LENGTH_LONG).show(); 
                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        //System.exit(0);
                                    } try {
                                        Thread.sleep(1000);
                                        Log.i("Timer", Integer.toString(i));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {
                                        finish();
                                    }
                                }
                            }

                        };
                        sessionTimer.start();


                        Log.v(TAG, "Successo2");


                        /*
                        Counter timer = new Counter();
                        timer.start();

                         */

                        Toast.makeText(LoginActivity.this, "LogIn con successo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MenuPagina.class);
                        startActivity(intent);

                    } else {
                        final String errorMessage = XMLFunctionsLogin.errStatusDesc(doc);
                        Log.v("fallimento", errorMessage);
                        LoginActivity.this.runOnUiThread(new Runnable() { 
                            public void run() { 
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show(); 
                            }
                        });

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }

            });

            bExit = (Button)findViewById(R.id.bExit);
            bExit.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    mpButtonClick.start();
                    finish();
                }
            });
        }
    }

    //Checking whether the username and password has stored already or not
    private final boolean checkLoginInfo() {
          boolean username_set = mPreferences.contains("UserName");
          boolean password_set = mPreferences.contains("PassWord"); 
          if ( username_set || password_set ) {
                return true;
          } 
          return false;
    }

    //md5 for crypting and hash
    private static String md5(String data) {
        byte[] bdata = new byte[data.length()];
        int i;
        byte[] hash;

        for (i = 0; i < data.length(); i++)
            bdata[i] = (byte) (data.charAt(i) & 0xff);

        try {
            MessageDigest md5er = MessageDigest.getInstance("MD5");
            hash = md5er.digest(bdata);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        StringBuffer r = new StringBuffer(32);
        for (i = 0; i < hash.length; i++) {
            String x = Integer.toHexString(hash[i] & 0xff);
            if (x.length() < 2)
                r.append("0");
            r.append(x);
        }
        return r.toString();
    }
}
