 public static void functionRegistration(String usr, String pwd) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{

    Socket socket = new Socket(SERVER_ADDRESS_STRING, PORT_NO);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    socket.setSoTimeout(DEFAULT_TIMEOUT);

    if(!socket.isConnected()){
        System.out.println("[!] [Client] Connection problem!");
        socket.close();
        return;
    }

    //Diffie-Hellman
    BigInteger shared_key = DiffieHellmanExchangeClient(socket, br, bw);
    byte[] hash = ObjectHash.getByteHashCode(shared_key, SECURE_HASH_TYPE.SHA384);

    //Extract IV and cipherKey
    byte[] IV = new byte[16];
    byte[] cipherKey = new byte[32];

    int i, limit;

    for(i = 0; i < IV.length; i++)
        IV[i] = hash[i];

    limit = i;

    for(; i < hash.length; i++)
        cipherKey[i - limit] = hash[i];

    //Send username
    bw.write(usr);
    bw.write("\r\n");
    bw.flush();


    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

    //Hash password
    String passwordHash = new String(ObjectHash.getByteHashCode(pwd, SECURE_HASH_TYPE.SHA512));

    //Cipher password
    String encryptedPasswordHash = new String(cipherMessage(passwordHash, cipherKey));

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    IvParameterSpec ivparameters = new IvParameterSpec(IV);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(cipherKey, "AES"), ivparameters);


    oos.writeObject(new SealedObject(encryptedPasswordHash, cipher));
    oos.flush();


    if(br.readLine().compareTo("ACK") == 0)
        Log.d("ACK", "ACK_RECEIVED");

    else
        Log.d("ACK","Something was wrong");

    br.close();
    bw.close();
    socket.close();
}
