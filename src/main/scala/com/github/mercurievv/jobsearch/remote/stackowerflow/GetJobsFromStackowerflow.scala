package com.github.mercurievv.jobsearch.remote.stackowerflow

import java.time.ZonedDateTime

import cats.Monad
import cats.data.{Validated, ValidatedNec}
import cats.implicits._
import com.github.mercurievv.jobsearch._
import com.github.mercurievv.jobsearch.model._
import com.github.mercurievv.rss.generated.Item
import org.http4s.Uri

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 3/10/2020
  * Time: 11:49 AM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
class GetJobsFromStackowerflow[F[_]](
  stackowerflowJobsApi: StackowerflowJobsApi[F]
)(implicit F: Monad[F]) {
  val getJobs: F[Seq[Errorable[Job]]] = stackowerflowJobsApi.getJobsRss
    .map(
      _.channel
        .item
        .map(mapRssItemToJob)
    )

  def mapRssItemToJob(item : Item): ValidatedNec[ParsingError, Job] = {
      val ur: String => ValidatedNec[ParsingError, Uri] = (s: String) => Uri
        .fromString(s)
        .leftMap(new ParsingError(_))
        .toValidatedNec
      val value: ValidatedNec[ParsingError, Uri] = item
        .link
        .toValidNec(ParsingError("link"))
        .andThen(ur)

      val zdt: ValidatedNec[ParsingError, ZonedDateTime] = item
        .pubDate
        .toValidNec(ParsingError("pubDate"))
        .andThen(gc => Validated.catchNonFatal(gc.toGregorianCalendar.toZonedDateTime).leftMap(new ParsingError(_)).toValidatedNec)

      (
        JobsResource.StackOverflow.validNec,
        item.guid.toValidNec(ParsingError("guid")).map(_.value).map(Id(_)),
        value,
        item.title.toValidNec(ParsingError("title")),
        item.description.toValidNec(ParsingError("description")),
        item.category.map(_.value).map(Tag(_)).validNec,
        item.author.toValidNec(ParsingError("author")).map(Company(_)),
        zdt
        ).mapN(Job)
  }

  case class ParsingError(message: String, cause: Option[Throwable] = None) extends Throwable(message){
    def this(cause: Throwable) {
      this(cause.getMessage, Some(cause))
    }
  }
}
