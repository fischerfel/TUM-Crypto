    int k=i;

    String questionImage[]={"","B001","B002","B003"};

    String questionDecryptImageName=questionImage[k];

    String afterDrcryptName[]={"A.jpg","B.jpg","C.jpg","D.jpg"};

    try{

        FileInputStream file = new FileInputStream("src/learning/dvd/Temp2/"+questionDecryptImageName+".jpg");

    FileOutputStream output = new FileOutputStream("src/learning/dvd/Temp2/"+afterDrcryptName[k]);

        byte j[]="NiTh5252".getBytes();

        SecretKeySpec kye = new SecretKeySpec(j,"DES");

        System.out.println(kye);

        Cipher enc = Cipher.getInstance("DES");

        enc.init(Cipher.DECRYPT_MODE,kye);

        CipherOutputStream cos = new CipherOutputStream(output, enc);

        byte[] buf = new byte[1024];

        int read;

        while((read=file.read(buf))!=-1){

            cos.write(buf,0,read);

        }

        file.close();

        cos.close();

        output.flush();  

    }catch(Exception e){

        JOptionPane.showMessageDialog(null, e);

    }

}  
