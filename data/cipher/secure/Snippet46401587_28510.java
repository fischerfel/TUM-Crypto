public class MainActivity extends AppCompatActivity {

    private static final String KEY_NAME = "secretkey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;

    private TextView textView;
    private Button auth_button,stop_button;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            textView=(TextView)findViewById(R.id.authStatus);
            auth_button=(Button)findViewById(R.id.auth_button);
            stop_button=(Button)findViewById(R.id.stop_button);

            if (!fingerprintManager.isHardwareDetected()) {
                textView.setText("Your device doesn't support fingerprint authentication");
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                textView.setText("Please enable the fingerprint permission");
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            if (!keyguardManager.isKeyguardSecure()) {
                textView.setText("Please enable lockscreen security in your device's Settings");
            }

            else {
                auth_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Starting service", Toast.LENGTH_SHORT).show();
                        try
                        {
                            generateKey();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        if(initCipher())
                        {
                            Provider provider=new Provider(fingerprintManager,cryptoObject,MainActivity.this);
                            Intent intent=new Intent(MainActivity.this,AsyncService.class);
                            startService(intent);
                        }
                    }
                });

                stop_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopService(new Intent(MainActivity.this,AsyncService.class));
                        Toast.makeText(MainActivity.this, "Service stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void generateKey() {
        try
        {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new

                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean initCipher() {
        try
        {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try
        {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }
        catch (KeyPermanentlyInvalidatedException e)
        {
            return false;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
