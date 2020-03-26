package com.github.mercurievv.jobsearch.businesslogic

import com.github.mercurievv.jobsearch.model.Job

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/19/2020
 * Time: 9:48 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
trait JobsStorage[F[_], S[_]] {
//  def filterNewJobsOnly(allJobs: S[Job]): S[Job]
  def saveJobsToDb(jobs: S[Job]): F[Unit]

}
