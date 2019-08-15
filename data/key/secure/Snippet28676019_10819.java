package com.example.encryptiondecryption;

    import java.security.SecureRandom;

    import javax.crypto.Cipher;
    import javax.crypto.KeyGenerator;
    import javax.crypto.spec.SecretKeySpec;
    import android.app.Activity;
    import android.app.Dialog;
    import android.content.Context;
    import android.os.Bundle;
    import android.util.Base64;
    import android.util.Log;
    import android.view.View;
    import android.view.View.OnClickListener;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;

    public class MainActivity extends Activity implements OnClickListener {

        static final String TAG = "SymmetricAlgorithmAES";
        static final String TAG1 = "encccccccc";
        EditText getData_edt, key_edt;
        Button genkey_btn, encrypt_btn, decrypt_btn;
        static String key_str = null;
        static String getData_str = null;

        static SecretKeySpec sks = null;
        static byte[] encodedBytes = null;

        static byte[] decodedBytes = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getData_edt = (EditText) (findViewById(R.id.am_input_edt));
            genkey_btn = (Button) (findViewById(R.id.am_key_btn));
            encrypt_btn = (Button) (findViewById(R.id.am_encrypt_btn));
            decrypt_btn = (Button) (findViewById(R.id.am_decrypt_btn));
            getData_str = getData_edt.getText().toString();

            genkey_btn.setOnClickListener(this);
            encrypt_btn.setOnClickListener(this);
            decrypt_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {

            case R.id.am_key_btn:
                genKey(sks);

                break;
            case R.id.am_encrypt_btn:
                encrypt(sks);

                break;
            case R.id.am_decrypt_btn:
                decrypt();

                break;

            default:
                break;
            }

        }

        private void genKey(SecretKeySpec sks) {
            // TODO Auto-generated method stub
            Context context = this;
            final Dialog myDialog = new Dialog(context);

            myDialog.setContentView(R.layout.dialog);

            myDialog.setTitle("enter key");

            key_edt = (EditText) myDialog.findViewById(R.id.dg_key_tv);
            Button ok_btn = (Button) myDialog.findViewById(R.id.dg_ok_btn);
            Button cancel_btn = (Button) myDialog.findViewById(R.id.dg_cancel_btn);

            Log.d(TAG1, key_str);

            ok_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    key_str = key_edt.getText().toString();

                }
            });
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(key_str.getBytes());

                KeyGenerator kg = KeyGenerator.getInstance("AES");
                kg.init(128, sr);
                sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
                Log.i("encrypt", sks.toString());

            } catch (Exception e) {
                Log.e(TAG, "AES secret key spec error");
            }
            cancel_btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    myDialog.dismiss();
                }
            });
            myDialog.show();

        }

        private void encrypt(SecretKeySpec sks) {
            // TODO Auto-generated method stub

            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.ENCRYPT_MODE, sks);
                encodedBytes = c.doFinal(getData_str.getBytes());
                String encoded = Base64
                        .encodeToString(encodedBytes, Base64.DEFAULT);
                System.out.println(" " + encoded);
            } catch (Exception e) {
                Log.e(TAG, "AES encryption error");
            }
        }

        private void decrypt() {
            // TODO Auto-generated method stub

            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, sks);
                decodedBytes = c.doFinal(encodedBytes);
                System.out.println(" " + new String(decodedBytes));

            } catch (Exception e) {
                Log.e(TAG, "AES decryption error");
                TextView tvdecoded = (TextView) findViewById(R.id.am_show_tv);
                tvdecoded.setText("DECOD\n" + new String(decodedBytes) + "\n");
            }
        }
    }
