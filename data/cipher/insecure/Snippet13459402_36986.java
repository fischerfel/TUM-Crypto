if (packet instanceof IPPacket) {

    IPPacket ipp = (IPPacket) packet;
    InetAddress dest = ipp.dst_ip;
    KeyGenerator keygenerator;

    try {
        keygenerator = KeyGenerator.getInstance("DES");
        SecretKey myDesKey = keygenerator.generateKey();
        Cipher desCipher;
        // Create the cipher
        desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        byte[] ipEncrypted = desCipher.doFinal(ipp.dst_ip.getAddress());
        InetAddress src = ipp.src_ip;
        //   System.out.println(dest);
        try {
            ipp.dst_ip = InetAddress.getByAddress(ipEncrypted);
        } catch(Exception e) {
             System.out.println(e.getMessage());
        }
        ipp.src_ip = src;
    } catch(Exception ex ) {
        System.out.println(ex.getMessage());
    }
