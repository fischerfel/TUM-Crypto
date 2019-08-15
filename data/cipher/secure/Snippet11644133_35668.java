public String Decrypt(String text, String pubkey) throws Exception
{
    System.out.println("------INSIDE IEPublicDecrypt METHOD------ ");
    X509Certificate cerificate = null;
    PrivateKey privatekey = null;
    KeyStore keyStorenew = null;
    String aliasnew = null;
    //new code for security

    if (browserName.equalsIgnoreCase("Netscape")) {
        System.out.println("Initializing Firefox");
        createPolicyFile();
        makeCfgFile();
        String strCfg = System.getProperty("user.home") + File.separator
                + "jdk6-nss-mozilla.cfg";
        System.out.println("String Configuration File " + strCfg);

        try {
            Provider p1 = new sun.security.pkcs11.SunPKCS11(strCfg);
            Security.addProvider(p1);
            System.out.println("Provider Added");
            keyStorenew = KeyStore.getInstance("PKCS11");
            System.out.println("Key Store instance created");
            keyStorenew.load(null, "password".toCharArray());
            System.out.println("Key Store loaded");
        } catch (Exception e) {
            System.out.println("Certificate Not found in browser");
        }
    }
    if (keyStorenew != null) {
        //initBrowserCertifcates();
        Enumeration<String> enumeration = keyStorenew.aliases();
        while (enumeration.hasMoreElements()) {
            aliasnew = enumeration.nextElement();


            try {
                cerificate = (X509Certificate) keyStorenew.getCertificate(aliasnew);
                System.out.println("Certificate  found in browser========"+cerificate);
            }
            catch (Exception e) {
                // TODO: handle exception
            }
        }
        System.out.println("Browser Certificate Initialized.");
    } else {
        System.out.println("========= Keystore is NULL ==========");

    }

    if(keyPairMap != null)
    {
        System.out.println("keyPairMap is not  NuLL.");
        privatekey=keyPairMap.get(pubkey.toString());
    }
    else
    {
        System.out.println("keyPairMap is NuLL.");
    }   
    System.out.println("------GOT PRIVATEKEY------ " + privatekey);
    BASE64Decoder base64Decoder = new BASE64Decoder();
    byte[] encryptText = base64Decoder.decodeBuffer(text);
    System.out.println("------GOT ENCRYTEDTEXT------ "
            + encryptText.toString());
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    System.out.println("------GOT CIPHER------ " + cipher);
    cipher.init(Cipher.DECRYPT_MODE, privatekey);
    System.out.println("------CIPHET INITIALISED------ ");
    String decryptedString = new String(cipher.doFinal(encryptText));
    System.out.println("------GOT DECRYPTEDTEXT------ " + decryptedString);
    return decryptedString;
}
