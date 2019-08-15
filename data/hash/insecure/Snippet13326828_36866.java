    public ZipCode(Context ctx) {
    this.ctx = ctx;
    if (!databaseExist(ctx)) {
        Log.d("ZipCode", "DB DNE");
        inflate_db(ctx);
        check_DB(ctx);
    } else {
        Log.d("ZipCode", "DB Exsits");
        check_DB(ctx);
    }

}
private static void inflate_db(Context ctx) {
        byte[] buffer = new byte[2048];
        int length;
        AssetManager am = ctx.getAssets();
        try {
            BufferedInputStream is = new BufferedInputStream(
                    am.open(ZIPCODE_SQLITE_FAUX_FILE));
            GZIPInputStream zis = new GZIPInputStream(is);
            File dbfile = ctx.getDatabasePath(ZIPCODE_SQLITE);
            FileOutputStream out = new FileOutputStream(dbfile);
            while ((length = zis.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
        }
    }

private static void check_DB(Context ctx) {
        File dbfile = ctx.getDatabasePath(ZIPCODE_SQLITE);
        FileInputStream fis;
        MessageDigest digester;
        byte[] bytes = new byte[8192];
        int byteCount;
        try {
            digester = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(dbfile);
            while ((byteCount = fis.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            String digest = Base64.encodeBytes(digester.digest());
            Log.d("MD5 Sum", digest);
            fis.close();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
