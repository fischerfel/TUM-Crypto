public GetUID4Nubo() {
    getid();
}

public static void main(String[] args) {
    new GetUID4Nubo();
}

public static String getid(){
    String result = "";
    try{
        //result = getMACID() + "@###@" + getSerialNumber("C") + "@###@" + getMotherboardSN();
        result = getSerialNumber("C") + "@###@" + getMotherboardSN();
        System.out.println(result);
        result = encrypt("nubo123", result);
        System.out.println(result);
    }catch(Exception e){
        e.printStackTrace();
    }
    return result;
}

public static String getone(){
    return "one";
}


    // 8-byte Salt
    static byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };
    // Iteration count
    static int iterationCount = 19;
    static Cipher ecipher;

public static String encrypt(String secretKey, String plainText)
        throws NoSuchAlgorithmException, InvalidKeySpecException,
        NoSuchPaddingException, InvalidKeyException,
        InvalidAlgorithmParameterException, UnsupportedEncodingException,
        IllegalBlockSizeException, BadPaddingException {
    // Key generation for enc and desc
    KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
    SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
            .generateSecret(keySpec);
    ///System.out.println("this is key : " + key);
    // Prepare the parameter to the ciphers
    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

    // Enc process
    ecipher = Cipher.getInstance(key.getAlgorithm());
    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    String charSet = "UTF-8";
    byte[] in = plainText.getBytes(charSet);
    byte[] out = ecipher.doFinal(in);

    String encStr = new String(Base64.encodeBase64(out));

    return encStr;
}

/*public static String getMACID(){
    String result = "";
    try{
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        byte[] mac = network.getHardwareAddress();
        result = byte2string4MAC(mac);
    }catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (SocketException e) {
        e.printStackTrace();
    }
    return result;
}*/

public static String byte2string4MAC(byte[] mac)
{
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mac.length; i++) {
        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
    }
    return sb.toString(); 
}

public static String getSerialNumber(String drive) {
    String result = "";
        try {
        File file = File.createTempFile("realhowto", ".vbs", new File("D:\\"));
        file.deleteOnExit();
        FileWriter fw = new java.io.FileWriter(file);

      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    +"Set colDrives = objFSO.Drives\n"
                    +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    +"Wscript.Echo objDrive.SerialNumber";  // see note

        fw.write(vbs);
        fw.close();
          Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
          BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
            result += line;
        }
        input.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result.trim();
}

public static String getMotherboardSN() {
    String result = "";
    try {
        File file = File.createTempFile("realhowto", ".vbs", new File("D:\\"));
        file.deleteOnExit();
        FileWriter fw = new java.io.FileWriter(file);

        String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                + "Set colItems = objWMIService.ExecQuery _ \n"
                + " (\"Select * from Win32_BaseBoard\") \n"
                + "For Each objItem in colItems \n"
                + " Wscript.Echo objItem.SerialNumber \n"
                + " exit for ' do the first cpu only! \n" + "Next \n";

        fw.write(vbs);
        fw.close();
        Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = input.readLine()) != null) {
            result += line;
        }
        input.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result.trim();
}
