public class DBConnection {
        java.sql.Connection conn = null;


    public static java.sql.Connection connect(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1;"+"user=looi963;password=1234;database=user");
            return conn;
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
            return null;
        }

}
    public void regis(String name,String password) throws SQLException
    {
        connect();
        PreparedStatement state=conn.prepareStatement("Insert into user where username=? and password=?");
        state.setString(1,name);
        state.setString(2, md5(password));

        int count = state.executeUpdate();
       if(count>0)
       { 
           System.out.println("aaddde");
       }
    }
    private String md5(String c) {
        try {
            MessageDigest digs = MessageDigest.getInstance("MD5");
            digs.update((new String(c)).getBytes("UTF8"));
            String str = new String(digs.digest());
            return str;
        }catch (Exception ex){
            return "";
        }
