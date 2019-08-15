   public static void main(String[] args) throws SQLException, Exception {
        Connection con = ConnectionDefinition.getOracleConnection(); //my oracle connection
        PreparedStatement pr = con.prepareStatement("select  utl_raw.cast_to_raw(dbms_obfuscation_toolkit.md5(input_string =>'password')) from dual");
        ResultSet rs = pr.executeQuery();
        rs.next();
        byte[] raw = rs.getBytes(1);
        String str = new String(raw);
        System.err.println(str);        

        //Generate MD5 Java
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update("password".getBytes());        
        byte byteData[] = md.digest();
        System.err.println(new String(byteData));        
        rs.close();
        pr.close();
        con.close();        
    }
