public static void main(String args[]) throws NoSuchAlgorithmException, IOException{

    Scanner sc = new Scanner(System.in);  
    System.out.println("Nome fo ficheiro:");
    String fileName = sc.nextLine();
    byte [] byteMsg = getByteMsg(fileName);


    //* Declare Encriptor--> Cipher//
    Cipher cifrador = null;
    try {
         cifrador = Cipher.getInstance("DES");
    } catch (NoSuchPaddingException ex) {
        System.out.println("Problema na mensagem");
    }


    //* Generate key -->  KeyGenerator*//
    KeyGenerator kgen = null;
    try{
        kgen = KeyGenerator.getInstance("DES");
    }
    catch(NoSuchAlgorithmException e){
        System.out.println("Algoritmo de encriptação não encontrado");
    }
    SecretKey key = kgen.generateKey();


    //* Inicialize Encritptor and encrypt message* --> Cipher//
    try {
        cifrador.init(1, key);   //1 = Cipher.ENCRYPT_MODE

    } catch (InvalidKeyException ex) {
        System.out.println("Chave de cifragem inválida");
    }
    byte[]byteMsgCod = null;
    try {
        byteMsgCod = cifrador.doFinal(byteMsg);
    } catch (IllegalBlockSizeException ex) {
        System.out.println("Tamanho de Bloco Ilegal");
    } catch (BadPaddingException ex) {
        System.out.println("Bad Padding");
    }  



    //* Write encrypted message in the file*//
    PrintWriter pw = new PrintWriter(fileName);
    String msgCod = new String(byteMsgCod);
    System.out.println(msgCod);
    pw.println(msgCod);
    pw.close();



    /* Read text from file */   
}
private static byte[] getByteMsg(String fileName) throws IOException{
    //Ler msg do ficheiro
    Scanner sc = new Scanner(System.in);
    FileInputStream fis = null;

    try {
        fis = new FileInputStream(fileName);
    } catch (FileNotFoundException ex) {
        System.out.println("Ficheiro não encontrado");
    }

    byte[] byteMsg = new byte[fis.available()];

    fis.close();

    return byteMsg;
}
