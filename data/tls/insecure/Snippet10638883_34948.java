public class AudioWorker extends Thread
{ 
    private boolean stopped = false;

    private String host;
    private int port;
    private long id=0;
    boolean run=true;
    AudioRecord recorder;

    //ulaw encoder stuff
    private final static String TAG = "UlawEncoderInputStream";

    private final static int MAX_ULAW = 8192;
    private final static int SCALE_BITS = 16;

    private InputStream mIn;

    private int mMax = 0;

    private final byte[] mBuf = new byte[1024];
    private int mBufCount = 0; // should be 0 or 1

    private final byte[] mOneByte = new byte[1];
    ////
    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
    public AudioWorker(String host, int port, long id)
    { 
        this.host = host;
        this.port = port;
        this.id = id;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
//        start();
    }

    @Override
    public void run()
    { 
        Log.i("AudioWorker", "Running AudioWorker Thread");
        recorder = null;
        AudioTrack track = null;
        short[][]   buffers  = new short[256][160];
        int ix = 0;

        /*
         * Initialize buffer to hold continuously recorded AudioWorker data, start recording, and start
         * playback.
         */
        try
        {
            int N = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
            recorder = new AudioRecord(AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N*10);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, 
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, N*10, AudioTrack.MODE_STREAM);
            recorder.startRecording();
            track.play();
            /*
             * Loops until something outside of this thread stops it.
             * Reads the data from the recorder and writes it to the AudioWorker track for playback.
             */


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

//          socketPrinter.println("POST /transmitaudio?patient=1333369798370 HTTP/1.0");
            socketPrinter.println("POST /transmitaudio?id="+id+" HTTP/1.0");
            socketPrinter.println("Content-Type: AudioWorker/basic");
            socketPrinter.println("Content-Length: 99999");
            socketPrinter.println("Connection: Keep-Alive");
            socketPrinter.println("Cache-Control: no-cache");
            socketPrinter.println();
            socketPrinter.flush();


            while(!stopped)
            { 
                Log.i("Map", "Writing new data to buffer");
                short[] buffer = buffers[ix++ % buffers.length];

                N = recorder.read(buffer,0,buffer.length);
                track.write(buffer, 0, buffer.length);

                byte[] bytes2 = new byte[buffer.length * 2];
                ByteBuffer.wrap(bytes2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(buffer);

                read(bytes2, 0, bytes2.length);
//              os.write(bytes2,0,bytes2.length);
                os.write(bytes2,0,bytes2.length);

                System.out.println("bytesRead "+buffer.length);
                System.out.println("data "+Arrays.toString(buffer));
            }
            os.close();
        }
        catch(Throwable x)
        { 
            Log.w("AudioWorker", "Error reading voice AudioWorker", x);
        }
        /*
         * Frees the thread's resources after the loop completes so that it can be run again
         */
        finally
        { 
            recorder.stop();
            recorder.release();
            track.stop();
            track.release();
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */

/**
 * Called from outside of the thread in order to stop the recording/playback loop
 */
public void close()
{ 
     stopped = true;
}
public void resumeThread()
{ 
     stopped = false;
     run();
}

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


    public static void encode(byte[] pcmBuf, int pcmOffset,
            byte[] ulawBuf, int ulawOffset, int length, int max) {

        // from  'ulaw' in wikipedia
        // +8191 to +8159                          0x80
        // +8158 to +4063 in 16 intervals of 256   0x80 + interval number
        // +4062 to +2015 in 16 intervals of 128   0x90 + interval number
        // +2014 to  +991 in 16 intervals of  64   0xA0 + interval number
        //  +990 to  +479 in 16 intervals of  32   0xB0 + interval number
        //  +478 to  +223 in 16 intervals of  16   0xC0 + interval number
        //  +222 to   +95 in 16 intervals of   8   0xD0 + interval number
        //   +94 to   +31 in 16 intervals of   4   0xE0 + interval number
        //   +30 to    +1 in 15 intervals of   2   0xF0 + interval number
        //     0                                   0xFF

        //    -1                                   0x7F
        //   -31 to    -2 in 15 intervals of   2   0x70 + interval number
        //   -95 to   -32 in 16 intervals of   4   0x60 + interval number
        //  -223 to   -96 in 16 intervals of   8   0x50 + interval number
        //  -479 to  -224 in 16 intervals of  16   0x40 + interval number
        //  -991 to  -480 in 16 intervals of  32   0x30 + interval number
        // -2015 to  -992 in 16 intervals of  64   0x20 + interval number
        // -4063 to -2016 in 16 intervals of 128   0x10 + interval number
        // -8159 to -4064 in 16 intervals of 256   0x00 + interval number
        // -8192 to -8160                          0x00

        // set scale factors
        if (max <= 0) max = MAX_ULAW;

        int coef = MAX_ULAW * (1 << SCALE_BITS) / max;

        for (int i = 0; i < length; i++) {
            int pcm = (0xff & pcmBuf[pcmOffset++]) + (pcmBuf[pcmOffset++] << 8);
            pcm = (pcm * coef) >> SCALE_BITS;

            int ulaw;
            if (pcm >= 0) {
                ulaw = pcm <= 0 ? 0xff :
                        pcm <=   30 ? 0xf0 + ((  30 - pcm) >> 1) :
                        pcm <=   94 ? 0xe0 + ((  94 - pcm) >> 2) :
                        pcm <=  222 ? 0xd0 + (( 222 - pcm) >> 3) :
                        pcm <=  478 ? 0xc0 + (( 478 - pcm) >> 4) :
                        pcm <=  990 ? 0xb0 + (( 990 - pcm) >> 5) :
                        pcm <= 2014 ? 0xa0 + ((2014 - pcm) >> 6) :
                        pcm <= 4062 ? 0x90 + ((4062 - pcm) >> 7) :
                        pcm <= 8158 ? 0x80 + ((8158 - pcm) >> 8) :
                        0x80;
            } else {
                ulaw = -1 <= pcm ? 0x7f :
                          -31 <= pcm ? 0x70 + ((pcm -   -31) >> 1) :
                          -95 <= pcm ? 0x60 + ((pcm -   -95) >> 2) :
                         -223 <= pcm ? 0x50 + ((pcm -  -223) >> 3) :
                         -479 <= pcm ? 0x40 + ((pcm -  -479) >> 4) :
                         -991 <= pcm ? 0x30 + ((pcm -  -991) >> 5) :
                        -2015 <= pcm ? 0x20 + ((pcm - -2015) >> 6) :
                        -4063 <= pcm ? 0x10 + ((pcm - -4063) >> 7) :
                        -8159 <= pcm ? 0x00 + ((pcm - -8159) >> 8) :
                        0x00;
            }
            ulawBuf[ulawOffset++] = (byte)ulaw;
        }
    }
    public static int maxAbsPcm(byte[] pcmBuf, int offset, int length) {
        int max = 0;
        for (int i = 0; i < length; i++) {
            int pcm = (0xff & pcmBuf[offset++]) + (pcmBuf[offset++] << 8);
            if (pcm < 0) pcm = -pcm;
            if (pcm > max) max = pcm;
        }
        return max;
    }

    public int read(byte[] buf, int offset, int length) throws IOException {
        if (recorder == null) throw new IllegalStateException("not open");

        // return at least one byte, but try to fill 'length'
        while (mBufCount < 2) {
            int n = recorder.read(mBuf, mBufCount, Math.min(length * 2, mBuf.length - mBufCount));
            if (n == -1) return -1;
            mBufCount += n;
        }

        // compand data
        int n = Math.min(mBufCount / 2, length);
        encode(mBuf, 0, buf, offset, n, mMax);

        // move data to bottom of mBuf
        mBufCount -= n * 2;
        for (int i = 0; i < mBufCount; i++) mBuf[i] = mBuf[i + n * 2];

        return n;
    }

}
