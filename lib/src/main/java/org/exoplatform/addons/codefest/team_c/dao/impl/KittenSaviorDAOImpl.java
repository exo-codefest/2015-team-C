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
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaviorDAOImpl implements KittenSaviorDAO {

  private static final Log LOG = ExoLogger.getExoLogger(KittenSaviorDAOImpl.class);

  private Map<Long, Meeting> meetings;
  private Map<Long, Option> options;
  private Map<Long, Choice> choices;
  private Map<String, User> users;

  public KittenSaviorDAOImpl() {
    meetings = new HashMap<Long, Meeting>();
    options = new HashMap<Long, Option>();
    choices = new HashMap<Long, Choice>();
    users = new HashMap<String , User>();
    initDatas();
  }

  private void initDatas() {

    try {

      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
      Date d1 = sdf.parse("09/07/2015 10:00");
      Date d2 = sdf.parse("09/07/2015 11:00");
      Date d3 = sdf.parse("09/07/2015 12:00");
      Date d4 = sdf.parse("09/07/2015 13:00");
      Date d5 = sdf.parse("09/07/2015 14:00");
      Date d6 = sdf.parse("09/07/2015 15:00");
      Date d7 = sdf.parse("09/07/2015 16:00");
      Date d8 = sdf.parse("09/07/2015 17:00");

      //User
      User root = new User("root", "Etc/GMT-1");
      User john = new User("john", "Etc/GMT-6");
      users.put(root.getName(), root);
      users.put(john.getName(), john);

      //Choice
      Choice rootYes = new Choice("root", true);
      Choice johnNo = new Choice("john", false);
      Choice rootNo = new Choice("root", false);
      Choice johnYes = new Choice("john", true);
      choices.put(rootYes.getId(), rootYes);
      choices.put(johnNo.getId(), johnNo);
      choices.put(rootNo.getId(), rootNo);
      choices.put(johnYes.getId(), johnYes);

      //Option
      Option withRootNoJohn = new Option(new ArrayList<Long>(Arrays.asList(rootYes.getId(), johnNo.getId())), d1, d2);
      Option withRootAndJohn = new Option(new ArrayList<Long>(Arrays.asList(rootYes.getId(), johnYes.getId())), d3, d4);
      Option withJohnNoRoot = new Option(new ArrayList<Long>(Arrays.asList(rootNo.getId(), johnYes.getId())), d4, d5);
      Option withNoRootNoJohn = new Option(new ArrayList<Long>(Arrays.asList(rootNo.getId(), johnNo.getId())), d5, d6);
      Option forBinch = new Option(new ArrayList<Long>(Arrays.asList(rootYes.getId(), johnYes.getId())), d6, d7);
      options.put(withRootNoJohn.getId(), withRootNoJohn);
      options.put(withRootAndJohn.getId(), withRootAndJohn);
      options.put(withJohnNoRoot.getId(), withJohnNoRoot);
      options.put(withNoRootNoJohn.getId(), withNoRootNoJohn);
      options.put(forBinch.getId(), forBinch);

      //Meeting
      Meeting meeting1 = new Meeting();
      meeting1.setCreator(root);
      meeting1.setTitle("Kitten Help");
      meeting1.setDescription("Help Kitten to do not died because of no choice");
      meeting1.setStatus("opened");
      meeting1.setOptions(new ArrayList<Long>(Arrays.asList(withRootNoJohn.getId(), withRootAndJohn.getId())));
      meeting1.setParticipants(new ArrayList<String>(Arrays.asList(root.getName(), john.getName())));

      Meeting meeting2 = new Meeting();
      meeting2.setCreator(john);
      meeting2.setTitle("Bia Hoi tonight");
      meeting2.setDescription("After hardwork let's take a binch");
      meeting2.setStatus("closed");
      meeting2.setFinalOption(forBinch);
      meeting2.setOptions(new ArrayList<Long>(Arrays.asList(withJohnNoRoot.getId(), withNoRootNoJohn.getId(), forBinch.getId())));
      meeting2.setParticipants(new ArrayList<String>(Arrays.asList(root.getName(), john.getName())));

      meetings.put(meeting1.getId(), meeting1);
      meetings.put(meeting2.getId(), meeting2);

    } catch (ParseException e) {
      e.printStackTrace();
    }
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
      LOG.info("Meeting title = "+meetings.get(meetingId).getTitle());
      LOG.info("Meeting participants = ");
      for (String participant : meetings.get(meetingId).getParticipants()) LOG.info(participant);
      LOG.info("User = "+user);
      LOG.info("Username = "+user.getName());
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

