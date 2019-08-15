public static void main(String[] args) throws IOException,
        NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException {

    Scanner sc = new Scanner(System.in);
    String filename = sc.nextLine();
    sc.close();

    System.out.println("The file requested is " + filename);

    File file = new File(filename);

    if (file.exists())
        System.out.println("File found");

    File to_b_encf = new File("encrypted.txt");

    if (!to_b_encf.exists())
        to_b_encf.createNewFile();

    System.out.println("encrypting");

    Cipher encipher = Cipher.getInstance("AES");
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    SecretKey key = keygen.generateKey();

    encipher.init(Cipher.ENCRYPT_MODE, key);

    FileOutputStream output = new FileOutputStream(to_b_encf);
    FileInputStream input = new FileInputStream(filename);
    CipherInputStream cis = new CipherInputStream(input, encipher);

    int read;

    while ((read = cis.read()) != -1) {
        output.write(read);
        output.flush();
    }

    input.close();
    output.close();

    System.out.println("done");
    System.out.println("decrypting");

    Cipher decipher = Cipher.getInstance("AES");//initiate a cipher for decryption
    decipher.init(Cipher.DECRYPT_MODE, key);//decrypt the file 

    File sourcefile = new File("encrypted.txt");
    File destfile = new File("decrypted.txt");

    if (!destfile.exists())
        destfile.createNewFile();

    FileInputStream decf = new FileInputStream(sourcefile);
    CipherInputStream c_decf = new CipherInputStream(decf,decipher);
    FileOutputStream destf = new FileOutputStream(destfile);

    cout = new CipherOutputStream(destf,decipher);

    while ((read = c_decf.read()) != -1) {
        cout.write(read);
        cout.flush();
    }

    c_decf.close();
    destf.close();
    cout.close();
    decf.close();
    System.out.println("done");
}
