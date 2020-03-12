package com.github.mercurievv.jobsearch.stackowerflow

import java.nio.ByteBuffer

import cats.Monad
import cats.data.EitherT
import cats.effect._
import com.github.mercurievv.rss.generated._
import com.github.mercurievv.rss.generated._
import scalaxb._
import org.http4s._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.util.{Failure, Success, Try}
import scala.xml.Node
import com.github.mercurievv.http4s.coders.scalaxb.Http4sScalaxbEncodersDecoders._
/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/4/2020
  * Time: 2:21 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class StackowerflowJobsApi[F[_]](httpClient: Client[F])(
  implicit
  C: ConcurrentEffect[F]) {
  def getJobsRss: F[Rss] = {
    val target = uri"https://stackoverflow.com/jobs/feed?q=scala&r=true"
    httpClient.expect[Rss](target)
  }
}
