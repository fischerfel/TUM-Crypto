public class ATMPINClient{


    public static void main(String[] args) throws Exception {

        String pkey = "MIIB/DCCAWmgAwIBAgIQpbarEXfe8rVDlWlg2T+ixzAJBgUrDgMCHQUAMBgxFjAUBgNVBAMTDUJBTktESE9GQVIwMDEwIBcNMTMwNzEwMDkxMjU5WhgPMjA5OTEyMjkyMDAwMDBaMBgxFjAUBgNVBAMTDUJBTktESE9GQVIwMDEwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBALZTB/2vKxWwCGhUdywVvikj8klvlzpZTJbVd0bRIN82bTTzp53SDXczc7mkto4vsqelGqnyjZcigyhj5y60SWYggc83d89I+i2Vo77am6aW8tfx1p/x9Op6bDLIN8V0uyoBK8IhRbuiugHmbP69Fyq4vXQ4+D2EzmmOuPRQfg4BAgMBAAGjTTBLMEkGA1UdAQRCMECAEAMkZd7uwQQG7803GjCmF7yhGjAYMRYwFAYDVQQDEw1CQU5LREhPRkFSMDAxghCltqsRd97ytUOVaWDZP6LHMAkGBSsOAwIdBQADgYEAhau3OD9QPoJm+H8v70WQmGUwJaS2IZORo/f8sMgUnVA6qoiD7BRkv8VVT0No4H+77YnYR2mtlCkU1BenKM3bC4WQXsXawMDSOoJcqBVLBFpYzl/8xpNrRyA8yyLUX37kXmH6mdioGLiNSKhQvX/XBYkTeOnsS2umt+zjS2JDS+g=";

        String atmPin = "1234";
        byte[] key = pkey.getBytes();
        encrypt(atmPin, key);


    }

    public static byte[] encrypt(String atmPin, byte[] keyCode)
            throws Exception {
        X509Certificate cert = X509Certificate.getInstance(keyCode);
         RSAPublicKey rk = (RSAPublicKey) cert.getPublicKey();
        System.out.println("Algorithm: " + rk.getAlgorithm());
        System.out.println("Modules : " + rk.getModulus());
        System.out.println("Hex - Modules : " + rk.getModulus().toString(16).toUpperCase());

        System.out.println("Exponent : " + rk.getPublicExponent());
        System.out.println("Name : " + cert.getSubjectDN().getName());
        System.out.println("Issuer Name : " + cert.getIssuerDN().getName());
        System.out.println("Not After : " + cert.getNotAfter());
        System.out.println("Not Before : " + cert.getNotBefore());
        System.out.println("Format : " + cert.getPublicKey().getFormat());
        Cipher cipher = Cipher.getInstance("RSA");

        cipher.init(Cipher.ENCRYPT_MODE, rk);
        System.out.println("Actual PIN : " + atmPin);
        byte[] PIN = cipher.doFinal(atmPin.getBytes("UTF-8"));

        String encPIN = new sun.misc.BASE64Encoder().encode(PIN);
        System.out.println("Encrypted Pin : " + encPIN);

        return PIN;

    }
}
