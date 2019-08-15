def md5(s: String) = {
    MessageDigest.getInstance("MD5").digest(s.getBytes).
           map("%02x".format(_)).mkString.substring(0,8)
  }

val rdd=sc.makeRDD(Array(1,8,6,4,9,3,76,4))//.collect().foreach(println)
val rdd2 = rdd.map(r=>(r+"s",Array(1.0,2.0)))

rdd2.map{
  case(a,b) => (md5(a)+"_"+a,b)
}.foreach(println)
