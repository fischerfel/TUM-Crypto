public static void main(String[] args) throws Exception {

    encrypt(new Scanner(System.in).next());
}
public static String encrypt(String fileToEncrypt) throws Exception{
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);
    SecretKey _keyPair = kgen.generateKey();

    Cipher _cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    _cipher.init(Cipher.ENCRYPT_MODE, _keyPair);

    File inputfile = new File(fileToEncrypt);
    File outputfile = new File("D:/SECUREFILE".concat("/").concat(FilenameUtils.getName(fileToEncrypt)));
    FileInputStream inputstream = new FileInputStream(inputfile);
    FileOutputStream outputStream = new FileOutputStream(outputfile, false);
    CipherOutputStream cos = new CipherOutputStream(outputStream, _cipher);
    IOUtils.copy(inputstream, cos);
    IOUtils.closeQuietly(inputstream);
    IOUtils.closeQuietly(cos);
    return outputfile.getPath();
}
