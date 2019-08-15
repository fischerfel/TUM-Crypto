import org.apache.http.client.methods.HttpPost
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils
import java.security.SecureRandom
import javax.net.ssl.*

fun main(args: Array<String>) {
  val client = SSLClient()

  val auth = AuthRequest()
  val ares = auth.post(client)
  val token = ares.value
  println(token)
}

class SSLClient {
    val client : CloseableHttpClient

    init {
        val config = Config
        config.loadCerts()
        val ctx = SSLContext.getInstance("TLSv1.2")
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(config.clientSSLCerts, config.keyPassword.toCharArray())
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(config.serverSSLCerts)
        ctx.init(kmf.keyManagers, tmf.trustManagers, SecureRandom())
        val sslsf = SSLConnectionSocketFactory(ctx)

        val r = RegistryBuilder.create<ConnectionSocketFactory>()
                .register("https", sslsf)
                .build()
        val cm = PoolingHttpClientConnectionManager(r)
        client = HttpClients.custom()
                .setConnectionManager(cm)
                .build()
    }

    fun post(endpoint : String) : HttpPost {
        val post = HttpPost(endpoint)
        post.addHeader("Content-Type", "application/xml")
        return post
    }

    fun execute(post : HttpPost, xmlBody : String) : String {
        println(xmlBody)
        post.entity = StringEntity(xmlBody, Charsets.UTF_8)
        client.execute(post).use { response ->
            response.allHeaders.forEach { println(it.toString()) }
            println(response.statusLine.toString())
            if (response.statusLine.statusCode !in 200..299) {
                throw Exception(response.statusLine.toString())
            }
            val entity = response.entity
            if (entity == null || entity.contentLength == 0L) {
                throw Exception("No body content in HTTP response")
            }
            val result = EntityUtils.toString(entity)
            println(result)
            return result
        }
    }
}
