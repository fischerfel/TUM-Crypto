package it145_final;
import java.util.Scanner;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IT145_Final {

public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
    Scanner scnr = new Scanner(System.in);
    Scanner fileIn = null;
    int failedAttempts = 0;
    int i = 0;
    String q = "q";
    // objects that I think I need???? Maybe??? but dont know how to get them from the FinalFiles class
    FinalFiles fileAdmin = new FinalFiles();
    FinalFiles fileVet = new FinalFiles();
    FinalFiles fileZoo = new FinalFiles();
    FinalFiles userA = new FinalFiles();
    fileAdmin.file();
    userA.file();

    while (failedAttempts < 3)
        {                
            System.out.println("Enter user name, or q to exit"); //get username
            String userName = scnr.next();     
            if (userName.equalsIgnoreCase(q)) //option to terminiate
                    {
                    System.out.println("Logging Out");
                    break;
                    }   
            System.out.println("Enter password"); // get password
            scnr.nextLine();
            String userPassword = scnr.nextLine();  
                            //The following takes the entered password and hashes it
                            String hashedPass = userPassword;  
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(hashedPass.getBytes());
                            byte[] digest = md.digest();
                            StringBuffer sb = new StringBuffer();
                            for (byte b : digest) {
                            sb.append(String.format("%02x", b & 0xff));
                                    }


                if (userName.equals(userA[i]) && sb.toString().equals(userA[i + 1]))
                {
                    if (userA[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;
                    }                        
                    else if (userA[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;

                    }
                    else if (userA[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;

                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }
                if (userName.equals(userB[i]) && sb.toString().equals(userB[i + 1]))
                {
                    if (userB[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;

                    }                        
                    else if (userB[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;
                    }
                    else if (userB[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;
                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }
                if (userName.equals(userC[i]) && sb.toString().equals(userC[i + 1]))
                {
                    if (userC[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;
                    }                        
                    else if (userC[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;
                    }
                    else if (userC[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;
                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }
                if (userName.equals(userD[i]) && sb.toString().equals(userD[i + 1]))
                {
                    if (userD[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;
                    }                        
                    else if (userD[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;
                    }
                    else if (userD[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;
                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }
                if (userName.equals(userE[i]) && sb.toString().equals(userE[i + 1]))
                {
                    if (userE[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;
                    }                        
                    else if (userE[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;
                    }
                    else if (userE[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;
                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }
                if (userName.equals(userF[i]) && sb.toString().equals(userF[i + 1]))
                {
                    if (userF[i + 3].equals("admin"))
                    {
                        System.out.println(admin);
                        break;
                    }                        
                    else if (userF[i + 3].equals("veterinarian"))
                    {
                        System.out.println(veterinarian);
                        break;
                    }
                    else if (userF[i + 3].equals("zookeeper"))
                    {
                        System.out.println(zookeeper);
                        break;
                    }
                    else
                    {
                        System.out.println("Failed attempt");
                        failedAttempts ++;
                    }

                }


        System.out.println("Login Failed");
        failedAttempts++;
        }







    }




}
