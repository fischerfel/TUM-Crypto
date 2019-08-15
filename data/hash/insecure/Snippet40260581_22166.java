package com.nil.ltpa;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesError;
import lotus.domino.NotesException;
import lotus.domino.View;

import com.nil.exception.Base64DecodeException;
import com.nil.helpers.HttpUtils;

public class LtpaLibrary {

private static final byte[]    ltpaTokenVersion        = { 0, 1, 2, 3 };
private static final int        dateStringLength        = 8;
private static final String    dateStringFiller        = "00000000";
private static final int        creationDatePosition    = ltpaTokenVersion.length;
private static final int        expirationDatePosition    = creationDatePosition + dateStringLength;
private static final int        preUserDataLength        = ltpaTokenVersion.length + dateStringLength + dateStringLength;
private static final int        hashLength            = 20;

/** This method parses the LtpaToken cookie received from the web browser and returns the information in the <tt>TokenData</tt>
 * class.
 * @param ltpaToken - the cookie (base64 encoded).
 * @param ltpaSecretStr - the contents of the <tt>LTPA_DominoSecret</tt> field from the SSO configuration document.
 * @return The contents of the cookie. If the cookie is invalid (the hash - or some other - test fails), this method returns
 * <tt>null</tt>.
 * @throws NoSuchAlgorithmException
 * @throws Base64DecodeException
 */
public static TokenData parseLtpaToken( String ltpaToken, String ltpaSecretStr ) throws NoSuchAlgorithmException,
        Base64DecodeException {
    byte[] data = HttpUtils.base64Decode( ltpaToken );

    int variableLength = data.length - hashLength;
    /* Compare to 20 to since variableLength must be at least (preUserDataLength + 1) [21] character long:
     * Token version: 4 bytes
     * Token creation: 8 bytes
     * Token expiration: 8 bytes
     * User name: variable length > 0
     */
    if( variableLength <= preUserDataLength ) return null;

    byte[] ltpaSecret = HttpUtils.base64Decode( ltpaSecretStr );

    if( !validateSHA( data, variableLength, ltpaSecret ) ) return null;

    if( !compareBytes( data, 0, ltpaTokenVersion, 0, ltpaTokenVersion.length ) ) return null;

    TokenData ret = new TokenData();
    ret.tokenCreated.setTimeInMillis( (long)Integer.parseInt( getString( data, creationDatePosition, dateStringLength ), 16 ) * 1000 );
    ret.tokenExpiration
            .setTimeInMillis( (long)Integer.parseInt( getString( data, expirationDatePosition, dateStringLength ), 16 ) * 1000 );

    byte[] nameBuffer = new byte[ data.length - ( preUserDataLength + hashLength ) ];
    System.arraycopy( data, preUserDataLength, nameBuffer, 0, variableLength - preUserDataLength );
    ret.username = new String( nameBuffer );

    return ret;
}

private static boolean validateSHA( byte[] ltpaTokenData, int variableLength, byte[] ltpaSecret ) throws NoSuchAlgorithmException {
    MessageDigest sha1 = MessageDigest.getInstance( "SHA-1" );

    byte[] digestData = new byte[ variableLength + ltpaSecret.length ];

    System.arraycopy( ltpaTokenData, 0, digestData, 0, variableLength );
    System.arraycopy( ltpaSecret, 0, digestData, variableLength, ltpaSecret.length );

    byte[] digest = sha1.digest( digestData );

    if( digest.length > ltpaTokenData.length - variableLength ) return false;

    int bytesToCompare = ( digest.length <= ltpaTokenData.length - variableLength ) ? digest.length : ltpaTokenData.length
            - variableLength;

    return compareBytes( digest, 0, ltpaTokenData, variableLength, bytesToCompare );
}

private static boolean compareBytes( byte[] b1, int offset1, byte[] b2, int offset2, int len ) {
    if( len < 0 ) return false;
    // offset must be positive, and the compare chunk must be contained the buffer
    if( ( offset1 < 0 ) || ( offset1 + len > b1.length ) ) return false;
    if( ( offset2 < 0 ) || ( offset2 + len > b2.length ) ) return false;

    for( int i = 0; i < len; i++ )
        if( b1[ offset1 + i ] != b2[ offset2 + i ] ) return false;

    return true;
}

/** Convert bytes from the buffer into a String.
 * @param buffer - the buffer to take the bytes from.
 * @param offset - the offset in the buffer to start at.
 * @param len - the number of bytes to convert into a String.
 * @return - A String representation of specified bytes.
 */
private static String getString( byte[] buffer, int offset, int len ) {
    if( len < 0 ) return "";
    if( ( offset + len ) > buffer.length ) return "";

    byte[] str = new byte[ len ];
    System.arraycopy( buffer, offset, str, 0, len );
    return new String( str );
}

/** Create a valid LTPA token for the specified user. The creation time is <tt>now</tt>.
 * @param username - the user to create the LTPA token for.
 * @param durationMinutes - the duration of the token validity in minutes.
 * @param ltpaSecretStr - the LTPA Domino Secret to use to create the token.
 * @return - base64 encoded LTPA token, ready for the cookie.
 * @throws NoSuchAlgorithmException
 * @throws Base64DecodeException
 */
public static String createLtpaToken( String username, int durationMinutes, String ltpaSecretStr ) throws NoSuchAlgorithmException,
        Base64DecodeException {
    return createLtpaToken( username, new GregorianCalendar(), durationMinutes, ltpaSecretStr );
}

/** Create a valid LTPA token for the specified user.
 * @param username - the user to create the LTPA token for.
 * @param creationTime - the time the token becomes valid.
 * @param durationMinutes - the duration of the token validity in minutes.
 * @param ltpaSecretStr - the LTPA Domino Secret to use to create the token.
 * @return - base64 encoded LTPA token, ready for the cookie.
 * @throws NoSuchAlgorithmException
 * @throws Base64DecodeException
 */
public static String createLtpaToken( String username, GregorianCalendar creationTime, int durationMinutes, String ltpaSecretStr )
        throws NoSuchAlgorithmException, Base64DecodeException {
    // create byte array buffers for both strings
    byte[] ltpaSecret = HttpUtils.base64Decode( ltpaSecretStr );
    byte[] usernameArray = username.getBytes();

    byte[] workingBuffer = new byte[ preUserDataLength + usernameArray.length + ltpaSecret.length ];

    // copy version into workingBuffer
    System.arraycopy( ltpaTokenVersion, 0, workingBuffer, 0, ltpaTokenVersion.length );

    GregorianCalendar expirationDate = (GregorianCalendar)creationTime.clone();
    expirationDate.add( Calendar.MINUTE, durationMinutes );

    // copy creation date into workingBuffer
    String hex = dateStringFiller + Integer.toHexString( (int)( creationTime.getTimeInMillis() / 1000 ) ).toUpperCase();
    System
            .arraycopy( hex.getBytes(), hex.getBytes().length - dateStringLength, workingBuffer, creationDatePosition,
                    dateStringLength );

    // copy expiration date into workingBuffer
    hex = dateStringFiller + Integer.toHexString( (int)( expirationDate.getTimeInMillis() / 1000 ) ).toUpperCase();
    System.arraycopy( hex.getBytes(), hex.getBytes().length - dateStringLength, workingBuffer, expirationDatePosition,
            dateStringLength );

    // copy user name into workingBuffer
    System.arraycopy( usernameArray, 0, workingBuffer, preUserDataLength, usernameArray.length );

    // copy the ltpaSecret into the workingBuffer
    System.arraycopy( ltpaSecret, 0, workingBuffer, preUserDataLength + usernameArray.length, ltpaSecret.length );

    byte[] hash = createHash( workingBuffer );

    // put the public data and the hash into the outputBuffer
    byte[] outputBuffer = new byte[ preUserDataLength + usernameArray.length + hashLength ];
    System.arraycopy( workingBuffer, 0, outputBuffer, 0, preUserDataLength + usernameArray.length );
    System.arraycopy( hash, 0, outputBuffer, preUserDataLength + usernameArray.length, hashLength );

    return HttpUtils.base64Encode( outputBuffer );
}

private static byte[] createHash( byte[] buffer ) throws NoSuchAlgorithmException {
    MessageDigest sha1 = MessageDigest.getInstance( "SHA-1" );
    return sha1.digest( buffer );
}

private static boolean fieldContainsValue( Vector values, Item item ) throws NotesException {
    for( int i = 0; i < values.size(); i++ ) {
        if( item.containsValue( values.get( i ) ) ) return true;
    }
    return false;
}

/** Get the contents of the LTPA_Secret field of the correct SSO configuration document based on the Internet site host name.
 * @param names - the <strong>names.nsf</strong> database.
 * @param siteName - the Internet host name of the site to generate/verify Domino cookie for. 
 * @return A class containing LTPA data, <tt>null</tt> if no matching SSO configuration document was found.
 * @throws NotesException
 * @throws UnknownHostException
 */
public static LtpaData getLtpaSecret( Database names, String siteName ) throws NotesException {
    Vector searchValues = new Vector();
    searchValues.add( siteName );

    try {
        InetAddress addr = InetAddress.getByName( siteName );
        searchValues.add( addr.getHostAddress() );
    } catch( UnknownHostException e ) {
        // do nothing
    }

    View internetSites = names.getView( "($InternetSites)" );
    String ssoQuery = "LtpaToken";

    Vector vRecycle = new Vector( internetSites.getEntryCount() );
    Document webSite = internetSites.getFirstDocument();
    while( webSite != null ) {
        vRecycle.add( webSite );
        if( webSite.getItemValueString( "Form" ).equalsIgnoreCase( "WebSite" )
                && webSite.getItemValueString( "Type" ).equalsIgnoreCase( "WebSite" ) ) {
            // The correct type of document
            if( fieldContainsValue( searchValues, webSite.getFirstItem( "ISiteAdrs" ) ) ) {
                ssoQuery = webSite.getItemValueString( "ISiteOrg" ) + ":" + webSite.getItemValueString( "HTTP_SSOCfg" );
                break;
            }
        }
        webSite = internetSites.getNextDocument( webSite );
    }
    internetSites.recycle( vRecycle ); // recycle all the collected documents
    internetSites.recycle();

    View ssoConfigs = names.getView( "($WebSSOConfigs)" );
    Document ssoConfigDoc = ssoConfigs.getDocumentByKey( ssoQuery );
    ssoConfigs.recycle();

    if( ssoConfigDoc == null )
        throw new NotesException( NotesError.NOTES_ERR_SSOCONFIG, "Site \"" + siteName + "\" SSO config document not found." );

    LtpaData ret = new LtpaData( ssoConfigDoc.getItemValueString( "LTPA_DominoSecret" ), ssoConfigDoc
            .getItemValueInteger( "LTPA_TokenExpiration" ), ssoConfigDoc.getItemValueString( "LTPA_TokenDomain" ) );

    ssoConfigDoc.recycle();

    return ret;
}
}







package com.nil.ltpa;

public class LtpaData {

public String    ltpaSecret;
public String    tokenDomain;
public int    tokenExpiration;

public LtpaData() {
    ltpaSecret = "";
    tokenDomain = "";
    tokenExpiration = 0;
}

public LtpaData( String ltpaSecret, int tokenExpiration, String tokenDomain ) {
    super();
    this.ltpaSecret = ltpaSecret;
    this.tokenExpiration = tokenExpiration;
    this.tokenDomain = tokenDomain;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
public String toString() {
    return "LTPAData {Secret=" + ltpaSecret + ", expiration=" + tokenExpiration + ", domain=" + tokenDomain + "}";
}

}



package com.nil.ltpa;

import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

public class TokenData {
public static final SimpleTimeZone    utcTimeZone    = new SimpleTimeZone( 0, "UTC" );

public String                    username;
public GregorianCalendar            tokenCreated;
public GregorianCalendar            tokenExpiration;

public TokenData() {
    username = "";
    tokenCreated = new GregorianCalendar( utcTimeZone );
    tokenCreated.setTimeInMillis( 0 );
    tokenExpiration = new GregorianCalendar( utcTimeZone );
    tokenExpiration.setTimeInMillis( 0 );
}

public String toString() {
    StringBuffer buf = new StringBuffer();

    buf.append( "[ username:" ).append( username ).append( ", tokenCreated: " ).append( tokenCreated.getTime().toString() );
    buf.append( ", tokenExpiration: " ).append( tokenExpiration.getTime().toString() ).append( " ]" );

    return buf.toString();
}
}



package com.nil.helpers;

import java.util.Vector;

import com.nil.exception.Base64DecodeException;

public class HttpUtils {

private static final String    base64Chars    = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

public static final String base64Encode( byte[] bytes ) {
    if( bytes == null ) return null;

    StringBuffer ret = new StringBuffer();

    for( int sidx = 0, didx = 0; sidx < bytes.length; sidx += 3, didx += 4 ) {
        ret.append( base64Chars.charAt( ( bytes[ sidx ] >>> 2 ) & 077 ) );
        if( sidx + 1 < bytes.length ) {
            ret.append( base64Chars.charAt( ( bytes[ sidx + 1 ] >>> 4 ) & 017 | ( bytes[ sidx ] << 4 ) & 077 ) );
            if( sidx + 2 < bytes.length )
                ret.append( base64Chars.charAt( ( bytes[ sidx + 2 ] >>> 6 ) & 003 | ( bytes[ sidx + 1 ] << 2 ) & 077 ) );
            else
                ret.append( base64Chars.charAt( ( bytes[ sidx + 1 ] << 2 ) & 077 ) );
            if( sidx + 2 < bytes.length ) ret.append( base64Chars.charAt( bytes[ sidx + 2 ] & 077 ) );
        } else
            ret.append( base64Chars.charAt( ( bytes[ sidx ] << 4 ) & 077 ) );
    }

    int mod = ret.length() % 4;
    for( int i = 0; ( mod > 0 ) && ( i < 4 - mod ); i++ )
        ret.append( '=' );

    return ret.toString();
} // public static final String base64Encode( byte[] bytes )

public static final byte[] base64Decode( String data ) throws Base64DecodeException {
    if( data.length() == 0 ) return new byte[ 0 ];
    Vector dest = new Vector( data.length() );

    // ASCII printable to 0-63 conversion
    int prevBits = 0; // stores the bits left over from the previous step
    int modAdjust = 0; // stores the start of the current line.
    for( int i = 0; i < data.length(); i++ ) {
        char ch = data.charAt( i ); // get the character
        if( ch == '=' ) break; // is it the padding character, no check for correct position
        int mod = ( i - modAdjust ) % 4; // what is the index modulo 4 in the current line
        if( mod == 0 ) {
            // the line can only be broken on modulo 0 (e.g. 72, 76 character per line. MIME specifies 76 as max).
            if( ( ch == '\r' ) || ( ch == '\n' ) ) { // we handle the encoders that use '\n' only as well
                modAdjust = i + 1; // skip the [CR/]LF sequence. The new line probably starts at i + 1;
                continue;
            }
        }
        // if we came to here, there was no special character
        int x = base64Chars.indexOf( ch ); // search for the character in the table
        if( x < 0 ) throw new Base64DecodeException(); // if the character was not found raise an exception
        switch( mod ) {
            case 0:
                prevBits = x << 2; // just store the bits and continue
                break;
            case 1:
                dest.add( new Byte( (byte)( prevBits | x >>> 4 ) ) ); // previous 6 bits OR 2 new ones
                prevBits = ( x & 017 ) << 4; // store 4 bits
                break;
            case 2:
                dest.add( new Byte( (byte)( prevBits | x >>> 2 ) ) ); // previous 4 bits OR 4 new ones
                prevBits = ( x & 003 ) << 6; // store 2 bits
                break;
            case 3:
                dest.add( new Byte( (byte)( prevBits | x ) ) ); // previous 2 bits OR 6 new ones
                break;
        }
    }

    byte[] ret = new byte[ dest.size() ]; // convert the Vector into an array
    for( int i = 0; i < ret.length; i++ )
        ret[ i ] = ( (Byte)dest.get( i ) ).byteValue();

    return ret;
}

public static final boolean isBase64Encoded( String sBase64 ) {
    int len = sBase64.length();
    if( len % 4 != 0 ) return false;
    for( int i = 0; i < len; i++ ) {
        char c = sBase64.charAt( i );
        if( ( c >= 'a' ) && ( c <= 'z' ) ) continue;
        if( ( c >= 'A' ) && ( c <= 'Z' ) ) continue;
        if( ( c >= '0' ) && ( c <= '9' ) ) continue;
        if( ( c == '+' ) || ( c == '/' ) || ( c == '=' ) ) continue;
        return false;
    }
    return true;
}

}



package com.nil.exception;

public class Base64DecodeException extends Exception {
/**
 *
 */
private static final long    serialVersionUID    = -5600202677007235761L;

/**
 *
 */
public Base64DecodeException() {
    // Auto-generated constructor stub
}

/**
 * @param argMessage
 */
public Base64DecodeException( String argMessage ) {
    super( argMessage );
}

/**
 * @param argCause
 */
public Base64DecodeException( Throwable argCause ) {
    super( argCause );
}

/**
 * @param argMessage
 * @param argCause
 */
public Base64DecodeException( String argMessage, Throwable argCause ) {
    super( argMessage, argCause );
}

}
