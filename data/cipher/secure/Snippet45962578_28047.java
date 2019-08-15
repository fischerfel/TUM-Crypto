class GCMServer
{
     // AES-GCM parameters
    public static final int AES_KEY_SIZE = 128; // in bits
    public static final int GCM_NONCE_LENGTH = 12; // in bytes
    public static final int GCM_TAG_LENGTH = 16; // in bytes

    public static void main(String args[]) throws Exception{ 
        try{
            DatagramSocket serverSocket = new DatagramSocket(9999,InetAddress.getByName("192.168.1.8"));
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            while(true){           
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receivePacket.setData(new byte[4096]);  
                serverSocket.receive(receivePacket);
                byte[] rec=receivePacket.getData();
                String receivedData = new String(rec,0,receivePacket.getLength());          
                byte[] cipherText = receivedData.getBytes();

                System.out.println("received packet size before convert to bytes "+receivePacket.getLength());//it displays 18
                System.out.println("received packet size after convert to bytes "+cipherText.length);//it display 30 how???? it must be 18          


                byte[] keyBytes ="qwertyuiopasdfgh".getBytes();
                SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");       
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
                byte[] nonce = new byte[GCM_NONCE_LENGTH];
                nonce = "poiuytrewqlk".getBytes();;;        
                GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);            
                byte[] aad = "Whatever I like".getBytes();;         
                cipher.init(Cipher.DECRYPT_MODE, key, spec);     
                cipher.updateAAD(aad);      
                byte[] plainText = cipher.doFinal(cipherText);      
                System.out.println("After decryption "+new String(plainText));
            }
        }catch(Exception e){
            System.out.println("Exception caught "+e);//got Exception caught javax.crypto.AEADBadTagException: Tag mismatch!
        }
     }
}
