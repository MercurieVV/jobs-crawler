package com.github.mercurievv.jobsearch

import cats.Monad
import cats.effect.ConcurrentEffect
import com.github.mercurievv.jobsearch.businesslogic.{CollectJobs, JobsStorage}
import com.github.mercurievv.jobsearch.persistence.DynamodbJobsStorage
import com.github.mercurievv.jobsearch.stackowerflow.{GetJobsFromStackowerflow, StackowerflowJobsApi}
import org.http4s.client.Client

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/13/2020
 * Time: 4:14 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class Module[F[_]: Monad, S[_]](client: Client[F])(implicit ce: ConcurrentEffect[F]) {
  import org.http4s.client.blaze._
  import zio._
  import zio.interop.catz._

  import scala.concurrent.ExecutionContext.global
  //    implicit val cs: ContextShift[AIO] = cats.effect.IO.contextShift(global)
  //    implicit val timer: Timer[AIO] = cats.effect.IO.timer(global)
  //    val runtime = Runtime.default
  //    import runtime._
  val dbStorage = new DynamodbJobsStorage()
  val jobs = new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](client))
  val jobsOps : JobsStorage[F, S] = ???
  val collectJobs = new CollectJobs(jobsOps)
}
