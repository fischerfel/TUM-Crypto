#! /usr/bin/python

import sys
from javax.net.ssl import TrustManager, X509TrustManager
from jarray import array
from javax.net.ssl import SSLContext

class TrustAllX509TrustManager(X509TrustManager):

    # Define a custom TrustManager which will blindly
    # accept all certificates
    def checkClientTrusted(self, chain, auth):
        pass

    def checkServerTrusted(self, chain, auth):
        pass

    def getAcceptedIssuers(self):
        return None

# Create a static reference to an SSLContext which will use
# our custom TrustManager
trust_managers = array([TrustAllX509TrustManager()], TrustManager)
TRUST_ALL_CONTEXT = SSLContext.getInstance("SSL")
TRUST_ALL_CONTEXT.init(None, trust_managers, None)

# Keep a static reference to the JVM's default SSLContext for restoring
# at a later time
DEFAULT_CONTEXT = SSLContext.getDefault()


def trust_all_certificates(f):
    # Decorator function that will make it so the context of the decorated
    # method will run with our TrustManager that accepts all certificates

    def wrapped(*args, **kwargs):
        # Only do this if running under Jython
        if 'java' in sys.platform:
            from javax.net.ssl import SSLContext
            SSLContext.setDefault(TRUST_ALL_CONTEXT)
            print "SSLContext set to TRUST_ALL"
            try:
                res = f(*args, **kwargs)
                return res
            finally:
                SSLContext.setDefault(DEFAULT_CONTEXT)
        else:
            return f(*args, **kwargs)

    return wrapped

#@trust_all_certificates
def read_page(host):
    import httplib 
    print "Host: " + host
    conn = httplib.HTTPSConnection(host)
    conn.set_debuglevel(1)
    conn.request('GET', '/example')
    response = conn.getresponse()
    print response.read()

read_page("twitter.com")
