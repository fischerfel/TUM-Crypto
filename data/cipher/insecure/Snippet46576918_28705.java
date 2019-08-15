public static KeyGenerator key_gen = null;
public static SecretKey sec_key = null;
public static byte[] raw = null;
private static SecretKeySpec sec_key_spec = null;
private static Cipher sec_cipher = null;

//for DSA
private static KeyPairGenerator keypairgen = null;
private static KeyPair keypair = null;
private static DSAPrivateKey private_key = null;
private static DSAPublicKey public_key = null;
private static Signature dsa_sig = null;
private static SecureRandom secRan = null;

public static void main(String args[]){
    FileInputStream in_file = null;
    FileInputStream in_file2 = null;
    FileOutputStream out_file = null;
    byte[] sha_hash = null;
    //byte[] hmac_hash = null;
    byte[] aes_ciphertext = null;
    int read_bytes = 0;

    try{
        //open files
        in_file = new FileInputStream(args[0]);
        out_file = new FileOutputStream("CipherText.txt");
        byte[] seed = args[1].getBytes();

        //read file into a byte array
        byte[] msg = new byte[in_file.available()];
        read_bytes = in_file.read(msg);

        //SHA-1 Hash
        sha_hash = sha1_hash(msg);
        byte[] concatenatedMSG = new byte[msg.length + sha_hash.length];

        for (int i = 0; i < concatenatedMSG.length; i++)
        {
            if(i < msg.length)
                concatenatedMSG[i] = msg[i];
            else
                concatenatedMSG[i] = sha_hash[i - msg.length];

        }

        //print out hash in hex
        System.out.println("SHA-1 Hash: " + toHexString(sha_hash));

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

        //do AES encryption
        aes_ciphertext = aes_encrypt(concatenatedMSG);
        out_file.write(aes_ciphertext);
        out_file.close();
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
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
