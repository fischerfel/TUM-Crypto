    Is it possible and how to do the same.Any help will be highly appreciated.


String FILENAME ="textFile.txt";
    String strMsgToSave = "content check";
    FileOutputStream fos;
    String outputTxt= "";
    String hash = null;
    String exception=null;
    try
    {
        fos = this.openFileOutput( FILENAME, Context.MODE_PRIVATE );
        fos.write( strMsgToSave.getBytes() );
        fos.close();

        FileInputStream input = openFileInput(FILENAME);
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
        hash = sb.toString();
        txtOutput.setText(hash);

    } catch (Exception e) {

        e.printStackTrace();
    }
