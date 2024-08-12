package com.ymo.data.remote

import okhttp3.OkHttpClient
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier

/**
 * Creates an OkHttpClient that accepts all self-signed certificates.
 */
fun createUnsafeOkHttpClient(): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Trust all clients
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // Trust all servers
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                // Return an empty array
                return arrayOf()
            }
        }
    )

    val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    val hostnameVerifier = HostnameVerifier { _, _ -> true }

    return OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier(hostnameVerifier)
        .build()
}