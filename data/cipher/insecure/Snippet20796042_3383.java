public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    // TODO Auto-generated method stub
            String FileName="encryptedtext.txt";
            String FileName2="decryptedtext.txt";
            String pad="0"; 

            KeyGenerator KeyGen=KeyGenerator.getInstance("AES");
            KeyGen.init(128);

            SecretKey SecKey=KeyGen.generateKey();

            Cipher AesCipher=Cipher.getInstance("AES");
            AesCipher.init(Cipher.ENCRYPT_MODE,SecKey);

            byte[] byteText="My name is yogesh".getBytes();
            byte[] byteCipherText=AesCipher.doFinal(byteText);
            String cipherText = null;

            try {
                FileWriter fw=new FileWriter(FileName);
                BufferedWriter bw=new BufferedWriter(fw);
                bw.write(byteCipherText.toString());
                bw.close();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                FileReader fr=new FileReader(FileName);
                BufferedReader br=new BufferedReader(fr);
                cipherText=br.readLine();
                br.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            AesCipher.init(Cipher.DECRYPT_MODE,SecKey);
            while(((cipherText.getBytes().length)%16)!=0)
            {
                cipherText=cipherText+pad;


            }

            byte[] bytePlainText=AesCipher.doFinal(cipherText.getBytes());
            FileWriter fw1;
            try {
                fw1 = new FileWriter(FileName2);
                BufferedWriter bw1=new BufferedWriter(fw1);
                bw1.write(bytePlainText.toString());
                bw1.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }






}
