package Server;

import application.ClientInterface;

import javax.crypto.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alexi on 05/12/2016.
 */
public class MyFileServer extends UnicastRemoteObject implements ServerInit, Serializable {

    private static Cipher ecipher;
    private static SecretKey key;


    public  MyFileServer() throws RemoteException {
        super();
    }

    @Override
    public void importFiles(ClientInterface n, String name) throws RemoteException {
        String videoPath = "src" + File.separator + "Videos" + File.separator + name;
        try {

            key = KeyGenerator.getInstance("DES").generateKey();

            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);


            ServerInit server = new MyFileServer();

            File video = new File(videoPath);
            FileInputStream in=new FileInputStream(video);
            byte [] mydata=new byte[(int)video.length()];

            SealedObject sealed = new SealedObject(mydata, ecipher);

            int mylen=in.read(mydata);
            while(mylen>0){
                n.sendData(video.getName(), sealed, mylen);
                mylen=in.read(mydata);
            }

        }catch( Exception e){
            e.printStackTrace();
        }
    }

    public static void startServer(){
        try{


            ServerInit server = new MyFileServer();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("videoServer", server);
            System.out.println(registry.lookup("videoServer"));

        }catch (RemoteException e){
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
