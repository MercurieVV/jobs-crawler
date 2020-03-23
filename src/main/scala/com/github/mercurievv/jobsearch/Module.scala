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
  val dbStorage = new DynamodbJobsStorage()
  val jobs = new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](client))
  val jobsOps : JobsStorage[F, S] = ???
  val collectJobs = new CollectJobs(jobsOps)
}
