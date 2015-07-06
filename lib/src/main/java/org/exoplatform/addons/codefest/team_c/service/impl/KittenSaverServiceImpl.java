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
package org.exoplatform.addons.codefest.team_c.service.impl;

import org.exoplatform.addons.codefest.team_c.dao.MeetingDAO;
import org.exoplatform.addons.codefest.team_c.dao.impl.MeetingDAOImpl;
import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaverServiceImpl implements KittenSaverService {

  private MeetingDAO meetingDAO;

  public KittenSaverServiceImpl() {
    this.meetingDAO = new MeetingDAOImpl();
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    return meetingDAO.createMeeting(meeting);
  }

  @Override
  public Meeting updateMeeting(Meeting meeting) {
    return meetingDAO.updateMeeting(meeting);
  }

  @Override
  public void deleteMeeting(Long id) {
    meetingDAO.deleteMeeting(id);
  }

  @Override
  public List<Meeting> getMeetingByUser(User user) {
    return meetingDAO.getMeetingByUser(user);
  }

  @Override
  public List<User> getParticipantsByMeeting(Long meetingId) {
    return meetingDAO.getParticipantsByMeeting(meetingId);
  }

  @Override
  public Map<Long, Option> getOptionByMeeting(Long meetingId) {
    return meetingDAO.getOptionByMeeting(meetingId);
  }

  @Override
  public void addChoiceToMeeting(Long meetingId, Long optionId, Choice choice) {
    meetingDAO.addChoiceToMeeting(meetingId, optionId, choice);
  }

}

