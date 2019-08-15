class CheckDex{
public boolean checkSHA1(File f) throws IOException, NoSuchAlgorithmException{
    RandomAccessFile raf = new RandomAccessFile(f, "r");
    byte[] sig = new byte[20];
    raf.seek(0xC);
    for(int i = 0; i < 20; i++){
        sig[i] = (byte) raf.readUnsignedByte();
    }

    MessageDigest md = MessageDigest.getInstance("SHA-1"); 

    byte[] code = new byte[(int) (raf.length()-32)];
    for(int i = 0; i < code.length; i++){
        code[i] = (byte) raf.readUnsignedByte();
    }
    byte[] comsig = md.digest(code);

    raf.close();
    return Arrays.equals(sig,comsig);
}
}
