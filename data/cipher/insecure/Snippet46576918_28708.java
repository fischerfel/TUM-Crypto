private static KeyGenerator key_gen = null;
private static SecretKey sec_key = null;
private static byte[] raw = null;
private static SecretKeySpec sec_key_spec = null;
private static Cipher sec_cipher = null;

//for DSA
private static KeyPairGenerator keypairgen = null;
private static KeyPair keypair = null;
private static DSAPrivateKey private_key = null;
private static DSAPublicKey public_key = null;
private static Signature dsa_sig = null;
private static SecureRandom secRan = null;
public static void main(String args[])
 {
    FileInputStream in_file = null;
    FileInputStream in_file2 = null;
    FileOutputStream out_file = null;
    String decrypted_str = new String();
    int read_bytes = 0;

    try{
        //open files
        in_file = new FileInputStream(args[0]);
        byte[] seed = args[1].getBytes();

        //encrypt file with AES
        //key setup - generate 128 bit key
        SecureRandom rand = new SecureRandom();
        rand.setSeed(seed);

        key_gen = KeyGenerator.getInstance("AES");
        key_gen.init(128, rand);
        sec_key = key_gen.generateKey();

        //get key material in raw form
        raw = sec_key.getEncoded();
        sec_key_spec = new SecretKeySpec(raw, "AES");

        //create the cipher object that uses AES as the algorithm
        sec_cipher = Cipher.getInstance("AES"); 

        //decrypt file
        byte[] ciphtext = new byte[in_file.available()];
        in_file.read(ciphtext); 

        decrypted_str = aes_decrypt(ciphtext);

        byte[] decryptedBytes = decrypted_str.getBytes();
        byte[] decryptedMSG = new byte[decryptedBytes.length - 20];
        byte[] decryptedHash = new byte[20];

        System.out.print(decrypted_str);
        System.out.print("\n");
        for(int i = 0; i < decryptedMSG.length; i++)
            decryptedMSG[i] = decryptedBytes[i];

        for(int i = 0; i < decryptedHash.length; i++)
            decryptedHash[i] = decryptedBytes[i+ decryptedMSG.length];

        String decryptedCalcSHA = toHexString(sha1_hash(decryptedMSG));
        String fileSHA = toHexString(decryptedHash);

        System.out.println("DEC:" +decryptedCalcSHA);
        System.out.println("FIL:" +fileSHA);
    }
    catch(Exception e){
        e.printStackTrace();
    }
    finally{

        try{
            if (in_file != null){
                in_file.close();
            }
            if(out_file != null){
                out_file.close();
            }
            if(in_file2 != null){
                in_file2.close();
            }
        } catch(Exception e2)
        {
            e2.printStackTrace();
        }

    }
}
