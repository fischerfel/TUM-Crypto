class GCMClient
{
    // AES-GCM parameters
    public static final int AES_KEY_SIZE = 128; // in bits
    public static final int GCM_NONCE_LENGTH = 12; // in bytes
    public static final int GCM_TAG_LENGTH = 16; // in bytes
    public static void main(String args[]) throws Exception{        
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("192.168.1.8");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];        
        byte[] input = "hi".getBytes(); //2 bytes 

        byte[] keyBytes ="qwertyuiopasdfgh".getBytes();
        SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");       
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        nonce = "poiuytrewqlk".getBytes();;;        
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] aad =  "Whatever I like".getBytes();;
        cipher.updateAAD(aad);
        byte[] cipherText = cipher.doFinal(input); 
        System.out.println(cipherText.length+ "data sent!!!!!!! "); //18 bytes after encryption
        DatagramPacket sendPacket = new DatagramPacket(cipherText, cipherText.length, IPAddress, 9999);
        clientSocket.send(sendPacket);  
        clientSocket.close();     
   }
}
