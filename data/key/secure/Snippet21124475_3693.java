   public class FileChooser extends ListActivity {

    private File currentDir;
    private File moveToDir;
    private FileArrayAdapter adapter;
    public static String TAG = "DEBUG THIS:";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
        registerForContextMenu(getListView());
        // get the attachment's filename
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        String attachmentFileName = preferences.getString("fileName", "");

        // save the attachment
        try {
            InputStream attachment = getContentResolver().openInputStream(
                    getIntent().getData());

            File savedFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), attachmentFileName);
            FileOutputStream f = new FileOutputStream(savedFile);
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = attachment.read(buffer)) > 0) {
                f.write(buffer);
            }
            f.close();
        } catch (Exception e) {
        }
    }

    // File Manager Source to view SD Card or Internal Storage Contents
    private void fill(File f) {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Item> dir = new ArrayList<Item>();
        List<Item> fls = new ArrayList<Item>();
        try {
            for (File ff : dirs) {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if (ff.isDirectory()) {

                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if (fbuf != null) {
                        buf = fbuf.length;
                    } else
                        buf = 0;
                    String num_item = String.valueOf(buf);
                    if (buf == 0)
                        num_item = num_item + " item";
                    else
                        num_item = num_item + " items";

                    // String formated = lastModDate.toString();
                    dir.add(new Item(ff.getName(), num_item, date_modify, ff
                            .getAbsolutePath(), "directory_icon"));
                } else {

                    fls.add(new Item(ff.getName(), ff.length() + " Byte",
                            date_modify, ff.getAbsolutePath(), "file_icon"));
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0, new Item("..", "Parent Directory", "", f.getParent(),
                    "directory_up"));
        adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,
                dir);
        this.setListAdapter(adapter);
    }

    // onClick listener to move back one directory
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if (o.getImage().equalsIgnoreCase("directory_icon")
                || o.getImage().equalsIgnoreCase("directory_up")) {
            currentDir = new File(o.getPath());
            fill(currentDir);
        } else {
            onFileClick(o);
        }
    }

    // open file onClick
    private void onFileClick(Item o) {

        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", o.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Copy");
        menu.add(0, v.getId(), 0, "Paste");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Encrypt");
        menu.add(0, v.getId(), 0, "Decrypt");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Copy") {
            function1(item.getItemId());
        }
        if (item.getTitle() == "Paste") {
            function2(item.getItemId());
        }
        if (item.getTitle() == "Delete") {
            function3(item.getItemId());
        }
        if (item.getTitle() == "Encrypt") {
            function4(item.getItemId());
        }
        if (item.getTitle() == "Decrypt") {
            function5(item.getItemId());
        } else {
        }
        return false;

    }

    public void function1(int id) {
        Toast.makeText(this, "Copy", Toast.LENGTH_SHORT).show();

        try {

            File sd = Environment.getExternalStorageDirectory();
            currentDir = new File(sd.getAbsolutePath());
            moveToDir = new File(sd.getAbsolutePath());
            copyDirectoryOneLocationToAnotherLocation(currentDir, currentDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void function2(int id) {

        Toast.makeText(this, "Paste", Toast.LENGTH_SHORT).show();
    }

    public void function3(int id) {
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
        DeleteRecursive(currentDir);
    }

    public void function4(int id) {
        Toast.makeText(this, "Encrypt", Toast.LENGTH_SHORT).show();
        //encrypt(null, null, null);
        // encrypt(String PlaintextFile, String Key, String CiphertextFile);
          encrypt("D:\\plaintext.txt", "testkey", "D:\\ciphertext.txt");
    }

    public void function5(int id) {
        Toast.makeText(this, "Decrypt", Toast.LENGTH_SHORT).show();
        decrypt(null, null, null);
        // decrypt(String PlaintextFile, String Key, String CiphertextFile);

    }

    private void DeleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (null != children) {
                for (File child : children)
                    DeleteRecursive(child);
            }
        }

        fileOrDirectory.delete();

    }

    public static void copyDirectoryOneLocationToAnotherLocation(
            File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectoryOneLocationToAnotherLocation(new File(
                        sourceLocation, children[i]), new File(targetLocation,
                        children[i]));
            }
        } else {

            InputStream in = new FileInputStream(Environment
                    .getExternalStorageDirectory().getPath());

            OutputStream out = new FileOutputStream(Environment
                    .getExternalStorageDirectory().getPath());
            Log.d("TEST", "source: " + in);
            Log.d("TEST", "target: " + out);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

    public static void encrypt(String PlaintextFile, String Key,
            String CiphertextFile) {
        try {
            byte[] KeyData = Key.getBytes();
            SecretKeySpec KS = new SecretKeySpec(KeyData, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, KS);
            FileInputStream fis = new FileInputStream(PlaintextFile);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            FileOutputStream fos = new FileOutputStream(CiphertextFile);
            byte[] b = new byte[1024];
            int i = cis.read(b);
            while (i != -1) {
                fos.write(b, 0, i);
                i = cis.read(b);
            }
            fos.flush();
            fos.close();
            fis.close();
            cis.close();
            System.out.println("Encryption Successfull !!!");

        } catch (NoSuchAlgorithmException exception) {
            // do something.

            Thread.currentThread().getStackTrace();
        } catch (NoSuchPaddingException exception) {
            // do something (else?).
            Thread.currentThread().getStackTrace();
        } catch (InvalidKeyException exception) {
            // do something.
            Thread.currentThread().getStackTrace();
        } catch (IOException exception) {
            // do something (else?).
            Thread.currentThread().getStackTrace();

        }

    }

    public static void decrypt(String CiphertextFile, String Key,
            String DecipheredFile) {
        try {
            byte[] KeyData = Key.getBytes();
            SecretKeySpec KS = new SecretKeySpec(KeyData, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, KS);
            FileInputStream fis = new FileInputStream(CiphertextFile);
            FileOutputStream fos = new FileOutputStream(DecipheredFile);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            byte[] b = new byte[1024];
            int i = fis.read(b);
            while (i != -1) {
                cos.write(b, 0, i);
                i = fis.read(b);
            }
            fos.flush();
            fos.close();
            fis.close();
            cos.flush();
            cos.close();
            System.out.println("Decryption Successfull !!!");

        } catch (NoSuchAlgorithmException exception) {
            // do something.

            Thread.currentThread().getStackTrace();
        } catch (NoSuchPaddingException exception) {
            // do something (else?).
            Thread.currentThread().getStackTrace();
        } catch (InvalidKeyException exception) {
            // do something.
            Thread.currentThread().getStackTrace();
        } catch (IOException exception) {
            // do something (else?).
            Thread.currentThread().getStackTrace();

        }

    }
     public static void main(String[] args) {
            encrypt("D:\\plaintext.txt", "testkey", "D:\\ciphertext.txt");
            decrypt("D:\\ciphertext.txt", "testkey", "D:\\originaltext.txt");
        }
}
