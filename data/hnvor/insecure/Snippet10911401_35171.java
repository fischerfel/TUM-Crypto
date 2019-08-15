public class Download extends Observable implements Runnable {
    private static final int MAX_BUFFER_SIZE = 1024;
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1; 
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;

    private URL url; // download URL    
    private static float size; // size of download in bytes 
    private int downloaded; // number of bytes downloaded   
    private int status; // current status of download
    private String location;    

    public Download(URL url,String location){
        this.url = url;
        size=-1;
        downloaded=0;
        status=DOWNLOADING;
        this.location=location;
        download();
    }

    public String getURL(){
        return url.toString();
    }

    public static float getSize(){
        return size;
    }

    public float getProgress(){
        return  ((float) downloaded / size) * 100;
    }

    public void pause(){
        status = PAUSED;
        stateChanged();
    }

    public void resume(){
        status = DOWNLOADING;
        stateChanged();
        download();
    }

    public void cancel(){
        status = CANCELLED;
        stateChanged();
    }

    public void error(){
        status = ERROR;
        stateChanged();
    }

    private String getFileName(URL url){
        String filepath = url.getFile(); 
        int slashIndex = filepath.lastIndexOf("/");
        String fileName = filepath.substring(slashIndex+1,filepath.length());
        String downloadPath = location+"/"+fileName;
        return downloadPath;
     }

    private void download(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {     
        RandomAccessFile randomAccessFile = null;
        InputStream inputStream = null;
        int responseCode = 0;
        try{
            HttpsURLConnection connection = getSSLCertificate(getURL());
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range","bytes=" + downloaded + "-");
            connection.connect();
            responseCode=connection.getResponseCode();
            if(responseCode/100 != 2){
                error();
            }
            int contentLength = connection.getContentLength();
            if(contentLength <1){
                error();
            }
            if(size == -1){
                size = contentLength;
                stateChanged();
            }
            randomAccessFile = new RandomAccessFile(getFileName(url),"rw");
            randomAccessFile.seek(downloaded);
            inputStream = connection.getInputStream();
            while(status == DOWNLOADING){
                //byte buffer[]=new byte[3000000];
                byte buffer[];
                float finalSize=size - downloaded;
                if ( finalSize > MAX_BUFFER_SIZE) {
                    buffer = new byte[(int) finalSize];
                } else {
                    buffer = new byte[MAX_BUFFER_SIZE];
                }
                int read = inputStream.read(buffer);
                if(read == -1)
                    break;

                randomAccessFile.write(buffer, 0, read);
                downloaded += read;
                stateChanged();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void stateChanged(){
        setChanged();
        notifyObservers();
    }

    private HttpsURLConnection getSSLCertificate(String urlPath) throws NoSuchAlgorithmException, KeyManagementException, IOException{
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0],new TrustManager[]{new DefaultTrustManager()},new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL(urlPath);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

        return conn;
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println("Enter the URL and Press ENTER:");
        Scanner scanner = new Scanner(System.in);
        String link = scanner.nextLine();
        System.out.println("Enter the destination location and Press ENTER:");
        String destination=scanner.nextLine();
        if(link.length() >0 && destination.length()>0){
            Download d = new Download(new URL(link),destination);
            System.out.println("Downloading... ");
            d.run();
            System.out.println("Downloaded Successfully" +" " +"Size:"+(getSize()/1048576) + " MB");
         }else{
             System.out.println("Error! Please provide url and destination location");
             System.exit(1);
         }
    }
