public static PacketWrapper compile(Packet packet, PublicKey publicKey)
        throws Exception {
    byte[] bytes = objToBytes(packet);
    System.out.println("Size > " + bytes.length);

    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] data = cipher.doFinal(bytes);
    return new PacketWrapper(data);
}    
