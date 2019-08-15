    Uri targURI = Uri.parse("content://xxxx/yyy.txt");
    try {
        InputStream content = getContentResolver().openInputStream(targURI);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(content));
        String line1;
        String text = "";
        while ((line1 = reader1.readLine()) != null) {
            text+=line1;
        }
        Log.i("FILE ENCRYPTED", text);
        String DECRYPTED = "";
        DECRYPTED = decrypt(text);
        Log.i("FILE DECRYPTED:", DECRYPTED);

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public String decrypt(String paramString) throws Exception {
        String md5_pin1 = "";
        String md5_pin = MD5(md5_pin1);
        SecretKeySpec keySpec = new SecretKeySpec(md5_pin.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] paramString1 = Base64.decode(paramString.getBytes(), 0);
        byte[] paramstring2 = cipher.doFinal(paramString1);
        String decoded = new String(paramstring2, "UTF-8");
        return decoded;
    }

@NonNull
public static String MD5(String paramString) throws Exception {
    MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
    digest.update(paramString.getBytes());
    byte messageDigest[] = digest.digest();
    StringBuffer hexString = new StringBuffer();
    int i=0;
    while( i < messageDigest.length) {
        String str = Integer.toHexString( messageDigest[i] & 0xFF );
        if (str.length() == 1) {
            hexString.append("0");
        }

        hexString.append(str);
        i += 1;
    }
    return hexString.toString();
}
