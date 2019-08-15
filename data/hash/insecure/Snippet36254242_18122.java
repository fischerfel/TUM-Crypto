import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity {

    String passwordToHash;
    String result;
    boolean goodPIN = false;
    boolean startbruteforce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    //My stuff

    public void doIt(View v) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        RadioButton r2 = (RadioButton) findViewById(R.id.calculate);
        RadioButton r1 = (RadioButton) findViewById(R.id.crack);

        final EditText input = (EditText) findViewById(R.id.inputTextArea);
        final EditText output = (EditText) findViewById(R.id.outputTextArea);

        //Toast.makeText(this, "Working on it!", Toast.LENGTH_LONG).show();

        if(r2.isChecked())
        {
            if(input.getText().toString().length() > 4)
            {
                goodPIN = false;
                output.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle ("Uuuuuuhh....");
                builder.setMessage("Hash not calculated because that PIN would take too long to brute force :(");
                builder.setPositiveButton("Yeah, whatever...", null);
                builder.show();
            }
            else
            {
                goodPIN = true;
            }

            if(goodPIN)
            {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Toast.makeText(this, "Calculated MD5!", Toast.LENGTH_LONG).show();

                passwordToHash = input.getText().toString();

                MessageDigest digest = MessageDigest.getInstance("MD5");

                byte[] inputBytes = passwordToHash.getBytes("UTF-8");

                byte[] hashBytes = digest.digest(inputBytes);

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < hashBytes.length; i++)
                {
                    stringBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
                            .substring(1));
                }

                result = stringBuffer.toString();

                output.setText(result);
            }
        }


        else if(r1.isChecked())
        {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Working on it!", "Brute-forcing. Please wait...", true);
            double starttime = System.currentTimeMillis();

            final Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    String crackedPassword = "Hello";
                    String crackedPasswordHash = "a262";
                    int pinsTested = 1000;
                    int crackedPasswordInt = 1000;
                    String passwordToCrack;

                    //Get the password to crack
                    passwordToCrack = input.getText().toString();

                    long startTime = System.currentTimeMillis();

                    while (!crackedPasswordHash.equals(passwordToCrack))
                    {
                        pinsTested++;
                        crackedPasswordInt++;
                        crackedPassword = Integer.toString(crackedPasswordInt);

                        MessageDigest digest = null;
                        try
                        {
                            digest = MessageDigest.getInstance("MD5");
                        }
                        catch (NoSuchAlgorithmException e)
                        {
                            e.printStackTrace();
                        }

                        byte[] inputBytes = new byte[0];
                        try
                        {
                            inputBytes = crackedPassword.getBytes("UTF-8");
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        byte[] hashBytes = digest.digest(inputBytes);

                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i < hashBytes.length; i++)
                        {
                            stringBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
                                    .substring(1));
                        }

                        crackedPasswordHash = stringBuffer.toString();

                        //System.out.println(pinsTested + " PINs tested");
                        //System.out.println("Hash of: " + pinsTested + " is: " + crackedPasswordHash);
                    }
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime - startTime;

                    System.out.println("Done! " + pinsTested);

                    updateUI(pinsTested);

                    //runOnUiThread(pinsTested);
                }
            };

            Thread animation = new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.sleep(4000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                    thread.start();
                }
            };

            animation.start();

        }
    }

    public void updateUI(final int pass) {

        Looper.prepare();
        final Handler myHandler = new Handler();
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            test(pass);
                        }
                    });
                }
        })).start();
    }

    public void test(int pass)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle ("Done!");
        builder.setMessage("PIN is: " + pass);
        builder.setPositiveButton("Yeah, whatever...", null);
        builder.show();
    }
}
