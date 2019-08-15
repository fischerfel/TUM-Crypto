public static String MD5(String md5) {
       try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
            byte[] array = null;
            try {
                array = md.digest(md5.getBytes());
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
              sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
           }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static byte[] readExe(Path path)
    {
        byte[] fileByte = null;
        try {
            fileByte = Files.readAllBytes(path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileByte ;

    }
    public static  void write_signature_to_textFile(String location, String data)
    {
        String signLocation = "d:\\Users\\user-pc\\Desktop\\magshimim\\year 3\\signs.txt";
        File file =new File(signLocation);
        //if file doesn't exists, then create it
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file,true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedWriter bufferWritter = new BufferedWriter(fw);
        try {
            bufferWritter.write(location);
            bufferWritter.write(":");
            bufferWritter.newLine();
            bufferWritter.write(data);
            bufferWritter.newLine();
            bufferWritter.newLine();
            bufferWritter.newLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            bufferWritter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void write_signature_to_binaryFile(String location, byte[] data)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(location);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            out.write(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void create_list_of_dexFiles(String pathToScan, String ending)
    {
        String target_file = "";
        File folderToScan = new File(pathToScan);
        File[] listOfFiles = folderToScan.listFiles();
        for (int i = 0; i < listOfFiles.length; i++)
        {
             if (listOfFiles[i].isFile())
             {
                 target_file = listOfFiles[i].getAbsolutePath();
                 if (target_file.endsWith("."+ending))
                 {
                     //check whether trget_file product path with / or // . if / write script to change to //
                        byte[] exe_file_bytes = null;
                        String exe_location = target_file ;
                        Path exe_path = Paths.get(exe_location);
                        exe_file_bytes = readExe(exe_path);
                        String string_exe_file_bytes = new String(exe_file_bytes);
                        String result = MD5(string_exe_file_bytes);
                        System.out.println(result);
                        write_signature_to_textFile(exe_location, result);

                 }  
             }
             else if(listOfFiles[i].isDirectory())
             {
                 create_list_of_dexFiles(listOfFiles[i].getAbsolutePath(), ending);
             }
        }
        return;
    }

    public static void main(String[] args) 
    {

        String pathToScan = "d:\\Users\\user-pc\\Desktop\\magshimim\\year 3\\Android Viruses";
        create_list_of_dexFiles(pathToScan, "dex");
