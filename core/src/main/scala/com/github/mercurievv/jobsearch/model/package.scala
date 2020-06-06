package com.github.mercurievv.jobsearch

import shapeless.tag
import shapeless.tag.@@

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 3/10/2020
 * Time: 5:19 PM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
package object model {
  trait IdTag
  type Id = String @@ IdTag
  object Id{
    def apply(o: String): Id = tag[IdTag][String](o)
  }

  trait TagTag
  type Tag = String @@ TagTag
  object Tag{
   def apply(o: String): Tag = tag[TagTag][String](o)
  }

  trait CompanyTag
  type Company = String @@ CompanyTag
  object Company{
   def apply(o: String): Company = tag[CompanyTag][String](o)
  }


}
