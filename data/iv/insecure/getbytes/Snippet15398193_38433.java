String CIPHER_ALGORITHM = "DES/CFB8/NoPadding";
SecretKeySpec key5= new SecretKeySpec("passkey*".getBytes("UTF-8"), 
    CIPHER_ALGORITHM);

String ivString = "passkey*";
byte[] ivByte = ivString.getBytes("UTF-8");
IvParameterSpec iv5 = new IvParameterSpec(ivByte);  

Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
c.init(Cipher.DECRYPT_MODE, key5, iv5);

CipherInputStream cis = new CipherInputStream(
    new FileInputStream("/sdcard/test_folder/test.file"), c);

BufferedReader br = new BufferedReader(new InputStreamReader(cis));
Log.d("SONUC2", " " +br.readLine());
