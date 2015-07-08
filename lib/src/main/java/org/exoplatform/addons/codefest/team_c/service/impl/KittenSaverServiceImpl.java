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
import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaverServiceImpl implements KittenSaverService {

  private final String KEY_PREFIX = "Timezone_Setting";

  @Inject
  private SettingService settingService;

  @Inject
  private KittenSaviorDAO kittenSaviorDAO;

  @Inject
  private CalendarService calendarService;

  public KittenSaverServiceImpl()
  {
    this.kittenSaviorDAO = (KittenSaviorDAO) PortalContainer.getInstance().getComponentInstance(KittenSaviorDAO.class);
    this.settingService = (SettingService) PortalContainer.getInstance().getComponentInstance(SettingService.class);
    this.calendarService = (CalendarService) PortalContainer.getInstance().getComponentInstance(CalendarService.class);
  }

  @Override
  public Meeting getMeeting(Long id) {
    return kittenSaviorDAO.getMeetingById(id);
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    return kittenSaviorDAO.createMeeting(meeting);
  }

  private void createCalendarEvent(Meeting meeting) {
    for (String participant : meeting.getParticipants()) {
      // Retrieve default calendar of the participant
      String calId = getFirstCalendarId(participant);
      // Event
      CalendarEvent event = new CalendarEvent();
      event.setCalendarId(calId);
      event.setSummary(meeting.getTitle());
      event.setDescription(meeting.getDescription());
      event.setEventType(CalendarEvent.TYPE_EVENT);
      event.setRepeatType(CalendarEvent.RP_NOREPEAT);
      event.setPrivate(false);
      event.setPriority(CalendarEvent.PRIORITY_NORMAL);
      // Participants
      int nbPart = meeting.getParticipants().size();
      String[] participants = new String[nbPart];
      meeting.getParticipants().toArray(participants);
      event.setParticipant(participants);
      // Date and Time
      Option o = new Option();
      event.setFromDateTime(o.getStartDate());
      event.setToDateTime(o.getEndDate());
      try {
        calendarService.savePublicEvent(calId, event, true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String getFirstCalendarId(String username) {

    StringBuilder sb = new StringBuilder();
    List<Calendar> listUserCalendar = null;
    try {
      listUserCalendar = calendarService.getUserCalendars(username, true);
      if (listUserCalendar.size()>0) {
        return listUserCalendar.get(0).getId();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Meeting updateMeeting(Meeting meeting) {
    if (Meeting.STATUS_CLOSED.equals(meeting.getStatus()) &&
            meeting.getFinalOption() != null) {
      // Create the calendar only when we close the meeting with a final choice
      createCalendarEvent(meeting);
    }
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
    List<Choice> results = new ArrayList<Choice>();
    Option option = kittenSaviorDAO.getOptionById(optionId);
    for (Long id : option.getChoices()) {
      results.add(kittenSaviorDAO.getChoiceById(id));
    }
    return results;
  }

  @Override
  public User getUserByUsername(String username) {
    User user = kittenSaviorDAO.getUserByUsername(username);
    if (user.getTimezone() == null) {
      user.setTimezone(getUserTimezone(user));
    }
    return user;
  }

  private String key(String username) {
    if (username == null)
      throw new IllegalArgumentException("Cannot use null as a key in the settings service.");

    return String.format("%s_%s", KEY_PREFIX, username);
  }

  @Override
  public void setTimezoneForUser(String timezoneId, User user) {
    List<String> timezones = Arrays.asList(TimeZone.getAvailableIDs());
    if (timezones.contains(timezoneId)) {
      SettingValue<String> value = SettingValue.create(timezoneId);
      settingService.set(Context.USER, Scope.GLOBAL, key(user.getName()), value);
    } else {
      // Incorrect timezone
      throw new IllegalArgumentException(String.format("Incorrect timezone: %s is not a valid timezone ID.", timezoneId));
    }
  }

  @Override
  public void setTimezoneForUser(TimeZone timezone, User user) {

    if (timezone != null) {
      setTimezoneForUser(timezone.getID(), user);
    } else {
      // Incorrect timezone
      throw new IllegalArgumentException(String.format("Incorrect timezone: null is not a valid timezone."));
    }
  }

  @Override
  public String getUserTimezone(User user) {
    return getUserTimezone(user.getName());
  }

  @Override
  public String getUserTimezone(String username) {
    SettingValue<String> value = (SettingValue<String>)settingService.get(Context.USER, Scope.GLOBAL, key(username));
    return value == null ? null : value.getValue() ;
  }
}

