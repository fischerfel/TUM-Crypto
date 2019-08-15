#Client#

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
public class Client {

private static boolean useSockets = true;   // Easy for debugging; turns sockets on/off

public static void main(String[] args) throws Exception {

    Socket echoSocket = null;  // Client to server socket connection object
    PrintWriter out = null;    // Out prints to the socket
    BufferedReader in = null;  // In reads from the socket
    int portno = 4444;         // The port number of the server we are connecting to


        //This assumes the server is also run on a local machine
        InetAddress server_name = InetAddress.getLocalHost();

        try {
            echoSocket = new Socket(server_name, portno);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection.");
            System.exit(1);
        }

        //Out will write to the socket and in will read from the socket
        out = new PrintWriter(echoSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));


    //Read input from the console
    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    Scanner keyboard = new Scanner(System.in);
    //At this point we assume that a TCP connection has been made to the server
    //We now ask the user for a username and password. 
    //If it is the users first time logging in then an account is created.

    String choice = welcomeMessage(keyboard);
    System.out.println(choice);
    if(choice.equals("1")){
        out.println(choice);
        New_Userlogin(in, out, stdIn);
    }
    else if (choice.equals("2")){
        out.print(choice);
        Current_Userlogin(in, out, stdIn);
    }
    else{
        //Exit the program
    }




    //User has access to Cubby at this point

    //Operating System stuff
    String os = System.getProperty("os.name").toLowerCase();
    String path = System.getProperty("user.home");

    if (os.indexOf("win") >= 0) {
        path = path+"\\Cubby";
    } else if (os.indexOf("mac") >= 0 || os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
        path = path+"/Cubby";
    } else {
        System.out.println("Your OS is not supported");
    }

    //Create the Cubby folder if it doesn't exist
    File f = new File(path);
    if(!f.exists()){
        f.mkdir();
    }

    Path dir = Paths.get(path);
    WatchDir monitordir = new WatchDir(dir, true);
    monitordir.processEvents();

    // talking with server handled in WatchDir

    //user requests to quit
    //sign off from server

    if (useSockets)
    {
        out.close();
        in.close();
        echoSocket.close();
    }
    stdIn.close();
}

public static String welcomeMessage(Scanner keyboard){
    System.out.println("Welcome to Cubby!");
    System.out.println("Select from one of the options below: ");
    String choice = "0";
    while((!choice.equals("1")) & (!choice.equals("2")) & (!choice.equals("3"))){
        System.out.println("1. Create an account: ");
        System.out.println("2. Login with an existing account: ");
        System.out.println("3. Exit: ");
        choice = keyboard.nextLine();
    }
    return choice;
}

public static void New_Userlogin(BufferedReader in, PrintWriter out, BufferedReader stdIn) throws Exception{
    while(true){
        System.out.println("Please enter a username: ");
        String username = stdIn.readLine();
        out.print(username);
        int serv_rep = in.read();
        if(serv_rep == 0){
            System.out.println("Username already taken");
        }
        else{
            System.out.println("Account successfully created");
            while(true){
                System.out.println("Enter a password: ");
                String password = stdIn.readLine();
                System.out.println("Confirm Password: ");
                String password_check = stdIn.readLine();
                if(!password.equals(password_check)){
                    System.out.println("Passwords did not match. Try again");
                }
                else{
                    String hashPass = generateHash(password);
                    password = "0000000000000000000";
                    out.print(hashPass);
                    break;
                }
                break;
            }
        }
    }
}

public static void Current_Userlogin(BufferedReader in, PrintWriter out, BufferedReader stdIn) throws Exception{
    String loginResponse = "invalid";

    while (loginResponse != "Good")
    {
        System.out.println("Please enter your username: ");
        String username = stdIn.readLine(); // read in username

        System.out.println("Please enter your password:");
        String password = stdIn.readLine();

        // Hash the password
        String hashPass = generateHash(password);
        password = "0000000000000000000";

        if (useSockets)
        {
            // Now we need to send the username and hashedpw to the server for validation
            out.print(username + "\n" + hashPass);

            // Now we need to wait for a valid reply from the server
            loginResponse = in.readLine();
        }

        loginResponse = "Good";

        if (loginResponse != "Good")
        {
            System.out.println("Invalid username and password");
        }
    }
}

private static String generateHash(String password) throws Exception
{
    byte[] hashPass = null;
    String hashedPassString = null;

    String saltedpw = "!The$Salt^^~" + password;

    System.out.println(saltedpw);

    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    hashPass = sha.digest(saltedpw.getBytes("UTF-8"));

    BigInteger number = new BigInteger(1, hashPass);
    hashedPassString = number.toString(16);

    System.out.println(hashedPassString);

    // For security, Java recommends setting the plaintext pw to all 0s after use
    saltedpw = "0000000000000000000000000000000000";
    password = "0000000000000000000000000000000000";

    return hashedPassString;
}

}

#Server#
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.io.*;

public class Server {
//global variable to store the last IP used by the Client
public static int LAST_IP;


public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
    checkDir();



    //First a socket is created on the given port, 4444
    ServerSocket serverSocket = null;
    try {
        serverSocket = new ServerSocket(4444);
    } catch (IOException e) {
        System.err.println("Could not listen on port: 4444");
        System.exit(1);
    }

    //Then the server does an accept. The server will idle until it 
    //finds a client to accept it. As it stands, this code will only work
    //for one client at a time. There does exist code out there to allow for
    //multiple clients if we want to look into that.
    Socket clientSocket = null;
    try {
        System.out.println("Waiting for a client to connect");
        clientSocket = serverSocket.accept();
    } catch (IOException e) {
        System.err.println("Accept failed.");
        System.exit(1);
    }
    System.out.println("Server connected to client");
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    //The first thing from the client should be a number indicating whether or not
    //we need to log the user in or create a new account

    //The first is an int indicating new user or an existing user
    String choice = null;
   // if(in.ready()){
    Scanner keyboard = new Scanner(clientSocket.getInputStream());
    choice = keyboard.next();
    System.out.println(choice);
    //}

    if(choice.equals("1")){
        String username = in.readLine();
        createNewUser(username);
    }
  //  else{
    //  String username = in.readLine();
        //String password = in.readLine();
        //if(testCredentials(username, password)){
            //out.print("Good");
        //}




    out.close();
    in.close();
    clientSocket.close();
    serverSocket.close();
}

public static void checkDir(){
    File f = new File("Users");

    //If this is the first time the server is run, there wont be any folder
    //If the folder exists, then we leave it alone.
    if(!f.exists()){
        f.mkdir();
    }
}

public static String createNewUser(String name){
    //Query the SQL database looking for a username that matches the one a user 
    //has requested. If there is no username, then return 1. If a match was found
    //return 0;
    System.out.println("Successfully got to the new User login");

    return "0";
}
public static boolean testCredentials(String username, String pass) throws FileNotFoundException, ClassNotFoundException, SQLException
{   
}

}
