public class Download extends Observable implements Runnable {
    private static final int MAX_BUFFER_SIZE = 1024;        
    public static final int DOWNLOADING = 0;    
    public static final int PAUSED = 1; 
    public static final int COMPLETE = 2;   
    public static final int CANCELLED = 3;  
    public static final int ERROR = 4;  
    private URL url; 
    private int size; 
    private int downloaded; 
    private int status; 

    public Download(URL url){
        this.url = url;
        size=-1;
        downloaded=0;
        status=DOWNLOADING;
        download();
    }

    public String getURL(){
        return url.toString();
    }   

    public void error(){
        status = ERROR;
        stateChanged();
    }

    private void download(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {     
        RandomAccessFile randomAccessFile = null;
        InputStream inputStream = null;
        try{
            HttpsURLConnection connection = getSSLCertficate(getURL());
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range","bytes=" + downloaded + "-");
            connection.connect();
            if(connection.getResponseCode()/100 != 2){
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
            randomAccessFile = new RandomAccessFile("/home/user/Desktop/dotproject-2.1.5.tar.gz","rw");         
            randomAccessFile.seek(downloaded);
            inputStream = connection.getInputStream();
            while(status == DOWNLOADING){
                byte buffer[];
                int finalSize=size - downloaded;
                if ( finalSize > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                int read = inputStream.read(buffer);
                if(read == -1)
                    break;              

                randomAccessFile.write(buffer, 0, read);                
                downloaded += read;             
                stateChanged();
            }           

        }catch (Exception e) {

        }
    }

    private void stateChanged(){
        setChanged();
        notifyObservers();
    }

    private HttpsURLConnection getSSLCertficate(String urlPath) throws NoSuchAlgorithmException, KeyManagementException, IOException{
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
        Download d = new Download(new URL("https://192.16.1.130/Source.tar.gz"));
        d.run();
    }

}
