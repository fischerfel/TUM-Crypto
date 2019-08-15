import java.io.File;

public class ProgressBarExa extends Activity {

Button btnStartProgress;
ProgressDialog progressBar;
private int progressBarStatus = 0;
private Handler progressBarHandler = new Handler();

// private long fileSize = 0;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.progressbar_view);

    addListenerOnButton();

}

public void addListenerOnButton() {

    btnStartProgress = (Button) findViewById(R.id.btnStartProgress);
    btnStartProgress.setOnClickListener(
             new OnClickListener() {

       @Override
       public void onClick(View v) {

        // prepare for a progress bar dialog
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("File encrypting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();

        //reset progress bar status
        progressBarStatus = 0;

        //reset filesize
        // fileSize = 0;

        new Thread(new Runnable() {
          public void run() {
            while (progressBarStatus < 100) {

              // process some tasks
              progressBarStatus = doSomeTasks();

              // your computer is too fast, sleep 1 second
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }

              // Update the progress bar
              progressBarHandler.post(new Runnable() {
                public void run() {
                  progressBar.setProgress(progressBarStatus);
                }
              });
            }

            // ok, file is downloaded,
            if (progressBarStatus >= 100) {

                // sleep 2 seconds, so that you can see the 100%
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // close the progress bar dialog
                progressBar.dismiss();
            }
          }
           }).start();

           }

            });

    }

// file download simulator... a really simple
public int doSomeTasks() {

    try{
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "a.wmv";
        String newFileNEE = "b.wmv";
        String newFileNED = "c.wmv";

        FileInputStream fis = new FileInputStream(new File(baseDir + File.separator + fileName));

        File outfile = new File(baseDir + File.separator + newFileNEE);
            int read;
            if(!outfile.exists())
                outfile.createNewFile();

            // long outfile_size = outfile.length();

            File decfile = new File(baseDir + File.separator + newFileNED);
            if(!decfile.exists())
                decfile.createNewFile();


            FileOutputStream fos = new FileOutputStream(outfile);
            FileInputStream encfis = new FileInputStream(outfile);
            FileOutputStream decfos = new FileOutputStream(decfile);

            Cipher encipher = Cipher.getInstance("AES");
            Cipher decipher = Cipher.getInstance("AES");

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecretKey skey = kgen.generateKey();
            encipher.init(Cipher.ENCRYPT_MODE, skey);
            CipherInputStream cis = new CipherInputStream(fis, encipher);
            decipher.init(Cipher.DECRYPT_MODE, skey);
            CipherOutputStream cos = new CipherOutputStream(decfos,decipher);

            while((read = cis.read())!=-1)
                    {
                        fos.write((char)read);
                        fos.flush();
                    }   
            fos.close();
            while((read=encfis.read())!=-1)
            {
                cos.write(read);
                cos.flush();
            }
            cos.close();

    }catch (Exception e) {
        // TODO: handle exceptione
        e.printStackTrace();
    }
    return 100;
}
