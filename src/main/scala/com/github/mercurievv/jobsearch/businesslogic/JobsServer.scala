package com.github.mercurievv.jobsearch.businesslogic

import com.github.mercurievv.jobsearch.model.Job

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/19/2020
 * Time: 10:39 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
trait JobsServer[F[_], S[_]] {
  def getJobsFromServer: F[S[Job]]

}
