TextView tv1; 
TextView tv2; 
TextView tv3; 
Button convert; 
String publicKeyFilename = null; 
String privateKeyFilename = null; 
String ST = "Ebenezersawesome"; 

@Override public void onCreate(Bundle savedInstanceState) 
{ 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    Log.d("Ebz", "Made it to onCreate"); 
    tv1 = (TextView) findViewById(R.id.tv1); 
    tv2 = (TextView) findViewById(R.id.tv2); 
    tv3 = (TextView) findViewById(R.id.tv3); 
    convert = (Button) findViewById(R.id.button1); 

    try { 
                byte[]plainText = "ST".getBytes("UTF8"); 
                Log.d("Ebz", "made it to Try Block"); 
                KeyPairGenerator keyGen = 
                    KeyPairGenerator.getInstance("RSA"); 
                keyGen.initialize(4096); 
                KeyPair key = keyGen.generateKeyPair(); 
                Cipher cipher = 
                    Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
                tv3.setText(cipher.getProvider().getInfo().toString()); 
                //tv3.setText(ST); 
                cipher.init(Cipher.ENCRYPT_MODE, key.getPublic()); 
                byte[]cipherText = cipher.doFinal(plainText); 
                tv1.setText(new String(cipherText, "UTF8").toString()); 
                //tv1.setText(ST); 
                byte[]newPlainText = cipher.doFinal(cipherText); 
                tv2.setText(new String(newPlainText, "UTF8").toString()); 
                //tv2.setText(ST); 
            } catch(Exception e) { 
                System.out.println("error"); 
            }
    // tv2.setText(ST); 
    convert.setOnClickListener(new OnClickListener() { 
        public void onClick(View v) { 
            Log.d("Ebz", "Made it to onCreate");  
        } 
    }); 
}
