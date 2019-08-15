@Grab(group='org.jsoup', module='jsoup', version='1.7.3')
import org.jsoup.Jsoup

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

def nullTrustManager = [
checkClientTrusted: { chain, authType ->  },
checkServerTrusted: { chain, authType ->  },
getAcceptedIssuers: { null }
]

def nullHostnameVerifier = [
verify: { hostname, session -> true }
]

SSLContext sc = SSLContext.getInstance("SSL")
sc.init(null, [nullTrustManager as X509TrustManager] as TrustManager[],     null)
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
HttpsURLConnection.setDefaultHostnameVerifier(nullHostnameVerifier as     HostnameVerifier)

def (doc,files,dirs)  = 
    [Jsoup.connect('https://webdav/address').cookie('JSESSIONID','XYZsessionid').get(),[],[]]
doc.select("[href]").each{href ->
    def filename = href.text()
    def path = href.attr('href')
    path.endsWith("/")?dirs.add(filename):files.add(filename)
    }
println """DIRECTORIES :
${dirs.join('\n')}
FILES : 
${files.join('\n')}
"""
