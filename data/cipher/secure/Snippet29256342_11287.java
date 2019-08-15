public class AsymmetricEncryptionWithRSA {

  public void decryptdata1(){


    String PRIVATE_RSA_KEY_PKCS8 = 
            "-----BEGIN PRIVATE KEY-----\n" +
            "<RSAKeyValue><Modulus>rotP8p9dJpRM/D7HHW9wsRWJEBVdZolRR0PuYGp/Mjae2gElTZSBQ44ifOvVquI95ECDa2ypIrEz1k/mKRtHPmsSMqmySWL9CVsEZAc/zNfYjIKb4BDaFiBaPEsboebIEuaN3NFt2bbl/RB45TCycYylgDjCdFFASRbHs0jX4cM=</Modulus><Exponent>AQAB</Exponent></RSAKeyValue>\n" +
        "-----END PRIVATE KEY-----\n";
        String key = PRIVATE_RSA_KEY_PKCS8
            .replace("-----BEGIN PRIVATE KEY-----\n", "")
            .replace("\n-----END PRIVATE KEY-----\n", "");
        System.out.println("key : "+key);

        PKCS8EncodedKeySpec keySpec =
            new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(key));
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            String s = "kjZzSUPJBY4rM23P7t47W3RYPTqhEARCqOcgRfxxBSc3O2mxQZKE/HKDN9OzW5pc3K37rcd+Dc/d55uNJcJUWwmZL0vVgrQIBkQdSkrzcpXV9JD80Qgy8sCuHoqMU49U6I34I43+NUAFmQDkApHbMA8LGV6KLtoyIyUSpDiuW8Q=";
            byte[] ciphertextBytes = Base64.decode(s.getBytes());
            byte[] decryptedData = cipher.doFinal(ciphertextBytes);
            String decryptedString = new String(decryptedData);
            System.out.println("decrypted (plaintext) = " + decryptedString);
        }catch(Exception e){
            e.printStackTrace();

        }
  }

  public static void main(String[] args) {

    AsymmetricEncryptionWithRSA ae = new AsymmetricEncryptionWithRSA();
    ae.decryptdata1();

  }
