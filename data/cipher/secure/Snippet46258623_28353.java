static void SO46244541CryptAsURL (String... args) throws Exception {
    // arguments: data pubkeyfile(der) prvkeyfile(der) flag(if present specify 8859-1 on conversion)
    String clear = args[0];
    KeyFactory fact = KeyFactory.getInstance("RSA");
    Cipher objCipherTunkicloud = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    // encrypt side
    objCipherTunkicloud.init(Cipher.ENCRYPT_MODE, fact.generatePublic(new X509EncodedKeySpec(read_file(args[1]))));
    byte[] arrDecryptedKeyBytes = objCipherTunkicloud.doFinal(clear.getBytes(StandardCharsets.UTF_8));
    // for correct result must enable flag and specify 8859-1 on ctor
    String tkn = args.length>3? new String(arrDecryptedKeyBytes,StandardCharsets.ISO_8859_1): new String(arrDecryptedKeyBytes);
    String output = URLEncoder.encode(tkn, "ISO-8859-1");
    System.out.println (output);
    // decrypt side
    String temp = URLDecoder.decode(output, "ISO-8859-1");
    //reused: Cipher objCipherTunkicloud = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    objCipherTunkicloud.init(Cipher.DECRYPT_MODE, fact.generatePrivate(new PKCS8EncodedKeySpec(read_file(args[2]))));
    arrDecryptedKeyBytes = objCipherTunkicloud.doFinal(temp.getBytes(StandardCharsets.ISO_8859_1));
    System.out.println (new String(arrDecryptedKeyBytes));
}

public static byte[] read_file (String filename) throws Exception {
    return Files.readAllBytes(new File(filename).toPath());
}
