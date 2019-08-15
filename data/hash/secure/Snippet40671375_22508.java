import scala.util.matching.Regex
import java.security.MessageDigest

val inputPath = ""
val outputPath = ""

//finds mac addresses with given regex
def find(s: String, r: Regex): List[String] = {
    val l = r.findAllIn(s).toList
    if(!l.isEmpty){ 
        return l
    } else {
        val lis: List[String] = List("null")
        return lis
    }
}

//hashes given string with sha256
def hash(s: String): String = {
    return MessageDigest.getInstance("SHA-256").digest(s.getBytes).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
}

//hashes given line
def hashAll(s: String, r:Regex): String = {
    var st = s
    val macs = find(s, r)
    for (mac <- macs){
        st = st.replaceAll(mac, hash(mac))
    }
    return st
}

//read data
val rdd = sc.textFile(inputPath)

//mac address regular expression
val regex = "(([0-9A-Z]{1,2}[:-]){5}([0-9A-Z]{1,2}))".r

//hash data
val hashed_rdd = rdd.map(line => hashAll(line, regex))

//write hashed data
hashed_rdd.saveAsTextFile(outputPath)
