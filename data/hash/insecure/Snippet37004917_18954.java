import java.sql.DriverManager
import scala.collection.mutable.ArrayBuffer
import java.sql.ResultSet
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import net.spy.memcached.MemcachedClient
import net.spy.memcached.AddrUtil
import net.spy.memcached.BinaryConnectionFactory
import com.sun.rowset.CachedRowSetImpl
import java.util.concurrent.TimeoutException
import java.util.concurrent.TimeUnit

object Memcachedtest 
{
  def MD5encode(query_line:String):String=
    {
        var md5:String=null;

        if (query_line == null) return null;

        try {
            val digest1:MessageDigest = MessageDigest.getInstance("MD5");
             val hash:Array[Byte]  =digest1.digest(query_line.getBytes());
            val sb:StringBuilder = new StringBuilder(2*hash.length);

            digest1.update(query_line.getBytes());
            for(b <- hash)
            {
                sb.append("%02x".format( b&0xff));
            }
            md5=sb.toString();
        }
        catch 
        {
        case e:NoSuchAlgorithmException => e.printStackTrace();
        }
         md5
    }

  def memcache_get_result(mysql_table_statement:String)
  {
    var crsi:CachedRowSetImpl=new CachedRowSetImpl();
    var mem_client:MemcachedClient=new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses("IP"))

         var obj:Object=null
         Class.forName("com.mysql.jdbc.Driver");

            val conn =  DriverManager.getConnection("jdbc:mysql:IP","user","pass");
        val query_md5_res=MD5encode(mysql_table_statement);
             var future_object=mem_client.asyncGet(query_md5_res);
            try {
                obj=future_object.get(5, TimeUnit.SECONDS);
            }
            catch {

              case t:TimeoutException =>    future_object.cancel(false);
                System.out.println("Memcached timeout...");
            }
            if (obj==null) {
                System.out.print("Query result not in Memcached, ");
                var res:ResultSet = conn.createStatement().executeQuery(mysql_table_statement);
                crsi.populate(res);

                res.close();
                mem_client.set(query_md5_res, 10, crsi);
                while (crsi.next()) {

                  System.out.print("output : " + crsi.getString("COLUMN_ID"));
                }
                crsi.close();
            }
            else  {
                System.out.print("Query result in Memcached, ");
                var crsi_res_set:CachedRowSetImpl=obj.asInstanceOf[CachedRowSetImpl]
                System.out.println(crsi_res_set);
                crsi_res_set.beforeFirst();
                while (crsi_res_set.next()) {

                    System.out.print("output : " + crsi.getString("COLUMN_ID"));
                }
                crsi_res_set.close();
            }

        if (conn != null) {
            conn.close();
        }
  }

   def main(args:Array[String])
    {
      var crsi:CachedRowSetImpl=new CachedRowSetImpl();
      var mem_client:MemcachedClient=new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses("IP"))
      memcache_get_result("select statement")
      memcache_get_result("select statement")

        mem_client.shutdown();
    }


}
