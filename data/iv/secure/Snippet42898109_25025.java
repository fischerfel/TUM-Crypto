public class MCrypt
{
    public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    private static String oldKEY = "#K#2sdklfjlas@#$";
    private static String newKEY;

    public static String save_Temp_File_Name_Path = "";
    public static String save_Temp_DIR_Path = "";
    public static String save_Temp_File_Name = "";

    //decode file
    public static String AES_DecodeFile(Context context, String bookID, String extension, byte[] str, String androidID)
            throws Exception
    {

        //----- key will be 16 or 24 or 32 byte.
        //----- android_ID is 15 byte.
        newKEY = oldKEY + androidID;

        Log.d("keySize", newKEY.length() + "/" + newKEY);

        try
        {
            save_Temp_File_Name = "temp_" + bookID + extension.toLowerCase();
//            save_Temp_DIR_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/temp";
            save_Temp_DIR_Path = context.getFilesDir() + "/temp";
//        String save_Temp_DIR_Path = context.getPackageResourcePath();
            save_Temp_File_Name_Path = save_Temp_DIR_Path + "/" + save_Temp_File_Name;

            Log.d("temp file is exist ?", save_Temp_File_Name_Path);
            File folder = new File(save_Temp_DIR_Path);
            if (!folder.exists())
            {
                folder.mkdir();
                Log.d("Temp_DIR_Folder", "Make temp Directory");
            }
            else
            {
                Log.d("Temp_DIR_Folder", "is Exist");
            }

            File data = new File(save_Temp_DIR_Path, save_Temp_File_Name);

            if (!data.exists())
            {
                FileWriter writer = new FileWriter(data);
                writer.flush();
                writer.close();
            }

            byte[] textBytes =Base64.decode(str,0);
            SecretKeySpec newKey = new SecretKeySpec(newKEY.getBytes("ISO_8859_1"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            String textDecoded = new String(cipher.doFinal(textBytes), "ISO_8859_1");
            //-------

            OutputStream out = new FileOutputStream(data);
            out.write(textDecoded.getBytes("ISO_8859_1"));

            Log.d("decoding...", "true");

            return save_Temp_File_Name_Path;
        }
        catch (Exception e)
        {
            Log.d("Exception", e.getMessage());
        }

        return save_Temp_File_Name_Path;
    }

}
