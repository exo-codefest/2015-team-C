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

import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
public class Meeting {

  public static Long counterId = 0l;

  private Long id;
  private String title;
  private String description;
  private User creator;
  private String status;
  private List<String> participants;
  private List<Long> options;
  private Option finalOption;

  public Meeting() {
    this.id = counterId++;
  }

  public Meeting(Long id, String title, String description, User creator, String status, List<String> participants, List<Long> options, Option finalOption) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.creator = creator;
    this.status = status;
    this.participants = participants;
    this.options = options;
    this.finalOption = finalOption;
  }

  public Meeting(String title, String description, User creator, String status, List<String> participants, List<Long> options, Option finalOption) {
    this.id = counterId++;
    this.title = title;
    this.description = description;
    this.creator = creator;
    this.status = status;
    this.participants = participants;
    this.options = options;
    this.finalOption = finalOption;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public List<Long> getOptions() {
    return options;
  }

  public void setOptions(List<Long> options) {
    this.options = options;
  }

  public Option getFinalOption() {
    return finalOption;
  }

  public void setFinalOption(Option finalOption) {
    this.finalOption = finalOption;
  }
}

