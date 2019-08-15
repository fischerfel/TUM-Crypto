public class CRSslSocketHandler extends CRSocketHandler{

    private SSLEngine _engine;
    private SSLEngineResult.HandshakeStatus hsStatus;

    /**
    * Stores the result from the last operation performed by the SSLEngine 
    */
    private SSLEngineResult.Status status = null;

    /** Application data decrypted from the data received from the peer.
    * This buffer must have enough space for a full unwrap operation,
    * so we can't use the buffer provided by the application, since we
    * have no control over its size.
    */
    private final ByteBuffer peerAppData;
    /** Network data received from the peer. Encrypted. */  
    private final ByteBuffer peerNetData;
    /** Network data to be sent to the peer. Encrypted. */
    private final ByteBuffer netData;  

    /** Used during handshake, for the operations that don't consume any data */
    private ByteBuffer dummy;   


    private boolean initialHandshake = false;  

    private final String[] AvailProtocol = {"TLSv1"};

    private boolean FirstTime = true;

    //Debug
    private BufferedWriter  debugFile;
    private BufferedWriter  test;


    /** Creates a new instance of CRSslSocketHandler */
   public CRSslSocketHandler(SocketChannel channel, DualKeyHashtable queryCollection, DualKeyHashtable routineCollection, long sn, String hostName, int portNum) throws Exception {
       super(channel, queryCollection, routineCollection, sn);


       // Debug purposes
       debugFile = new BufferedWriter(new FileWriter("SslDebug.txt",true));
       test = new BufferedWriter(new FileWriter("ssltest.txt",true));
       try{
           System.out.println("Create Ssl Context");
            test.write("Create Ssl Context");  

            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            System.out.println("Init Ssl Context");
            test.write("Init Ssl Context");

            /String Pass = "password";
            char[] passphrase = Pass.toCharArray();

            System.out.println("Get keys");
            test.write("Get keys");
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystore.ks"), passphrase);
             System.out.println("Get trust");
            test.write("Get trust");          
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            System.out.println("Init trust");
            test.write("Init trust");           
            tmf.init(ks); 

            System.out.println("Init context");
            test.write("Init context");         
            sslContext.init(null, tmf.getTrustManagers(), null);
             System.out.println("Create engine host name: " + hostName + " port number: " + portNum);
            test.write("Create engine host name: " + hostName + " port number: " + portNum);
            _engine = sslContext.createSSLEngine(hostName, portNum);
            // _engine = sslContext.createSSLEngine();
            System.out.println("Set client mode");
            test.write("Set client mode");            
            _engine.setUseClientMode(true);
            _engine.setEnabledProtocols(AvailProtocol);



            test.write("Begin Hanshaking");
            System.out.println("Begin Hanshaking");
            _engine.beginHandshake();
            hsStatus = _engine.getHandshakeStatus();
       }
       catch (IOException e){

       }
       catch (Exception e) {
           System.out.println("Exception: " + e.getMessage());
           test.write("Exception: " + e.getMessage());
           test.close();
       }
     System.out.println("Alocate buffers");
      test.write("Alocate buffers");
      SSLSession  session = _engine.getSession();
      peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());
      peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
      netData = ByteBuffer.allocate(session.getPacketBufferSize());
      // Change the position of the buffers so that a 
      // call to hasRemaining() returns false. A buffer is considered
      // empty when the position is set to its limit, that is when
      // hasRemaining() returns false.      
      peerAppData.position(peerAppData.limit());
      netData.position(netData.limit());
      char c = (char)(28);
      String msg = "OK" + c;
      dummy = ByteBuffer.wrap(msg.getBytes());
      // dummy = ByteBuffer.allocate(0);
      initialHandshake = true;
      test.close();
    }

    protected int ReadExtFile(ByteBuffer buffer) throws IOException{
        if (initialHandshake) {
            doHandshake();
        }        
        // Check if the stream is closed.
        if (_engine.isInboundDone()) {
            // We reached EOF.
            return EOF;
        }
        // First check if there is decrypted data waiting in the buffers
        if (!peerAppData.hasRemaining()) {
            int appBytesProduced = readAndUnwrap();
            if (appBytesProduced == EOF){
                debugFile.write("Failed to read");
                debugFile.close();
                return appBytesProduced;
            }
            if (appBytesProduced == 0) {
                return appBytesProduced;
            } 
        }

        // It's not certain that we will have some data decrypted ready to 
        // be sent to the application. Anyway, copy as much data as possible
        int limit = Math.min(peerAppData.remaining(), buffer.remaining());
        for (int i = 0; i < limit; i++) {
            buffer.put(peerAppData.get());
        }
        return limit;
    }
    private int readAndUnwrap() throws IOException {
        // No decrypted data left on the buffers.
        // Try to read from the socket. There may be some data
        // on the peerNetData buffer, but it might not be sufficient.
        int bytesRead = _client.read(peerNetData);
        // decoder.decode(peerNetData, charBuffer, false);
        // charBuffer.flip();               
        System.out.println("Read bytes " + bytesRead);


        if (bytesRead == EOF) {
            // We will not receive any more data. Closing the engine
            // is a signal that the end of stream was reached.
            _engine.closeInbound();         
            // EOF. But do we still have some useful data available? 
            if (peerNetData.position() == 0 ||
            status == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                // Yup. Either the buffer is empty or it's in underflow,
                // meaning that there is not enough data to reassemble a
                // TLS packet. So we can return EOF.
                return EOF;
            }
            // Although we reach EOF, we still have some data left to
            // be decrypted. We must process it 
        }

        // Prepare the application buffer to receive decrypted data
        peerAppData.clear();

        // Prepare the net data for reading. 
        peerNetData.flip();
        SSLEngineResult res;
        System.out.println("Do unwrap " + bytesRead);
        try{
            do {
                res = _engine.unwrap(peerNetData, peerAppData);
                System.out.println("Read status" + res.getHandshakeStatus().toString());
                // During an handshake renegotiation we might need to perform
                // several unwraps to consume e handshake data.
            } while (res.getStatus() == SSLEngineResult.Status.OK &&
            res.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP &&
            res.bytesProduced() == 0);

            System.out.println("Read status" + res.getHandshakeStatus().toString());
            // If the initial handshake finish after an unwrap, we must activate
            // the application interestes, if any were set during the handshake
            if (res.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
                initialHandshake = false;
            }
           // If no data was produced, and the status is still ok, try to read once more
           if (peerAppData.position() == 0 && 
            res.getStatus() == SSLEngineResult.Status.OK &&
            peerNetData.hasRemaining()) {
               res = _engine.unwrap(peerNetData, peerAppData);                             
            }
           /*
            * The status may be:
            * OK - Normal operation
            * OVERFLOW - Should never happen since the application buffer is 
            *   sized to hold the maximum packet size.
            * UNDERFLOW - Need to read more data from the socket. It's normal.
            * CLOSED - The other peer closed the socket. Also normal.
            */
            status = res.getStatus();
            hsStatus = res.getHandshakeStatus();
            // Should never happen, the peerAppData must always have enough space
            // for an unwrap operation
            assert status != SSLEngineResult.Status.BUFFER_OVERFLOW : 
            "Buffer should not overflow: " + res.toString();  
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }




        // The handshake status here can be different than NOT_HANDSHAKING
        // if the other peer closed the connection. So only check for it
        // after testing for closure.
        if (status == SSLEngineResult.Status.CLOSED) {
            debugFile.write("Connection is being closed by peer.");
            return EOF;
        }   

        // Prepare the buffer to be written again.
        peerNetData.compact();
        // And the app buffer to be read.
        peerAppData.flip();

        if (hsStatus == SSLEngineResult.HandshakeStatus.NEED_TASK ||
                hsStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP ||
                hsStatus == SSLEngineResult.HandshakeStatus.FINISHED) 
        {
            debugFile.write("Rehandshaking...");
            doHandshake();
        }

        return peerAppData.remaining();
    }

        public void closeSocket(boolean timeout){

            super.closeSocket(timeout);
            try{
                debugFile.write("Close");
                debugFile.close();
            }catch(IOException e){}
        }

    /**
     * Execute delegated tasks in the main thread. These are compute
     * intensive tasks, so there's no point in scheduling them in a different
     * thread.
     */
    private void doTasks() {
        Runnable task;
        while ((task = _engine.getDelegatedTask()) != null) {
            task.run();
        }
        hsStatus = _engine.getHandshakeStatus();
    }
        private void doHandshake() throws IOException {
            while (true) {
                SSLEngineResult res;
                System.out.println("Handshake status: " + hsStatus.toString());
                switch (hsStatus) {
                    case FINISHED:
                        initialHandshake = false;
                        return; 
                    case NEED_TASK:
                        doTasks();
                        // The hs status was updated, so go back to the switch
                        break;
                    case NEED_UNWRAP:
                        readAndUnwrap();
                        return;
                    case NEED_WRAP:

                        if (netData.hasRemaining()) {
                            return;
                        }
                        // Prepare to write
                            netData.clear();

                            try{
                            res = _engine.wrap(dummy, netData);

                            if (res.bytesProduced() == 0){
                                System.out.println("No net data produced during handshake wrap.");
                            }
                            else{
                                System.out.println("Result status: " + res.getStatus() + "Bytes porduced: " + res.bytesProduced());
                            }
                            if (res.bytesConsumed() != 0){
                                System.out.println("App data consumed during handshake wrap.");
                            }
                            hsStatus = res.getHandshakeStatus();
                        }catch(SSLException se){

                            System.out.println("Error: " + se.getMessage());
                            System.out.println("Details: " + se.getLocalizedMessage());
                            throw se;
                        }

                        System.out.println(hsStatus.toString());
                        netData.flip();
                        try{
                            int writebytes = _client.write(netData);
                            System.out.println("Number of bytes sent: " + writebytes);
                            if (netData.hasRemaining()) {
                                System.out.println("netdata has remaining");
                            }
                         }
                         catch(Exception e){
                             System.out.println(e.getMessage());
                         } 
                        break;
                    case NOT_HANDSHAKING:
                        assert false : "doHandshake() should never reach the NOT_HANDSHAKING state";
                        return;
            }
        }
    }


}
