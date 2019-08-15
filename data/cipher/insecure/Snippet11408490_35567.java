<%@ page import="java.io.*" %>
<%@ page import="java.security.*" %>
<%@ page import="javax.crypto.*" %>
<%@ page import="javax.crypto.spec.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="org.apache.commons.codec.binary.*" %>

    <HTML>
    <HEAD>
    <TITLE> Cheers! </TITLE>
    </HEAD>
    <BODY>

    <%
        String algorithm1 = "DES";//magical mystery constant
        String algorithm2 = "DES/CBC/NoPadding";//magical mystery constant
        IvParameterSpec iv = new IvParameterSpec( new byte [] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 } );//magical mystery constant
        Cipher cipher;
        SecretKey key;
        String k="12345abc"; //Just a random pick for testing
        key = new SecretKeySpec( k.getBytes("UTF-8"), algorithm1 );
        cipher = Cipher.getInstance( algorithm2 );

        String str="test4abc"; //Test String, 8 characters

        cipher.init( Cipher.ENCRYPT_MODE, key, iv ); //normally you could leave out the IvParameterSpec argument, but not with Oracle

        byte[] bytes=str.getBytes("UTF-8");

        byte[] encrypted = cipher.doFinal( bytes );

        String encoded = new String( Hex.encodeHex( encrypted ) );
        out.println( "Encrypted/Encoded: \"" + encoded + "\"" );



        cipher.init( Cipher.DECRYPT_MODE, key, iv );    

        //byte [] decoded = org.apache.commons.codec.binary.Hex.decodeHex( encoded.toCharArray( ) );
        byte [] decoded = Hex.decodeHex( encoded.toCharArray( ) );

        String decrypted = new String (cipher.doFinal( decoded ));
        out.println("DECRYPTED: \"" + decrypted + "\"" );
    }

    %>  
    </BODY>
    </HTML>
