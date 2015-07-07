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

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
public class Choice {

  public static Long counterId = 0l;

  private Long id;
  private String participant;
  Boolean choice;

  public Choice() {
    this.id = counterId++;
  }

  public Choice(Long id, String participant, Boolean choice) {
    this.id = id;
    this.participant = participant;
    this.choice = choice;
  }

  public Choice(String participant, Boolean choice) {
    this.id = counterId++;
    this.participant = participant;
    this.choice = choice;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getParticipant() {
    return participant;
  }

  public void setParticipant(String participant) {
    this.participant = participant;
  }

  public Boolean getChoice() {
    return choice;
  }

  public void setChoice(Boolean choice) {
    this.choice = choice;
  }
}

