public class VideoActivity extends Activity 
{
    VideoView video_view;
    MediaController m_controller;
    MediaPlayer mplayer;
    String[] a = {"1","2","3","4","5","6"};

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        try {
            main(a);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

        public void writeToFile(String filename, Object object) throws Exception 
        {
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try 
            {
                fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/corebird1.mp4"));
                oos = new ObjectOutputStream(fos);
                oos.writeObject(object);
                oos.flush();
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            finally 
            {
                if (oos != null) 
                {
                    oos.close();
                }
                if (fos != null) 
                {
                    fos.close();
                }
            }
        }

        public void main(String[] args) throws Exception 
        {
            //
            // Generating a temporary key and stire it in a file.
            //
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            writeToFile("secretkey.dat", key);
            Log.d("#######key", String.valueOf(key));

            //
            // Preparing Cipher object for encryption.
            //
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            //
            // Here we seal (encrypt) a simple string message (a string object).
            //
            SealedObject sealedObject = new SealedObject("THIS IS A SECRET MESSAGE!", cipher);

            //
            // Write the object out as a binary file.
            //
            writeToFile("sealed.dat", sealedObject);
        } 
}
