package com.github.mercurievv.jobsearch.stackowerflow

import java.time.ZonedDateTime

import cats.Monad
import cats.data.{NonEmptyChain, Validated}
import cats.implicits._
import com.github.mercurievv.jobsearch._
import com.github.mercurievv.jobsearch.model._
import org.http4s.Uri

import scala.util.Try

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
      _.channel.item.map(
        item =>
          (
            JobsResource.StackOverflow.validNec,
            item.guid.toValidNec("guid").map(_.value).map(Id(_)),
            item.link.toValidNec("link").map(Uri.unsafeFromString),
            item.title.toValidNec("title"),
            item.description.toValidNec("description"),
            item.category.map(_.value).map(Tag(_)).validNec,
            item.author.toValidNec("author").map(Company(_)),
            item.pubDate
              .toRight("pubDate")
              .flatMap(pd => Try(pd.toGregorianCalendar.toZonedDateTime).toEither.left.map(_.getMessage))
              .toValidatedNec
          ).mapN(Job)
      )
    )

}
