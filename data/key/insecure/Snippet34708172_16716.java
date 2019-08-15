package smsapp;

import java.util.ArrayList;
import java.util.List;
import org.smslib.AGateway;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.Library;
import org.smslib.Message.MessageTypes;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

public class ReadMessages {

    public static int lastMemoryIndex = 0;

    public void doIt() throws Exception {
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
        try {
            System.out.println("Example: Read messages from a serial gsm modem.");
            System.out.println(Library.getLibraryDescription());
            System.out.println("Version: " + Library.getLibraryVersion());
            // Create the Gateway representing the serial GSM modem.
            SerialModemGateway gateway = new SerialModemGateway("modem.COM9", "COM9", 115200, "", "");
            // Set the modem protocol to PDU (alternative is TEXT). PDU is the default, anyway...
            gateway.setProtocol(Protocols.PDU);
            // Do we want the Gateway to be used for Inbound messages?
            gateway.setInbound(true);
            // Do we want the Gateway to be used for Outbound messages?
            gateway.setOutbound(true);
            // Let SMSLib know which is the SIM PIN.
            gateway.setSimPin("4236");
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
            Service.getInstance().S.CNMI_EMULATOR_INTERVAL = 2;
            Service.getInstance().S.WATCHDOG_INTERVAL = 3;

            // Printout some general information about the modem. 
            System.out.println("CNMI_EMULATOR_INTERVAL " + Service.getInstance().S.CNMI_EMULATOR_INTERVAL);
            System.out.println("WATCHDOG_INTERVAL " + Service.getInstance().S.WATCHDOG_INTERVAL);
            System.out.println();
            System.out.println("Modem Information:");
            System.out.println("  Manufacturer: " + gateway.getManufacturer());
            System.out.println("  Model: " + gateway.getModel());
            System.out.println("  Serial No: " + gateway.getSerialNo());
            System.out.println("  SIM IMSI: " + gateway.getImsi());
            System.out.println("  Signal Level: " + gateway.getSignalLevel() + " dBm");
            System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
            System.out.println();
            // In case you work with encrypted messages, its a good time to declare your keys.
            // Create a new AES Key with a known key value. 
            // Register it in KeyManager in order to keep it active. SMSLib will then automatically
            // encrypt / decrypt all messages send to / received from this number.
            //  Service.getInstance().getKeyManager().registerKey("+306948494037", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            // Read Messages. The reading is done via the Service object and
            // affects all Gateway objects defined. This can also be more directed to a specific
            // Gateway - look the JavaDocs for information on the Service method calls.

            msgList = new ArrayList<InboundMessage>();

            Service.getInstance().readMessages(msgList, MessageClasses.ALL);
            int size = msgList.size();
            for (int x = 0; x < size; x++) {
                InboundMessage msg = msgList.get(x);
                if (x == (size - 1)) {
                    System.out.println(msg.getMemIndex() + " " + msg.getText());
                }
                lastMemoryIndex = (int) msg.getMemIndex();
            }
//            for (InboundMessage msg : msgList) {
//                System.out.println(msg.getMessageId() +" "+msg.getText());
//            }
            // Sleep now. Emulate real world situation and give a chance to the notifications
            // methods to be called in the event of message or voice call reception.
            System.out.println("Now Sleeping - Hit <enter> to stop service.");
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Service.getInstance().stopService();
        }
    }

    public class InboundNotification implements IInboundMessageNotification {

        public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg) {
            if (msgType == MessageTypes.INBOUND) {
                String originator = msg.getOriginator();
                // System.out.println(" >>> New Inbound message detected from Gateway: " + gateway.getGatewayId());
                int newIndex = (int) msg.getMemIndex();
                if (newIndex > lastMemoryIndex) {
                    System.out.println(msg.getDate() + " " + msg.getText());
                    String message = msg.getText();
                    String[] details = dbutils.checker.getLipaDetails(message);
                    String lipa_code = details[0];
                    String lipa_date = details[1];
                    String lipa_time = details[2];
                    String lipa_amount = details[3];
                    String lipa_sys_amount = details[4];
                    String phone_number = details[5];
                    String names = details[6];
                    String received = "no";
                    String cashier = f_SuperMarket.SuperMarketViews.salesPerson.getText();
                    String sysdate = dbutils.formatedDate.getdate();
                    String systime = dbutils.formatedDate.getTime();
                    String date = dbutils.formatedDate.getDateFromDB(sysdate);
                    String time = dbutils.formatedDate.getTimeFromDb(systime);

                    dbutils.updateTables.newLipaNaMpesaEntry(lipa_code, lipa_date, lipa_time, lipa_amount, phone_number,
                            names, sysdate, date, systime, time, received, cashier, message);
//                    try {
//                        Service.getInstance().deleteMessage(msg);
//                    } catch (Exception e) {
//                    }
                    refreshLipaTable();
                    lastMemoryIndex = newIndex;
                }
            } else if (msgType == MessageTypes.STATUSREPORT) {
                System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
            }
        }
    }

    public static void refreshLipaTable() {
        String columns = "LIPA_CODE,LIPA_AMOUNT,LIPA_DATE,LIPA_TIME,NAMES";
        dbutils.updateTables.refreshTable(columns, "lipanampesa", f_SuperMarket.MakePayment.lipaTable);
    }

    public class CallNotification implements ICallNotification {

        public void process(AGateway gateway, String callerId) {
            System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
        }
    }

    public class GatewayStatusNotification implements IGatewayStatusNotification {

        public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
            System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
        }
    }

    public class OrphanedMessageNotification implements IOrphanedMessageNotification {

        public boolean process(AGateway gateway, InboundMessage msg) {
            System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
            System.out.println(msg);
            // Since we are just testing, return FALSE and keep the orphaned message part.
            return false;
        }
    }

    public static void main(String args[]) {
        ReadMessages app = new ReadMessages();
        try {
            app.doIt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
