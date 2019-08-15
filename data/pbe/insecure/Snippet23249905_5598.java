public static void main(String[] arg) throws Exception {

    Scanner scanner = new Scanner(System.in);
    byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
               (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

    {
        File inputFile = new File("C:/Users/AMD/Desktop/bhp/pngg.jpg");
        BufferedImage input = ImageIO.read(inputFile);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

        PBEKeySpec pbeKeySpec = new PBEKeySpec("pass".toCharArray());          

        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        FileOutputStream output = new FileOutputStream("C:/Users/AMD/Desktop/bhp/encrpngg.png");
        CipherOutputStream cos = new CipherOutputStream(output, pbeCipher);

        ImageIO.write(input,"PNG",cos);
        cos.close();
        inputFile.delete();

    }

    {
        PBEKeySpec pbeKeySpec = new PBEKeySpec("pass".toCharArray());

        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 20);
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

        File inFile=new File("C:/Users/AMD/Desktop/bhp/encrpngg.png");
        FileInputStream fis=new FileInputStream(inFile);
        CipherInputStream cis=new CipherInputStream(fis, pbeCipher);
        BufferedImage inpt=ImageIO.read(cis);
        cis.close();
        FileOutputStream output = new FileOutputStream("C:/Users/AMD/Desktop/bhp/decrpngg.jpg");
        ImageIO.write(inpt,"PNG",  output);

    }
}
