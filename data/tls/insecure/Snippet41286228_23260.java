import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.server.*
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer
import org.slf4j.LoggerFactory
import java.io.*
import java.net.InetAddress
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.websocket.*
import javax.websocket.server.ServerEndpoint

(...)

@Throws(Exception::class)
    private fun getServerConnector(): ServerConnector {
        val connector: ServerConnector
        if (USE_SSL) {
            LOG.info("** Using SSL for all websocket connections **")
            val contextFactory = SslContextFactory()
            contextFactory.sslContext = this.buildSSLContext()
            val sslConnectionFactory = SslConnectionFactory(contextFactory, HttpVersion.HTTP_1_1.toString())

            val config = HttpConfiguration()
            config.secureScheme = "https"
            config.securePort = settings.getPort()
            config.outputBufferSize = Int.MAX_VALUE
            config.requestHeaderSize = 8192
            config.responseHeaderSize = 8192
            val sslConfiguration = HttpConfiguration(config)
            sslConfiguration.addCustomizer(SecureRequestCustomizer())
            val httpConnectionFactory = HttpConnectionFactory(sslConfiguration)

            connector = ServerConnector(this.server, sslConnectionFactory, httpConnectionFactory)
        } else {
            val config = HttpConfiguration()
            config.securePort = settings.getPort()
            config.outputBufferSize = Int.MAX_VALUE
            config.requestHeaderSize = 8192
            config.responseHeaderSize = 8192
            val httpConnectionFactory = HttpConnectionFactory()
            connector = ServerConnector(this.server, httpConnectionFactory)
        }
        connector.port = settings.getPort()
        return connector
    }

@Throws(Exception::class)
    private fun buildSSLContext(): SSLContext {
        val ks = KeyStore.getInstance("JKS")
        val kf = File(KEYSTORE_FILE)
        ks.load(FileInputStream(kf), KEYSTORE_PASSWORD.toCharArray())

        val kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(ks, KEY_PASSWORD.toCharArray())

        val tmf = TrustManagerFactory.getInstance("SunX509")
        tmf.init(ks)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(kmf.keyManagers, tmf.trustManagers, null)

        return sslContext
    }
