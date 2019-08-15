public class AudioTransmitter extends Thread{

    private MicrophoneManager mm=null;
    private boolean transmittingAudio = false;
    private String host;
    private int port;
    private long id=0;
    boolean run=true;

    public AudioTransmitter(String host, int port, long id) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.start();
    }

    public void run() {

        System.out.println("creating audio transmitter host "+host+" port "+port+" id "+id);

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain, String authType) {
                        for (int j=0; j<chain.length; j++)
                        {
                            System.out.println("Client certificate information:");
                            System.out.println("  Subject DN: " + chain[j].getSubjectDN());
                            System.out.println("  Issuer DN: " + chain[j].getIssuerDN());
                            System.out.println("  Serial number: " + chain[j].getSerialNumber());
                            System.out.println("");
                        }
                    }
                }
        };

        while (run) {
            if(transmittingAudio) {

                try {
                    if(mm==null) {
                        mm = new MicrophoneManager();
//                      mm.initialize();
                    }
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    SSLSocketFactory sslFact = sc.getSocketFactory();
                    SSLSocket socket = (SSLSocket)sslFact.createSocket(host, port);

                    socket.setSoTimeout(10000);
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream os = new DataOutputStream(new BufferedOutputStream(outputStream));
                    PrintWriter socketPrinter = new PrintWriter(os);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

//                  socketPrinter.println("POST /transmitaudio?patient=1333369798370 HTTP/1.0");
                    socketPrinter.println("POST /transmitaudio?id="+id+" HTTP/1.0");
                    socketPrinter.println("Content-Type: audio/basic");
                    socketPrinter.println("Content-Length: 99999");
                    socketPrinter.println("Connection: Keep-Alive");
                    socketPrinter.println("Cache-Control: no-cache");
                    socketPrinter.println();
                    socketPrinter.flush();

//                  in.read();

                    mm.startAudioInput();

                    int buffersizebytes = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,AudioFormat.ENCODING_PCM_16BIT); //4096 on ion 

                    System.out.println("audio started");
                    byte[] data = new byte[buffersizebytes];
                    while(transmittingAudio) {
                            //              byte[] data = new byte[mm.available()];
                            int bytesRead = mm.read(data);
                            os.write(data,0,bytesRead);
                            os.flush();
                            //              ca.transmitAxisAudioPacket(data);
//                          System.out.println("read "+data);
                            System.out.println("bytesRead "+bytesRead);
                            System.out.println("data "+Arrays.toString(data));
                    }
                    os.close();
                    mm.stopAudioInput();
                } catch (Exception e) {
                    System.out.println("excpetion while transmitting audio connection will be closed"+e);
                    transmittingAudio=false;
                }
            }
            else {
                try {
                Thread.sleep(1000);
                } catch (Exception e){
                    System.out.println("exception while thread sleeping"+e);}
            }
        }

    }

    public void setTransmittingAudio(boolean transmittingAudio) {
        this.transmittingAudio = transmittingAudio;
    }

    public void finished() {
        this.transmittingAudio = false;
        mm.finishAudioInput();
    }


}
