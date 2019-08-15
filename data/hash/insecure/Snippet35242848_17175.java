public class ImageCacher  {


        public static Bitmap getImage(final Context con,  final String Image)throws NoSuchAlgorithmException, Exception {


            if (Patterns.WEB_URL.matcher(Image.trim()).matches()) {

                if ((ImageCache.ifExist(Image))) {
                    Toast.makeText(con.getApplicationContext(), "Image is already in Cache", Toast.LENGTH_SHORT).show();
                    return ImageCache.showImage(Uri.parse(Image).getLastPathSegment());

                } else {


                    new AsyncTask<Void, Void, Boolean>() {
                        protected Boolean doInBackground(Void... params) {
                            ImageCache.download(con.getApplicationContext(), Image);

                            return null;


                        }

                    }.execute();
                    Toast.makeText(con.getApplicationContext(), "Image has been downloaded", Toast.LENGTH_SHORT).show();


                }
                while (ImageCache.spaceInMB(ImageCache.filePathFolder) > 50) {
                    ImageCache.deleteLastModified(ImageCache.filePathFolder, con.getApplicationContext());
                }
                //doesnt work 
                //String s= testChecksum(new File (filepathfolder, (Uri.parse(Image).getLastPathSegment()).trim() ) 

                return null;
            }else   Toast.makeText(con.getApplicationContext(), "Please insert valid Url adress", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


 static String testChecksum(File outputF) throws  IOException, NoSuchAlgorithmException



    {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream (outputF);

            byte[] data = new byte[1024];
            int read = 0;
            while ((read = fis.read(data)) != -1) {
                md5.update(data, 0, read);
            };
            byte[] hashBytes = md5.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1)); 
            }

            String fileHash = sb.toString();

            return fileHash;
        }
