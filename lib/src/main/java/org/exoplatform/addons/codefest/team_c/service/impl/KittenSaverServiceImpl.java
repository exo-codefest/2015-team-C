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

import org.exoplatform.addons.codefest.team_c.dao.KittenSaviorDAO;
import org.exoplatform.addons.codefest.team_c.dao.impl.KittenSaviorDAOImpl;
import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.addons.codefest.team_c.service.KittenSettingsService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaverServiceImpl implements KittenSaverService {

  @Inject
  private KittenSaviorDAO kittenSaviorDAO;

  private KittenSettingsService settingsService;

  public KittenSaverServiceImpl(KittenSettingsService settings)
  {
    this.settingsService = settings;
    this.kittenSaviorDAO = new KittenSaviorDAOImpl();
  }

  @Override
  public Meeting getMeeting(Long id) {
    return kittenSaviorDAO.getMeetingById(id);
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    return kittenSaviorDAO.createMeeting(meeting);
  }

  @Override
  public Meeting updateMeeting(Meeting meeting) {
    return kittenSaviorDAO.updateMeeting(meeting);
  }

  @Override
  public List<Meeting> getMeetingByUser(User user) {
    return kittenSaviorDAO.getMeetingByUser(user);
  }

  @Override
  public List<Meeting> getMeetingByUserId(String userId) {
    return getMeetingByUser(getUserByUsername(userId));
  }

  @Override
  public List<User> getParticipantsByMeeting(Long meetingId) {
    return kittenSaviorDAO.getParticipantsByMeetingId(meetingId);
  }

  @Override
  public List<Option> getOptionByMeeting(Long meetingId) {
    return kittenSaviorDAO.getOptionByMeetingId(meetingId);
  }

  @Override
  public void addChoiceToMeeting(Long meetingId, Long optionId, Choice choice) {
    kittenSaviorDAO.addChoiceToOptionById(optionId, choice);
  }

  @Override
  public List<Choice> getChoicesByOption(Long optionId) {
    return null;
  }

  @Override
  public User getUserByUsername(String username) {
    User user = kittenSaviorDAO.getUserByUsername(username);
    if (user.getTimezone() == null) {
      user.setTimezone(settingsService.getUserTimezone(user));
    }
    return user;
  }
}

