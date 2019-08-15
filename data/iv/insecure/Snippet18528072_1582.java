Download & Decrypt file from SFTP using JSCH

/**
 * The class to download the files from SFTP server.
 */
package com.test.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * @author varunvikramsingh@gmail.com
 * 
 */
@Service
public class DownloadFileServiceJschImpl implements DownloadFileService {
    @Value("${sftpServer}")
    String sftpServer;

    @Value("${sftpUsername}")
    String sftpUsername;

    @Value("${sftpPassword}")
    String sftpPassword;

    @Value("${sftpPort}")
    String sftpPort;

    @Value("${decryptPrivateKey}")
    String DecryptKeyKey;

    private Log logger = LogFactory.getLog(this.getClass().getName());

    @Override
    public void downloadFile(String ccoid, String reportType, String url, String filename, OutputStream outStream,
            byte[] decryptKeyFromSOA) throws SftpException {

            try {

            // SFTP HOST DETAILS
            String ftpHostSFTP = sftpServer;
            // SFTP PORT NUMBER
            String ftpPortSFTP = sftpPort;
            int parsePort = Integer.parseInt(ftpPortSFTP);
            // SFTP USER NAME
            String ftpUserNameSFTP = sftpUsername.trim();
            // SFTP PASSWORD
            String ftpPasswordSFTP = sftpPassword;
            // SFTP REMOTE DIRECTORY
            String ftpRemoteDirectory = "/data";


            // First Create a JSch session
            logger.info("Creating session with SFTP.");
            // JAVA SECURE CHANNEL API for connecting to the service of via SSH22
            JSch jsch = new JSch();
            Session session = null;
            Channel channel = null;
            ChannelSftp sftpChannel = null;

            logger.info("Trying to Connect to SFTP Server : "+sftpServer);
            logger.info("SFTP Server userName : "+sftpUsername);
            logger.info("SFTP Server sftPort: "+sftpPort);
            logger.info("SFTP Server password: "+sftpPassword);
            session = jsch.getSession(ftpUserNameSFTP, ftpHostSFTP, parsePort);
            //session = jsch.getSession(sftpUsername, sftpServer, sftpPort);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(ftpPasswordSFTP);
            //session.setPassword(sftpPassword);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            sftpChannel.cd("/");
            sftpChannel.cd("/data/");

            logger.info("Current Directory for user is : "+ sftpChannel.pwd());
            logger.info("User is trying to download file :"+filename);

            String newFileName = filename+".enc";

            logger.info("Portal added .enc as suffix to filename, fileName now is :"+newFileName);

            // ==============Decrypt SOA key=================
            byte[] decryptedSOAKeyArray = decrypt(decryptKeyFromSOA,readSecurityKey());
            String soaDecryptedKey = new String(decryptedSOAKeyArray);

            logger.info("Private Key Received from SOA :"+soaDecryptedKey);

            logger.info("Reading the file from SFTP server");

            BufferedInputStream bis = new BufferedInputStream(sftpChannel.get(newFileName));  
            BufferedOutputStream bos = new BufferedOutputStream(outStream);

            logger.info("Decrypting the file received from SFTP");

            // Decrypt the file
            decrypt(bis, bos, decryptedSOAKeyArray);

            logger.info("File Successfully Downloaded");

            outStream.close();
            sftpChannel.exit();
            session.disconnect();

            logger.info("All connections successfully closed");

        }
        catch (JSchException e) {
            e.printStackTrace();
        }
        catch (SftpException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    ServletContext servletContext;
    private byte[] readSecurityKey() {

        ObjectInputStream in = null;
        byte[] publicKey = null;

        String path = servletContext.getRealPath("/WEB-INF/conf");
        String publicKeyfilename = path + "\\PublicKey.txt";

        logger.info("Reading security Key from file :"+publicKeyfilename);

        try {
            InputStream is = new FileInputStream(publicKeyfilename);
            publicKey = getByteArrayFromFile(is);
        }
        catch (IOException ioe) {
            logger.info("Exception in reading security key "+ioe);
        }
        finally {
            if (null != in) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    /*
                     * Eating this exception since it occurred while closing the input stream.
                     */
                }
            }

        } // end of finally
        return publicKey;
    }

    public static void invokeDecryptFile(String encFilename,String decFilename,byte[] password){
        try{
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(encFilename));
        BufferedOutputStream decout = new BufferedOutputStream(new FileOutputStream(decFilename));
        decrypt(is, decout, password);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static byte[] getByteArrayFromFile(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = is.read(buffer, 0, buf_size))) {
                out.write(buffer, 0, len);
            }
            byte[] fileContent = out.toByteArray();
            is.close();
            return fileContent;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] decrypt(byte[] cipherTextBytes, byte[] password) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(cipherTextBytes);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        decrypt(bis, bos, password);        
        return bos.toByteArray();
    }

    public static void decrypt(InputStream in, OutputStream out, byte[] password) throws Exception{
        final String ALGORITHM = "AES";
        final String transformation="AES/CFB8/NoPadding";
        final int CACHE_SIZE = 64*1024;
        final int IV_LENGTH=16;
        byte[] iv = new byte[IV_LENGTH];
        in.read(iv);

        Cipher cipher = Cipher.getInstance(transformation); 
        SecretKeySpec keySpec = new SecretKeySpec(password, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        in = new CipherInputStream(in, cipher);
        byte[] buf = new byte[CACHE_SIZE];
        int numRead = 0;
        while ((numRead = in.read(buf)) >= 0) {
            out.write(buf, 0, numRead);
        }
        out.close();
    }

    // @Override
    // public void downloadFile(InputStream in, OutputStream out) {
    // try {
    // int i = 0;
    // byte[] bytesIn = new byte[1024];
    //
    // /*
    // * Loop through the entire file writing bytes.
    // */
    // while ((i = in.read(bytesIn)) >= 0) {
    // out.write(bytesIn, 0, i);
    // }
    //
    // out.close();
    // in.close();
    // }
    // catch(Exception e) {
    // e.printStackTrace();
    // }
    //
    // }


}
