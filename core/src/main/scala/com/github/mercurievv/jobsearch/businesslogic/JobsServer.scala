package com.github.mercurievv.jobsearch.businesslogic

import cats.data.NonEmptyChain
import cats._
import cats.implicits._
import com.github.mercurievv.jobsearch.Errorable
import com.github.mercurievv.jobsearch.model.Job
import com.github.mercurievv.jobsearch.remote.stackowerflow.{RssItemToJobConverter, StackowerflowJobsApi}
import com.github.mercurievv.jobsearch.remote.upwork.UpworkJobsApi
import simulacrum._

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/19/2020
 * Time: 10:39 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
import enumeratum._

sealed trait JobsServer[F[_], S[_]] /*extends EnumEntry*/ {
  def getJobsFromServer: F[S[Errorable[Job]]]

}

object JobsServer /*extends Enum[JobsServer[*, *]]*/ {
//  val values = findValues

  final class StackOwerflow[F[_] : Monad, S[_]](api: StackowerflowJobsApi[F], seqToS: Seq ~> S) extends JobsServer[F, S] {
    override def getJobsFromServer: F[S[Errorable[Job]]] = api.getJobsRss.map(RssItemToJobConverter(_)).map(seqToS(_))
  }

  final class Upwork[F[_] : Monad, S[_]](api: UpworkJobsApi[F], seqToS: Seq ~> S) extends JobsServer[F, S] {
    override def getJobsFromServer: F[S[Errorable[Job]]] = api.getJobsRss.map(RssItemToJobConverter(_)).map(seqToS(_))
  }
}
