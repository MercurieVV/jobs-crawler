package com.github.mercurievv.jobsearch

import cats.{FunctorFilter, Monad, ~>}
import cats.effect.{Async, ConcurrentEffect}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.github.mercurievv.jobsearch.AppHandler.sToFs2
import com.github.mercurievv.jobsearch.businesslogic.{CollectJobs, JobsServer, JobsStorage}
import com.github.mercurievv.jobsearch.model.Job
import com.github.mercurievv.jobsearch.persistence.DynamodbJobsStorage
import com.github.mercurievv.jobsearch.stackowerflow.{GetJobsFromStackowerflow, StackowerflowJobsApi}
import fs2.Stream
import org.http4s.client.Client

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/13/2020
 * Time: 4:14 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class Module[F[_]: Monad: Async, S[_]: Monad : FunctorFilter](
                                                        httpClient: Client[F],
                                                        seqToS: Seq ~> S,
                                                        sToFs2: S ~> Stream[F, *])(implicit ce: ConcurrentEffect[F]) {

  private val dbclient = {
    val conf = new EndpointConfiguration("https://dynamodb.eu-west-1.amazonaws.com", "eu-west-1")
    AmazonDynamoDBAsyncClientBuilder.standard().withEndpointConfiguration(conf).build()
  }
  val dynDbStorage = new DynamodbJobsStorage[F](dbclient)
  val saveJobs: S[Job] => F[Unit] = s => dynDbStorage.saveJobsToDb (sToFs2(s))

  val jobs = new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](httpClient))
  val collectJobs = new CollectJobs(saveJobs)
  val jobsServers = List(new JobsServer.StackOwerflow(new GetJobsFromStackowerflow[F](new StackowerflowJobsApi[F](httpClient)), seqToS))
}
