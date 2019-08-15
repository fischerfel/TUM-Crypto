byte[] key = ("Sh").getBytes("UTF-8");
MessageDigest sha = MessageDigest.getInstance("SHA-1");
key = sha.digest(key);
key = Arrays.copyOf(key, 16);

secretKeySpec = new SecretKeySpec(key, "AES");
cipher = Cipher.getInstance("AES");

public void dec (String dir)
{       
    try{            
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);            
        String cleartextFile = dir;         
        FileInputStream fis = new FileInputStream(cleartextFile);                   
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        FileOutputStream fos = new FileOutputStream(dir);

        block = new byte[6];
        while ((i = cis.read(block)) != -1) {
            fos.write(block, 0, i);
        }
        fos.close();        
    }
    catch(Exception ex)
    {
        Toast.makeText(MainActivity.this, "Chiper Error"+ex, Toast.LENGTH_LONG).show();
    }
}
