package com.github.mercurievv.jobsearch.remote.upwork

import cats.effect._
import com.github.mercurievv.http4s.coders.scalaxb.Http4sScalaxbEncodersDecoders._
import com.github.mercurievv.jobsearch.xml.CustomXmlProtocol._
import com.github.mercurievv.rss.generated.Rss
import org.http4s.client.Client
import org.http4s.implicits._

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 6/6/2020
 * Time: 8:24 AM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class UpworkJobsApi [F[_]](httpClient: Client[F])(
  implicit
  C: ConcurrentEffect[F]) {
  def getJobsRss: F[Rss] = {
    val target = uri"https://www.upwork.com/ab/feed/jobs/rss?q=scala&sort=recency"
    httpClient.expect[Rss](target)
  }
}