/*
 * Copyright 2013-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.http4s.client.blaze

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLContext, X509TrustManager}
import org.http4s.BuildInfo
import org.http4s.blaze.util.TickWheelExecutor
import org.http4s.headers.`User-Agent`
import org.http4s.ProductId
import scala.concurrent.duration._

private[blaze] object bits {
  // Some default objects
  val DefaultResponseHeaderTimeout: Duration = 10.seconds
  val DefaultTimeout: Duration = 60.seconds
  val DefaultBufferSize: Int = 8 * 1024
  val DefaultUserAgent = Some(`User-Agent`(ProductId("http4s-blaze", Some(BuildInfo.version))))
  val DefaultMaxTotalConnections = 10
  val DefaultMaxWaitQueueLimit = 256

  @deprecated("Use org.http4s.blazecore.tickWheelResource", "0.19.1")
  lazy val ClientTickWheel = new TickWheelExecutor()

  /** Caution: trusts all certificates and disables endpoint identification */
  lazy val TrustingSslContext: SSLContext = {
    val trustManager = new X509TrustManager {
      def getAcceptedIssuers(): Array[X509Certificate] = Array.empty
      def checkClientTrusted(certs: Array[X509Certificate], authType: String): Unit = {}
      def checkServerTrusted(certs: Array[X509Certificate], authType: String): Unit = {}
    }
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, Array(trustManager), new SecureRandom)
    sslContext
  }
}
