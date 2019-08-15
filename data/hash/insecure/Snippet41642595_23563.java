import java.nio.file.{Files, Paths}
import java.security.MessageDigest

object Test {

  def main(args: Array[String]) = {

    val startTime = System.currentTimeMillis();
    val byteArray = Files.readAllBytes(Paths.get("/Users/amir/pgns/bigPGN.pgn"))
    val endTime = System.currentTimeMillis();
    println("Read file into byte " +byteArray+ " in " + (endTime - startTime) +" ms");

    val startTimeHash = System.currentTimeMillis();
    val hash = MessageDigest.getInstance("MD5").digest(byteArray)
    val endTimeHash = System.currentTimeMillis();
    System.out.println("hashed file into " +hash+ " in " +(endTime - startTime)+ " ms");
  }
}
