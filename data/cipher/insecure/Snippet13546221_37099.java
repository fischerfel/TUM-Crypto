public static void main(String args[]) {
    Cipher fcipher, scipher;
    String plaintextstring = "";

    System.out.println("Enter the first message:");
    BufferedReader buffp = new BufferedReader(new InputStreamReader(System.in));
    try {
        plaintextstring = buffp.readLine();
    } catch (Exception e) {
        System.out.println("Exception occured:" + e.getMessage());
    }

    int strlen1 = plaintextstring.length();

    int x1 = strlen1 % 8;

    int y1 = 8 - x1;

    StringBuffer buf1 = new StringBuffer(plaintextstring);

    if (x1 > 0) {
        for (int k = 0; k < y1; k++) {
            buf1.append('0');
        }
    }
    System.out.println("Modified plaintext is:" + buf1);
    String inp = buf1.toString();

    try {

        SecretKey key1 = KeyGenerator.getInstance("DES").generateKey();
        SecretKey key2 = KeyGenerator.getInstance("DES").generateKey();

        fcipher = Cipher.getInstance("DES/ECB/NoPadding");//CBC
        scipher = Cipher.getInstance("DES/ECB/NoPadding");//CBC

        // Create an 8-byte initialization vector 
        //byte[] iv = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        //byte[] iv = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        byte[] iv = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08};
        System.out.println("Length of IV is :" + iv.length);



        byte[] sencrypted = new byte[8];

        fcipher.init(Cipher.ENCRYPT_MODE, key1);
        scipher.init(Cipher.DECRYPT_MODE, key2);
        System.out.println("Default Charset is :" + Charset.defaultCharset());
        byte[] pbytes = inp.getBytes("UTF-8");
        System.out.println("Byte array of input string is :" + pbytes);

        System.out.println("Size of first message in bytes is:" + pbytes.length);
        for (int i = 0; i < pbytes.length; i++) {
            System.out.println("Array values of byte array are: " + pbytes[i]);
        }

        int z = (pbytes.length) / 8;
        System.out.println("Number of data blocks of 8 bytes formed from message = " + z);
        for (int i = 0; i < z; i++) {
            byte[] ds = getSection(pbytes, i * 8);//getting block of 64bit
            byte[] out = new byte[8];

            for (int r = 0; r < 8; r++) {
                System.out.println("Array values of IV from previous stage is :" + iv[r]);
            }
            for (int k = 0; k < 8; k++) {
                out[k] = (byte) (ds[k] ^ iv[k]);//XORing of message block with IV bit by bit.
                // System.out.println("XORed array byte by byte is:"+out[k]);
            }
            byte[] fencrypted = fcipher.doFinal(out);//Applying DES Encryption to the XOR'ed result.(E)key1
            byte[] fdecrypted = scipher.doFinal(fencrypted);// (D)key2 
            sencrypted = fcipher.doFinal(fdecrypted);// (E)key1


            System.out.println("Encrypted byte length: " + sencrypted.length);
            System.out.println("Encrypted text is :" + sencrypted);

            fcipher.init(Cipher.DECRYPT_MODE, key1);
            scipher.init(Cipher.ENCRYPT_MODE, key2);

            byte[] sfdecrypted = fcipher.doFinal(sencrypted);//DES1 key1 (D)
            byte[] sfencrypted = scipher.doFinal(sfdecrypted);//DES Key2 (E)
            byte[] ssdecrypted = fcipher.doFinal(sfencrypted);//DES Key3  (D)
            System.out.println("length of final decrypted byte array is :" + ssdecrypted.length);
            byte[] d = new byte[8];
            for (int u = 0; u < 8; u++) {
                d[u] = (byte) (ssdecrypted[u] ^ iv[u]);//XORing of message block with IV bit by bit.
                System.out.println("final decrypted array byte by byte of a single 64 bit block is:" + d[u]);
            }
            String sdecryptedstr = new String(d);
            System.out.println("Decrypted block is :" + sdecryptedstr);
            iv = sencrypted;
            System.out.println("IV from previous stage is: "+iv); 
        }


    } catch (Exception e) {
        System.out.println("Exception Occured: " + e);
    }

}
//function to get CBC message blocks.
public static byte[] getSection(byte[] message, int start) {
    byte[] section = new byte[8];//dividing whole message into 64bit blocks.
    for (int i = 0, j = start; i < 8; i++, j++) {
        section[i] = message[j];
    }
    return section;
}//end of getSection function.
