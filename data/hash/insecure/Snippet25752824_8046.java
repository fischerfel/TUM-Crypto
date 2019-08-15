import java.io.*;
import java.net.*;
import java.util.*;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import com.google.api.services.admin.directory.*;
import com.google.api.services.admin.directory.model.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public class PasswordResetter
{
    public void changePassword( final String username, final String newPassword ) throws Exception
    {
        final Directory dir = getDirectory();
        final User studentUser = updatePassword( getUser( dir, DOMAIN_NAME, username ), password );
        updateDirectory( dir, studentUser );
    }

    private Directory getDirectory() throws IOException, GeneralSecurityException, URISyntaxException
    {
        final NetHttpTransport httpTransport = new NetHttpTransport();
        final JacksonFactory jsonFactory = new JacksonFactory();
        final File p12 = new File( P12_FILENAME );
        final GoogleCredential credential = new GoogleCredential.Builder()
                                                                    .setTransport(httpTransport)
                                                                    .setJsonFactory(jsonFactory)
                                                                    .setServiceAccountUser( SUPER_USER_EMAIL )
                                                                    .setServiceAccountId( SERVICE_ACCOUNT_EMAIL_FROM_DEV_CONSOLE ) //the one that ends in "@developer.gserviceaccount.com"
                                                                    .setServiceAccountScopes( getCredentials() )
                                                                    .setServiceAccountPrivateKeyFromP12File( p12 )
                                                                    .build();
        return new Directory.Builder( httpTransport, jsonFactory, null)
                                    .setHttpRequestInitializer( credential )
                                    .setApplicationName( "API Project" )    //Not necessary, but silences a runtime warning using any not-blank string here
                                    .build();
    }

    private List<String> getCredentials()
    {
        final List<String> toReturn = new LinkedList<String>();
        toReturn.add( DirectoryScopes.ADMIN_DIRECTORY_GROUP );
        toReturn.add( DirectoryScopes.ADMIN_DIRECTORY_USER );
        toReturn.add( DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY );
        toReturn.add( DirectoryScopes.ADMIN_DIRECTORY_USER_SECURITY );
        return toReturn;
    }

    private Users getUser( final Directory dir, final String domain, final String username ) throws Exception
    {
        Directory.Users.List diruserlist = dir.users().list()
                                                        .setDomain( domain )
                                                        .setQuery( "email:" + username + "*" );
        return diruserlist.execute();
    }

    private User updatePassword( final User user, final String password ) throws Exception
    {
        final MessageDigest md = MessageDigest.getInstance( "MD5" );    //I've been warned that this is not thread-safe
        final byte[] digested = md.digest( password.getBytes( "UTF-8" ) );
        final String newHashword = DatatypeConverter.printHexBinary( digested );
        return user.setHashFunction("MD5")                              //only accepts MD5, SHA-1, or CRYPT
                    .setPassword( newHashword );
    }

    private void updateDirectory( final Directory dir, final User user ) throws IOException
    {
        final Directory.Users.Update updateRequest = dir.users().update( user.getPrimaryEmail(), user );
        updateRequest.execute();
    }
}
