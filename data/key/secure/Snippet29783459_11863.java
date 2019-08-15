 public static void EncryptFile(Path POld, String key, Path PNew)
{
    try {

        File FileOld = new File(POld.toString());
        FileInputStream file = new FileInputStream(FileOld);
        FileOutputStream outStream = new FileOutputStream(FileOld);
        byte k[]= key.getBytes();
        SecretKeySpec KEYY=new SecretKeySpec(k, "AES");
        Cipher enc = Cipher.getInstance("AES");
        enc.init(Cipher.ENCRYPT_MODE, KEYY);
        CipherOutputStream cos = new CipherOutputStream(outStream,enc);
        byte[] buffer = new byte[1024];
        int read;
        while((read=file.read(buffer))!=-1){
            cos.write(buffer, 0, read);
        }
        file.close();
        outStream.flush();
        cos.close();
       JOptionPane.showMessageDialog(null, "Encrypted!");


    }
    catch(FileNotFoundException e) {
       JOptionPane.showMessageDialog(null, "File Not Found!");
    }
    catch (IOException e){
        JOptionPane.showMessageDialog(null, "IO Exception!");
    }
    catch (NoSuchPaddingException e){
        JOptionPane.showMessageDialog(null, "No such padding!");
    }
    catch (NoSuchAlgorithmException e){
        JOptionPane.showMessageDialog(null, "No Such Algorithm!");
    }
    catch (InvalidKeyException e){
        JOptionPane.showMessageDialog(null, "Invalid Key!");
    }
}
