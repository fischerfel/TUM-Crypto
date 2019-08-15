          private CharSequence getHash(String sourceDir)  {
          // TODO Auto-generated method stub

          File file = new File(packageInfo.applicationInfo.sourceDir);
         String outputTxt= "";
           String hashcode = null;

        try {

        FileInputStream input = new FileInputStream(file);

        ByteArrayOutputStream output = new ByteArrayOutputStream ();
        byte [] buffer = new byte [65536];
        int l;


              while ((l = input.read (buffer)) > 0)
                  output.write (buffer, 0, l);

                  input.close ();
              output.close ();

                byte [] data = output.toByteArray ();

                MessageDigest digest = MessageDigest.getInstance( "SHA-1" ); 

            byte[] bytes = data;

            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            StringBuilder sb = new StringBuilder();

            for( byte b : bytes )
            {
                sb.append( String.format("%02X", b) );
            }

            hashcode = sb.toString();


         } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
         } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
         } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
       }



          return hashcode;
         }
