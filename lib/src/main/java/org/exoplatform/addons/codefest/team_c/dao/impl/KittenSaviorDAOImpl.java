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
package org.exoplatform.addons.codefest.team_c.dao.impl;

import org.exoplatform.addons.codefest.team_c.dao.KittenSaviorDAO;
import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaviorDAOImpl implements KittenSaviorDAO {

  private Map<Long, Meeting> meetings;
  private Map<Long, Option> options;
  private Map<Long, Choice> choices;
  private Map<String, User> users;

  public KittenSaviorDAOImpl() {
    meetings = new HashMap<Long, Meeting>();
    options = new HashMap<Long, Option>();
    choices = new HashMap<Long, Choice>();
    users = new HashMap<String , User>();
  }

  //////////////////////////
  //
  // MEETING
  //
  //////////////////////////

  @Override
  public Meeting getMeetingById(Long id) {
    return meetings.get(id);
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    return meetings.put(meeting.getId(), meeting);
  }

  @Override
  public Meeting updateMeeting(Meeting meeting) {
    return meetings.put(meeting.getId(), meeting);
  }

  @Override
  public void deleteMeeting(Meeting meeting) {
    meetings.remove(meeting.getId());
  }

  @Override
  public List<Meeting> getMeetingByUser(User user) {
    List<Meeting> userMeetings = new ArrayList<Meeting>();
    for (Long meetingId: this.meetings.keySet()) {
      if (meetings.get(meetingId).getParticipants().contains(user.getName())) {
        userMeetings.add(meetings.get(meetingId));
      }
    }
    return userMeetings;
  }

  @Override
  public void addOptionsToMeeting(Meeting meeting, Option option) {
    meetings.get(meeting.getId()).getOptions().add(option.getId());
  }

  //////////////////////////
  //
  // USER
  //
  //////////////////////////

  @Override
  public User getUserById(Long id) {
    return users.get(id);
  }

  @Override
  public User createUser(User user) {
    return users.put(user.getName(), user);
  }

  @Override
  public User updateUser(User user) {
    return createUser(user);
  }

  @Override
  public void deleteUser(User user) {
    users.remove(user.getName());
  }

  @Override
  public User getUserByUsername(String username) {
    return users.get(username);
  }

  @Override
  public String getTimezoneByUser(String username) {
    return users.get(username).getTimezone();
  }

  @Override
  public List<User> getParticipantsByMeetingId(Long meetingId) {
    List<User> participants = new ArrayList<User>();
    for (String userId : meetings.get(meetingId).getParticipants()) {
      participants.add(users.get(userId));
    }
    return participants;
  }

  //////////////////////////
  //
  // OPTION
  //
  //////////////////////////

  @Override
  public Option getOptionById(Long id) {
    return options.get(id);
  }

  @Override
  public Option createOption(Option option) {
    return options.put(option.getId(), option);
  }

  @Override
  public Option updateOption(Option option) {
    return options.put(option.getId(), option);
  }

  @Override
  public void deleteOption(Option option) {
    options.remove(option.getId());
  }

  @Override
  public List<Option> getOptionByMeetingId(Long meetingId) {
    List<Option> meetingOptions = new ArrayList<Option>();
    for (Long optionId : meetings.get(meetingId).getOptions()) {
      meetingOptions.add(options.get(optionId));
    }
    return meetingOptions;
  }

  @Override
  public void addChoiceToOption(Option option, Choice choice) {
    options.get(option.getId()).getChoices().add(choice.getId());
  }

  @Override
  public void addChoiceToOptionById(Long optionId, Choice choice) {
    addChoiceToOption(options.get(optionId), choice);
  }

  //////////////////////////
  //
  // CHOICE
  //
  //////////////////////////

  @Override
  public Choice getChoiceById(Long id) {
    return choices.get(id);
  }

  @Override
  public Choice createChoice(Choice choice) {
    return choices.put(choice.getId(), choice);
  }

  @Override
  public Choice updateChoice(Choice choice) {
    return choices.put(choice.getId(), choice);
  }

  @Override
  public void deleteChoice(Choice choice) {
    choices.remove(choice.getId());
  }

}

