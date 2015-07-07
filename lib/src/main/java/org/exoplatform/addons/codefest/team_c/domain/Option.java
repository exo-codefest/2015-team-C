/* 
* Copyright (C) 2003-2015 eXo Platform SAS.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see http://www.gnu.org/licenses/ .
*/
package org.exoplatform.addons.codefest.team_c.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
public class Option {

  public static Long counterId = 0l;

  private Long id;
  private List<Long> choices;
  private Date startDate;
  private Date endDate;

  public Option() {
    this.id = counterId++;
  }

  public Option(Long id, List<Long> choices, Date startDate, Date endDate) {
    this.id = id;
    this.choices = choices;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Option(List<Long> choices, Date startDate, Date endDate) {
    this.id = counterId++;
    this.choices = choices;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<Long> getChoices() {
    return choices;
  }

  public void setChoices(List<Long> choices) {
    this.choices = choices;
  }
}

