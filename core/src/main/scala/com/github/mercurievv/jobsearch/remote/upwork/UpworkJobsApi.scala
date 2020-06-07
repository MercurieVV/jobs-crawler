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
    val target = uri"https://www.upwork.com/ab/feed/jobs/rss?and_terms=scala&exclude_terms=kotlin+java+javascript+python+php+ruby&sort=recency&paging=0%3B10&api_params=1&q=scala+AND+NOT+%28kotlin+OR+java+OR+javascript+OR+python+OR+php+OR+ruby%29"
    httpClient.expect[Rss](target)
  }
}