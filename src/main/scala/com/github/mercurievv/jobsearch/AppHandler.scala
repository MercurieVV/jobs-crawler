package com.github.mercurievv.jobsearch

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 5/30/2020
  * Time: 6:58 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
import java.io.{InputStream, OutputStream}

import cats.arrow.FunctionK
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import com.github.mercurievv.bulyon.lambdahttp4s.fs2zio.SimpleZioHandler
import fs2._
import org.http4s._
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io.{->, /, POST}
import zio.ZIO
import zio.blocking.Blocking
import zio.console.{putStrLn, Console}
import zio.interop.catz._
import zio.interop.catz.implicits._
import cats.implicits._
import cats._
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder
import com.github.mercurievv.jobsearch.AppHandler.AppEnvironment
import org.http4s.client.middleware.{RequestLogger, ResponseLogger}

import scala.concurrent.ExecutionContext.global

object AppHandler extends RequestStreamHandler {
  import zio._
  import zio.clock.Clock
  import zio.interop.catz._
  type AppEnvironment = Clock with Console with Blocking

  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    println(Runtime
      .default
      .unsafeRunSync(run(Runtime.default)))
  }
  private val seqToList = λ[Seq ~> List](_.toList)
  private val sToFs2: List ~> Stream[AIO, *] = λ[List ~> fs2.Stream[AIO, *]](fs2.Stream.emits(_))

  def run[R <: Console](implicit runtime: Runtime[R]): ZIO[R, Nothing, Int] = {
    BlazeClientBuilder[AIO](global)
      .resource
      .toManagedZIO
      .use {
        client =>
          val clientLogged = RequestLogger(logHeaders = false, logBody = false)(ResponseLogger(logHeaders = false, logBody = true)(client))
          val module = new Module[AIO, List](clientLogged, seqToList, sToFs2)
          module.collectJobs.collectJobsFromServers(module.jobsServers)
      }
      .foldM(
          err => putStrLn(s"Execution failed with: $err").as(1),
          _ => ZIO.succeed(0)
      )
  }
}
