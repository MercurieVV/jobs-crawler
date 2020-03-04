package com.github.mercurievv.jobsearch.stackowerflow

import cats.effect._
import com.github.mercurievv.rss.generated.Rss
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.language.higherKinds
/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/4/2020
 * Time: 2:21 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class StackowerflowJobs[F[_]](httpClient: Client[F]) {
  def getJobsRss: IO[String] = {
    val target = uri"https://stackoverflow.com/jobs/feed?q=scala&r=true"
    httpClient.expect[Rss](target)
  }}
