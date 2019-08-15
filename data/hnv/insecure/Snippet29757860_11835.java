class connectSocket implements Runnable
{

    @Override
    public void run() 
    {
        do
        {
        try
        {
            IsRunning = true;

            SSLContext context = SSLContext.getDefault();

            socket = context.getSocketFactory().createSocket(SERVERIP, SERVERPORT);

            SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();

            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            socket2 = (SSLSocket) sf.createSocket(socket, SERVERIP, SERVERPORT, false);

            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();

            SSLSession s = socket2.getSession();




            Intent text_intent = new Intent("Text_view");


            //
            String MACAddr = Utils.getMACAddress(SocketServiceController.this,"eth0");
            if (MACAddr.length() == 0)
                MACAddr = Utils.getMACAddress(SocketServiceController.this,"wlan0");
            if (MACAddr.length() == 0)
                MACAddr = Secure.getString(SocketServiceController.this.getContentResolver(),Secure.ANDROID_ID); 
            System.out.println("************" + MACAddr);
            //
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream())), true);

            out.write("0002:" + MACAddr + "\r\n");
            //

            //

            out.flush();

            try
            {
                InputStream inputstream = socket2.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));


                readObject = reader.readLine();

                String text = "";

                while(readObject != "")
                {
                    text = readObject;


                    createNotification("Alert", text, SocketServiceController.this);



                    sendBroadcast(text_intent);

                    readObject = reader.readLine();
                }
                connection_attemp = 0;
            }
            catch(Exception ex)
            {

                IsRunning = false;
                createNotification("Alert","Connection is Closed", SocketServiceController.this);
                stopService(new Intent(getApplicationContext(), SocketServiceController.class));
                IsRunning = false;
                if(connection_attemp == 0 )
                {
                    c = Calendar.getInstance();
                    connect_time=c.get(Calendar.MINUTE);


                }
                double minutes = c.get(Calendar.MINUTE)- connect_time;

                if(minutes < 5.0)
                {

                     connection_attemp++;
                     System.out.println("MINUTES: " + minutes + " CONNECT TIME: " + connect_time);
                     Thread.sleep(5000);

                     System.out.println("CONNECTIO ATTEMPT : " + connection_attemp);
                }
                else
                {
                     System.out.println("SLEEPING");
                    Thread.sleep(15000);

                }
                 System.out.println("STARTING NEW SERVICE");

                        startService(new Intent(getApplicationContext(), SocketServiceController.class));

            }





        }
        catch(Exception ex)
        {
            ex.printStackTrace();

            IsRunning = false;

            try
            {
                if(IsRunning != false)
                    socket.close();

            }
            catch(IOException e1)
            {
                e1.printStackTrace();
            }
        }
        IsRunning = false;
        }
        while(!IsRunning);
