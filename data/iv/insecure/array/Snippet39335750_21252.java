import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;



class Q5 { //Queue/Buffer   
    byte[] shipmentConfirmation;//will include the actual message content been delivered
    boolean valueSet = false;
    synchronized byte[] get() 
    {       
        while(!valueSet) 
        try 
        {
            wait();
        } 
        catch(InterruptedException e) 
        {
            System.out.println("InterruptedException caught");
        }
        //System.out.println("Got: " + n);
        valueSet = false;
        notify();
        return shipmentConfirmation;
    }
    synchronized void put(byte[] shipmentCinfirm) 
    {
        while(valueSet)
        try 
        {
            wait();
        } 
        catch(InterruptedException e) 
        {
            System.out.println("InterruptedException caught");
        }
        this.shipmentConfirmation = shipmentCinfirm;
        valueSet = true;
        //System.out.println("Put: " + n);
        notify();
    }
}

class Global5{
    public static int sendcouter = 0;
    public static SecretKey secret_Key;
    public static Cipher desCipher;
}


//<<security pattern>> SymmetricEncryptionEncryptor
class SecurityEncryptor//<<security>> Encryptor
{
    static byte[] Encryptor(byte shipmentConfirmation[],Cipher c) throws Exception //Encryptor
    {       
        byte[] ciphertext = SecurityEncryptionAlgorithm.EncryptionAlgorithm(shipmentConfirmation,c,Global5.secret_Key);
        return ciphertext;
    }   
}
class SecurityEncryptionAlgorithm//<<security>> EncryptionAlgorithm
{
    static byte[] EncryptionAlgorithm(byte shipmentConfirmation[],Cipher c,SecretKey sk) throws Exception 
    {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.ENCRYPT_MODE, sk, ivspec);
        return c.doFinal(shipmentConfirmation);
    }
}



//<<security pattern>> aSecureAsynchronousMCReceiverConnector
class SecurityDecryptor//<<Security>> Decryptor
{
    static byte[] Decryptor(byte EncryptedShipmentConfirmation[],Cipher c,SecretKey sk) throws Exception //Decryptor
    {
        byte[] ct = SecurityDecryptionAlgorithm.DecryptionAlgorithm(EncryptedShipmentConfirmation,c,sk);
        return ct;
    }   
}
class SecurityDecryptionAlgorithm//<<Security>> DecryptionAlgorithm
{
    static byte[] DecryptionAlgorithm(byte EncryptedShipmentConfirmation[],Cipher c,SecretKey sk) throws Exception 
    {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.DECRYPT_MODE, sk, ivspec);
        return c.doFinal(EncryptedShipmentConfirmation);
    }
}



public class testFigure1 { //Main
       public static void main(String args[]) throws Exception {          
            Q5 q1 = new Q5();//creating buffer/queue
            Q5 q2 = new Q5();

            System.out.println("How many messages to send: ");
            Scanner in = new Scanner(System.in);
            int input = in.nextInt();//Get input from the supplier          
            in.close();

            aSupplierInterface Supplier = new aSupplierInterface(q1, input);    
            aSecuritySenderCoordinator SenderCoordinator = new aSecuritySenderCoordinator(q1, input, q2);           
            aSecurityReceiverCoordinator receive = new aSecurityReceiverCoordinator(q2, input);   
            Supplier.t_pro.join();
            SenderCoordinator.t_coordinator5.join();
            receive.t_SecurityReceiverCoordinator5.join();
            System.out.println("End of Program!");
       }   
}

class aSupplierInterface  implements Runnable //<<application Component>> aSupplierInterface
{
    Q5 q;   
    int supinput;
    Thread t_pro;//pro to represent producer or suppler
    aSupplierInterface(Q5 qq, int input)
    {
        supinput = input;
        this.q = qq;
        t_pro = new Thread(this, "Producer");
        t_pro.start();
    }
    public void run() 
    {
        int i = 0;
        String shipment;
        byte[] shipmentConfirmation;
        while(i<supinput)
        {
            i++;
            shipment = "This is the Delivery Number: "+ i;
            shipmentConfirmation = shipment.getBytes();

            q.put(shipmentConfirmation);//Putting supplier's goods in a queue/buffer            
        }   
    }       
}

class aSecuritySenderCoordinator implements Runnable //<<security coordinator>> aSecuritySenderCoordinator
{
    Q5 q;
    Q5 q2;
    Thread t_coordinator5;
    int supinput;

    public aSecuritySenderCoordinator(Q5 qq, int input, Q5 q2) throws Exception
    {       
        supinput=input;
        this.q = qq;    
        this.q2=q2;
        t_coordinator5 = new Thread(this, "coordinator5");
        t_coordinator5.start();
    }

    public void run() 
    {
        byte[] pkShipmentConfirmation;
        byte[] shipmentConfirmation;
        int i = 0;
        while(i<supinput) 
        {               
            i++;

                //Getting goods that supplier has put in the queue previously
            pkShipmentConfirmation=q.get();//This will contain content of the message/delivery you are sending
            KeyGenerator keygen;
            try {
            keygen = KeyGenerator.getInstance("DES");
            Global5.sendcouter++;//To Create the key once
            if(Global5.sendcouter==1)//Create once
            {
                Global5.secret_Key = keygen.generateKey();
                Global5.desCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            }           
            //String st1 = new String(pkShipmentConfirmation);//just to print a message
            //System.out.println("*ORIGINAL MESSAGE:"+st1);    
            shipmentConfirmation = SecurityEncryptor.Encryptor(pkShipmentConfirmation,Cipher.getInstance("DES/CBC/PKCS5Padding"));//Encrypting 


            new anAsynchronousMCReceiver(q2, shipmentConfirmation);     

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




class anAsynchronousMCReceiver 
{ //<<communication pattern>> anAsynchronousMCReceiver
       Q5 q;

       anAsynchronousMCReceiver( Q5 q2, byte[] shipmentConfirm) throws Exception
       {
           this.q = q2;
           q.put(shipmentConfirm);  //Entering received data in to the Queue/Buffer 
       }
}



class aSecurityReceiverCoordinator implements Runnable//<<security coordinator>> aSecurityReceiverCoordinator
{       
    Thread t_SecurityReceiverCoordinator5;  
    Q5 q;
    int supinput;
    byte[]encryptedShipmentConfirmation;
    public aSecurityReceiverCoordinator(Q5 q2, int input) throws Exception
    {  
        this.q = q2;    
        supinput = input;
        t_SecurityReceiverCoordinator5 = new Thread(this, "SecurityReceiverCoordinator5");
        t_SecurityReceiverCoordinator5.start();

    }
    public void run() 
    {
        try {
            int i = 0;
            while(i<supinput) 
            {           
                i++;
                encryptedShipmentConfirmation = q.get();                
                byte[] confirmation = SecurityDecryptor.Decryptor(encryptedShipmentConfirmation,Cipher.getInstance("DES/CBC/PKCS5Padding"),Global5.secret_Key);//ERROR HAPPENS HERE              
                String shipConfirmation = new String(confirmation);

                System.out.println("AT DelivertyOrder: " +  shipConfirmation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
