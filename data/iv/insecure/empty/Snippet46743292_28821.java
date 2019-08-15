    String key22 = myKey;
    byte[] b = key22.getBytes();

    final SecretKey key = new SecretKeySpec(b, "DESede");
    final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
    final Cipher decipher = Cipher.getInstance("DESede/CBC/NoPadding");
    decipher.init(Cipher.DECRYPT_MODE, key, iv);    
    final byte[] plainText = decipher.doFinal(file_encrypt);


    try {
        String dir = Environment.getExternalStorageDirectory() + File.separator + ".android";
        String dir2 = Environment.getExternalStorageDirectory() + File.separator + ".android/.androidmain";
        File folder = new File(dir); //folder name
        File folder2 = new File(dir2); //folder name
        if (!folder.exists())
            folder.mkdirs();
        if (!folder2.exists())
            folder2.mkdirs();

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + ".android/.androidmain/file");


        if (file.exists()) {
          //  Toast.makeText(contInst, "111", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(contInst, "3333", Toast.LENGTH_SHORT).show();
            file.createNewFile();
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));


        bos.write(plainText);
        bos.flush();
        bos.close();          
       videoplay.setSource(Uri.fromFile(file));

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }


    return "ok";
}
