public void doIt() throws Exception
{
    // Define a list which will hold the read messages.
    List<InboundMessage> msgList;
    // Create the notification callback method for inbound & status report
    // messages.
    InboundNotification inboundNotification = new InboundNotification();
    // Create the notification callback method for inbound voice calls.
    CallNotification callNotification = new CallNotification();
    //Create the notification callback method for gateway statuses.
    GatewayStatusNotification statusNotification = new GatewayStatusNotification();
    OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
    try
    {
        //System.out.println("Example: Read messages from a serial gsm modem.");
        //System.out.println(Library.getLibraryDescription());
        //System.out.println("Version: " + Library.getLibraryVersion());
        // Create the Gateway representing the serial GSM modem.
        SerialModemGateway gateway = new SerialModemGateway("modem.com1", "COM4", 115200, "Huawei", "E160");
        // Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
        gateway.setProtocol(Protocols.PDU);
        // Do we want the Gateway to be used for Inbound messages?
        gateway.setInbound(true);
        // Do we want the Gateway to be used for Outbound messages?
        gateway.setOutbound(true);
        // Let SMSLib know which is the SIM PIN.
        gateway.setSimPin("0000");
        // Set up the notification methods.
        Service.getInstance().setInboundMessageNotification(inboundNotification);
        Service.getInstance().setCallNotification(callNotification);
        Service.getInstance().setGatewayStatusNotification(statusNotification);
        Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
        // Add the Gateway to the Service object.
        Service.getInstance().addGateway(gateway);
        // Similarly, you may define as many Gateway objects, representing
        // various GSM modems, add them in the Service object and control all of them.
        // Start! (i.e. connect to all defined Gateways)
        Service.getInstance().startService();
        // Printout some general information about the modem.
        System.out.println();
        System.out.println("Informacion del modem:");
        System.out.println(" Fabricante: " + gateway.getManufacturer());
        System.out.println("  Modelo: " + gateway.getModel());
        System.out.println("  Serial No: " + gateway.getSerialNo());
        System.out.println("  SIM IMSI: " + gateway.getImsi());
        //System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
        //System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
        System.out.println();
        // In case you work with encrypted messages, its a good time to declare your keys.
        // Create a new AES Key with a known key value. 
        // Register it in KeyManager in order to keep it active. SMSLib will then automatically
        // encrypt / decrypt all messages send to / received from this number.
        Service.getInstance().getKeyManager().registerKey("+306948494037", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
        // Read Messages. The reading is done via the Service object and
        // affects all Gateway objects defined. This can also be more directed to a specific
        // Gateway - look the JavaDocs for information on the Service method calls.

                ConexionMySQL mysql = new ConexionMySQL();
                Connection cnn =  mysql.Conectar();
                String sms= "";
                String originator="";


                String sql ="";
                int n = 0;
                String success="Mensajee satisfactorio";

                msgList = new ArrayList<InboundMessage>();
        Service.getInstance().readMessages(msgList, MessageClasses.ALL);
        for (InboundMessage msg : msgList){
            //System.out.println(msg);
                     sms = msg.getText().toString();
                     remitente = msg.getOriginator().toString();
                     //fecha = msg.getDate().getTime();




                sql ="INSERT INTO message(message, originator) VALUES (?,?)";

                PreparedStatement post = (PreparedStatement) cnn.prepareStatement(sql);
                post.setString(1, message);
                post.setString(2, originator);
                //post.setDate(3, fecha);   public void doIt() throws Exception
{
    // Define a list which will hold the read messages.
    List<InboundMessage> msgList;
    // Create the notification callback method for inbound & status report
    // messages.
    InboundNotification inboundNotification = new InboundNotification();
    // Create the notification callback method for inbound voice calls.
    CallNotification callNotification = new CallNotification();
    //Create the notification callback method for gateway statuses.
    GatewayStatusNotification statusNotification = new GatewayStatusNotification();
    OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();
    try
    {
        //System.out.println("Example: Read messages from a serial gsm modem.");
        //System.out.println(Library.getLibraryDescription());
        //System.out.println("Version: " + Library.getLibraryVersion());
        // Create the Gateway representing the serial GSM modem.
        SerialModemGateway gateway = new SerialModemGateway("modem.com1", "COM4", 115200, "Huawei", "E160");
        // Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
        gateway.setProtocol(Protocols.PDU);
        // Do we want the Gateway to be used for Inbound messages?
        gateway.setInbound(true);
        // Do we want the Gateway to be used for Outbound messages?
        gateway.setOutbound(true);
        // Let SMSLib know which is the SIM PIN.
        gateway.setSimPin("0000");
        // Set up the notification methods.
        Service.getInstance().setInboundMessageNotification(inboundNotification);
        Service.getInstance().setCallNotification(callNotification);
        Service.getInstance().setGatewayStatusNotification(statusNotification);
        Service.getInstance().setOrphanedMessageNotification(orphanedMessageNotification);
        // Add the Gateway to the Service object.
        Service.getInstance().addGateway(gateway);
        // Similarly, you may define as many Gateway objects, representing
        // various GSM modems, add them in the Service object and control all of them.
        // Start! (i.e. connect to all defined Gateways)
        Service.getInstance().startService();
        // Printout some general information about the modem.
        System.out.println();
        System.out.println("Informacion del modem:");
        System.out.println(" Fabricante: " + gateway.getManufacturer());
        System.out.println("  Modelo: " + gateway.getModel());
        System.out.println("  Serial No: " + gateway.getSerialNo());
        System.out.println("  SIM IMSI: " + gateway.getImsi());
        //System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
        //System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
        System.out.println();
        // In case you work with encrypted messages, its a good time to declare your keys.
        // Create a new AES Key with a known key value. 
        // Register it in KeyManager in order to keep it active. SMSLib will then automatically
        // encrypt / decrypt all messages send to / received from this number.
        Service.getInstance().getKeyManager().registerKey("+306948494037", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
        // Read Messages. The reading is done via the Service object and
        // affects all Gateway objects defined. This can also be more directed to a specific
        // Gateway - look the JavaDocs for information on the Service method calls.

                    ConexionMySQL mysql = new ConexionMySQL();
                    Connection cnn =  mysql.Conectar();
                    String sms= "";
                    String remitente="";
                    //Date fecha;

                    String sql ="";
                    int n = 0;
                    String mensaje="Mensajee satisfactorio";

                    msgList = new ArrayList<InboundMessage>();
        Service.getInstance().readMessages(msgList, MessageClasses.ALL);
        for (InboundMessage msg : msgList){
            //System.out.println(msg);
                         sms = msg.getText().toString();
                         remitente = msg.getOriginator().toString();
                         //fecha = msg.getDate().getTime();




                    sql ="INSERT INTO mensaje(mensaje, remitente) VALUES (?,?)";

                    PreparedStatement post = (PreparedStatement) cnn.prepareStatement(sql);
                    post.setString(1, sms);
                    post.setString(2, originator);


                    n = post.executeUpdate();

                    if(n >0){
                        JOptionPane.showMessageDialog(null, mensaje);
                        Service.getInstance().deleteMessage(msg);

                    }















        // Sleep now. Emulate real world situation and give a chance to the notifications
        // methods to be called in the event of message or voice call reception.
                    System.out.println("Now Sleeping - Hit <enter> to stop service.");
        System.in.read();



        System.in.read();






                    }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        Service.getInstance().stopService();
    }
}

                n = post.executeUpdate();

                if(n >0){
                    JOptionPane.showMessageDialog(null, mensaje);
                    Service.getInstance().deleteMessage(msg);

                }















        // Sleep now. Emulate real world situation and give a chance to the notifications
        // methods to be called in the event of message or voice call reception.
                System.out.println("Now Sleeping - Hit <enter> to stop service.");
        System.in.read();



        System.in.read();






                }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    finally
    {
        Service.getInstance().stopService();
    }
}
