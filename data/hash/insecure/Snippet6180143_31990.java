    private class DownloadAndInstall extends AsyncTask<String, Integer, Boolean>
{
    protected Boolean doInBackground(String... derp)
    {
        String ur=derp[0];
        String fileName=derp[1];
        ByteArrayBuffer baf = new ByteArrayBuffer(50);

        try
        {
            URL url = new URL(ur);
            URLConnection ucon = null;
            ucon = url.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            int current = 0;
            int updateCount=0;
            while ((current = bis.read()) != -1)
            {
                if(updateCount==256)
                {
                    publishProgress(baf.length());
                    updateCount=0;
                }
                baf.append((byte) current);
                updateCount++;
            }

            FileOutputStream fos = PokeDroid.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
            fos.write(baf.toByteArray());
            fos.close();

        } catch (Exception e) {
            Log.e("pokedroid", e.toString());
        }

        MessageDigest digest = null;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("pokedroid", e.toString());
        }
        digest.update(baf.toByteArray());
        byte[] h = digest.digest();

        if(baf.length()==0)
            return null;
        String[] fileList=fileList();
        boolean exists=false;
        for(String i:fileList)
            if(i.equals("updatehash.md5"))
                exists=true;

        String newHash=new String(h);
        Log.e("pokedroid", "new="+newHash);

        if(exists)
        {
            try
            {
                String oldHash=loadObject("updatehash.md5");
                Log.e("pokedroid", "old="+oldHash);
                if(oldHash.equals(newHash))
                    return false;
                else
                    saveObject(newHash, "updatehash.md5");
            }
            catch (Exception e)
            {
                Log.e("pokedroid",e.toString());
            }
        }
        else
        {
            try {
                saveObject(newHash, "updatehash.md5");
            } catch (IOException e) {
                Log.e("pokedroid",e.toString());
            }
        }
        return true;
    }

    protected void onProgressUpdate(Integer...integers)
    {
        p.setMessage("Downloading update...\n"+integers[0]/1000+"kb downloaded so far.");
    }

    protected void onPostExecute(Boolean b)
    {
        if(b==null)
        {
            noConnection.show();
            deleteFile("PokeDroid.apk");
            p.dismiss();
            return;
        }
        if(!b)
            noNewUpdate.show();
        else
        {
            Intent intent=new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file:///data/data/com.games.pokedroid/files/PokeDroid.apk"), "application/vnd.android.package-archive");
            startActivity(intent);
            deleteFile("PokeDroid.apk");
        }
        p.dismiss();
    }

    public void saveObject(String obj, String filename) throws IOException
    {
        FileOutputStream fos = openFileOutput(filename,Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(obj);
        out.close();
        fos.close();
    }

    public String loadObject(String filename) throws StreamCorruptedException, IOException, ClassNotFoundException
    {
        FileInputStream fis=openFileInput(filename);
        ObjectInputStream in=new ObjectInputStream(fis);
        String out=(String) in.readObject();
        in.close();
        fis.close();
        return out;
    }
}
