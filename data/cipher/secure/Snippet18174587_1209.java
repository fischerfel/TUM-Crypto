public static Packet decompile(PacketWrapper wrapper, PrivateKey privateKey)
        throws Exception {
    for (Provider provider : Security.getProviders()) {
        System.out.println(provider.getName());
        System.out.println(provider.getInfo());
        System.out.println(System.lineSeparator());
    }
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] data = cipher.doFinal(wrapper.data);
    return (Packet) bytesToObj(data);
}
