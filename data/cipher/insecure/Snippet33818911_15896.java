import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import premierleague.model.FootballClub;
import premierleague.model.Match;

/**
 *
 * @author Akila
 */
public class Serializing implements Serializable{

    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ArrayList<FootballClub> FootBallInputStream() throws FileNotFoundException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        File file = new File("FootballClub.ser");
        fileIn = new FileInputStream(file);
        CipherInputStream CipherIn = new CipherInputStream(in, cipher);
        in = new ObjectInputStream(CipherIn);
        ArrayList<FootballClub> e = (ArrayList<FootballClub>) in.readObject();
        in.close();
        fileIn.close();

        return e;

    }

    public void FootBallOutputStream(ArrayList<FootballClub> e) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("DES");
        File file = new File("FootballClub.ser");
        fileOut = new FileOutputStream(file);
        CipherOutputStream cipherOut = new CipherOutputStream(out,cipher);
        out = new ObjectOutputStream(cipherOut);
        out.writeObject(e);
        out.close();
        fileOut.close();
    }


}
