package gsb.com;
import gsb.com.R;


import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity
{
    // Lien vers votre page php sur le serveur
    private static final String UPDATE_URL  = some ip
    public ProgressDialog progressDialog;
    private EditText UserEditText;
    private EditText PassEditText;
    private android.widget.Button button;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i("Info","Dans Login - onCreate --------------");
        //initialisation d'une progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Patientez svp...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        //recuperation des elements de la vue
        UserEditText = (EditText)findViewById(R.id.username);
        if (UserEditText == null) { Log.w("Info", "UserEditText est null --------------"); }
        PassEditText = (EditText)findViewById(R.id.password);
        if (PassEditText == null) { Log.w("Info", "PassEditText est null --------------"); }
        button = (Button)findViewById(R.id.okbutton);
        if (button == null) { Log.w("Info", "button est null --------------"); }

        //definition du listener du bouton ok
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                int userSize = UserEditText.getText().length();
                int passSize = PassEditText.getText().length();

                //si les deux champs sont complétés
                if(userSize > 0 && passSize > 0){
                    progressDialog.show();
                    String user = UserEditText.getText().toString();
                    String pass = PassEditText.getText().toString();
                    //appel de la fonction doLogin qui va appeler le script connect.php
                    doLogin(user,pass);
                }
                else {
                    createDialog("Erreur !!", "SVP Entrez un nom d'utilisateur et un mot de passe.");
                }
            }
        });

        button = (Button)findViewById(R.id.cancelbutton);
        //creation du listener du bouton annuler pour sortir de l'appli
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                quit(false, null);
            }
        });
    }

    private void quit(boolean succes, Intent i){
        //envoi d'un resultat qui va permettre de quitter l'appli
        setResult((succes)? Activity.RESULT_OK : Activity.RESULT_CANCELED,i);
        finish();
    }

    private void createDialog(String title, String text){
        //affichage d'un popup
        AlertDialog ad  = new AlertDialog.Builder(this)
                .setPositiveButton("Ok", null).setTitle(title).setMessage(text)
                .create();
        ad.show();
    }

    private void doLogin(final String login, final String pass){
        final String pw = md5(pass);
        //final String pw = pass;

        //creation d'un thread
        Thread t = new Thread(){
            public void run(){
                Looper.prepare();
                //connexion au serveur pour communiquer avec le php
                DefaultHttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(),15000);

                HttpResponse response;
                HttpEntity entity;

                try{
                    //on etablit un lien avec le script connect.php
                    HttpPost post = new HttpPost(UPDATE_URL);
                    List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                    nvp.add(new BasicNameValuePair("username",login));
                    nvp.add(new BasicNameValuePair("password",pw));
                    post.setHeader("Content-Type", "application/x-www-form-urlencoded");

                    //passage des params login et password qui vont etre recup par le script php en Post
                    post.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));

                    //recup du resultat du script
                    response = client.execute(post);
                    entity = response.getEntity();
                    InputStream is = entity.getContent();

                    //appel d'une fonction pour traduire la reponse
                    read(is);
                    is.close();

                    if(entity != null){
                        entity.consumeContent();
                    }
                }
                catch(Exception e){
                    progressDialog.dismiss();
                    createDialog("Error", "Impossible d'etablir une connexion");
                }
                Looper.loop();
            }
        };
        t.start();
    }

    private void read(InputStream inpStr){
        //traduction du resultat du flux
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;

        try{
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            //classe definie plus bas
            LoginContentHandler lch = new LoginContentHandler();

            xr.setContentHandler(lch);
            xr.parse(new InputSource(inpStr));          
        }
        catch(ParserConfigurationException e){}
        catch(SAXException e){}
        catch(IOException e){}
    }

    private String md5(String in) {

        MessageDigest digest;

        try {

            digest = MessageDigest.getInstance("MD5");

            digest.reset();

            digest.update(in.getBytes());

            byte[] a = digest.digest();

            int len = a.length;

            StringBuilder sb = new StringBuilder(len << 1);

            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }

            return sb.toString();

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class LoginContentHandler extends DefaultHandler {
        //Traite le retour du script connect.php

        private boolean in_loginTag = false;
        private int userID;
        private boolean error_occured = false;

        public void startElement(String n, String l, String q, Attributes a) throws SAXException {

                if(l=="login"){
                    in_loginTag = true;
                }

                if(l=="error"){
                    progressDialog.dismiss();

                    switch(Integer.parseInt(a.getValue("value"))){
                    case 1:
                        createDialog("Error", "Impossible de se connecter à la base de données");
                        break;
                    case 2:
                        createDialog("Error", "Erreur dans la base de données, Table manquante");
                        break;
                    case 3:
                        createDialog("Error", "Login ou mot de passe invalide !");
                        break;
                    }
                    error_occured = true;
                }

                if(l=="user" && in_loginTag && a.getValue("id")!=""){
                    //si tout est ok on recup l'id de l'utilisateur
                    userID = Integer.parseInt(a.getValue("id"));
                }                           
        }

        public void endElement(String n, String l, String q) throws SAXException {
            //on renvoie l'id si tout est ok
            if(l=="login"){
                in_loginTag = false;

                if(! error_occured){
                    progressDialog.dismiss();
                    Intent i = new Intent();
                    i.putExtra("userid", userID);
                    quit(true, i);
                }
            }
        }

        public void characters(char ch[], int start, int length){}
        public void startDocument() throws SAXException{}
        public void endDocument() throws SAXException{}
    }
}
