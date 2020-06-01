package com.github.mercurievv.jobsearch

import cats.{FunctorFilter, Monad, ~>}
import cats.effect.ConcurrentEffect
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.github.mercurievv.jobsearch.businesslogic.{CollectJobs, JobsServer, JobsStorage}
import com.github.mercurievv.jobsearch.model.Job
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
class Module[F[_]: Monad, S[_]: Monad : FunctorFilter](
                                                        httpClient: Client[F],
                                                        seqToS: Seq ~> S,
                                                        saveJobs: S[Job] => F[Unit])(implicit ce: ConcurrentEffect[F]) {


  val jobs = new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](httpClient))
  val collectJobs = new CollectJobs(saveJobs)
  val jobsServers = List(new JobsServer.StackOwerflow(new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](httpClient)), seqToS))
}
