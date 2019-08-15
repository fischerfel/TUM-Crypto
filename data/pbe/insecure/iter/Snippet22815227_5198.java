public static final int SALT_LENGTH = 20;
public static final int PBE_ITERATION_COUNT = 200; //1024;

private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";

//algoritmo / modo / relleno 
private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

byte[] iv = "1234567890asdfgh".getBytes();

byte[] salt = "o6806642kbM7c5".getBytes();

@SuppressLint("TrulyRandom")
public byte[] encrypt(String password, String cleartext) {

    byte[] encryptedText = null;

    try {


        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

        //Factoria para crear la SecretKey, debemos indicar el Algoritmo
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);

        SecretKey tmp = factory.generateSecret(pbeKeySpec);

        //Creamos una llave;
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        //Obtenemos la llave, solo informativo
        byte[] key = secret.getEncoded();

        //La clase Cipher, se usa para cifrar mediante algoritmos de  clave simétrica
        Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);   

        //byte[] iv = generateIv();

        IvParameterSpec ivspec = new IvParameterSpec(iv);

        //Accion, SecretKey, parameter specification for an initialization vector
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);

        //Realizamos el cifrado
        encryptedText = encryptionCipher.doFinal(cleartext.getBytes());

    } catch (Exception e) {
        e.printStackTrace();
    }

    return encryptedText;
}

public String decrypt(String password, byte[] encryptedText) {

    String cleartext = "";

    try {

        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

        //Factoria para crear la SecretKey, debemos indicar el Algoritmo
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);

        SecretKey tmp = factory.generateSecret(pbeKeySpec);

        //Creamos una llave;
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        //Obtenemos la llave, solo informativo
        byte[] key = secret.getEncoded();

        //La clase Cipher, se usa para cifrar mediante algoritmos de  clave simétrica
        Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);

        //byte[] iv = generateIv();

        IvParameterSpec ivspec = new IvParameterSpec(iv);

        //Accion, SecretKey, parameter specification for an initialization vector
        decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);

        //Realizamos el descifrado
        byte[] decryptedText = decryptionCipher.doFinal(encryptedText);

        cleartext =  new String(decryptedText); 

    } catch (Exception e) {
        e.printStackTrace();
    }

    return cleartext;
}      
