  public static void main(String[]args){

      String imageName[] ={"A01","A02","A03","B01","B02","B03"};

      String imageNameEncrypy[] ={"A001","A002","A003","B001","B002","B003"};

      for(int i=0;i<imageName.length;i++){ 

      try{

        //FileInputStream file;

        FileInputStream  file = new FileInputStream("src/image2/"+imageName[i]+".jpg");

        FileOutputStream output = new FileOutputStream("src/image2/"+imageNameEncrypy[i]+".jpg");

        byte j[]="NiTh5252".getBytes();

        SecretKeySpec kye = new SecretKeySpec(j,"DES");

        System.out.println(kye);

        Cipher enc = Cipher.getInstance("DES");

        enc.init(Cipher.ENCRYPT_MODE,kye);

        CipherOutputStream cos = new CipherOutputStream(output, enc);

        byte[] buf = new byte[1024];

        int read;

        while((read=file.read(buf))!=-1){

            cos.write(buf,0,read);

        }

        file.close();

        output.flush();

        cos.close();

         JOptionPane.showMessageDialog(null,"Suscess");

    }catch(Exception e){

         JOptionPane.showMessageDialog(null,e);

    }

      }

     }
