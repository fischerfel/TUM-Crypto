public static ArrayList<byte[]> encodeFile(SecretKey yourKey, byte[] fileData)
        throws Exception {

    byte[] encrypted = null;

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, yourKey);
    AlgorithmParameters params = cipher.getParameters();
    byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    encrypted = cipher.doFinal(fileData);   

    ivandcipher.clear();
    ivandcipher.add(iv);
    ivandcipher.add(encrypted);

    return ivandcipher;
}
