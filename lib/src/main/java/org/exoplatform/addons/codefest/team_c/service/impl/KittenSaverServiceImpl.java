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
import org.exoplatform.addons.codefest.team_c.notifications.KittenNotificationPlugin;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.calendar.service.CalendarSetting;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.PluginKey;
import org.exoplatform.commons.notification.impl.NotificationContextImpl;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Singleton
public class KittenSaverServiceImpl implements KittenSaverService {

  private static final Log LOG = ExoLogger.getExoLogger(KittenSaverServiceImpl.class);

  private final String KEY_PREFIX = "Timezone_Setting";

  @Inject
  private KittenSaviorDAO kittenSaviorDAO;

  @Inject
  private CalendarService calendarService;

  public KittenSaverServiceImpl()
  {
    this.kittenSaviorDAO = (KittenSaviorDAO) PortalContainer.getInstance().getComponentInstance(KittenSaviorDAO.class);
    this.calendarService = (CalendarService) PortalContainer.getInstance().getComponentInstance(CalendarService.class);
  }

  @Override
  public Meeting getMeeting(Long id) {
    return kittenSaviorDAO.getMeetingById(id);
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    Meeting meetingNew = kittenSaviorDAO.createMeeting(meeting);
    createNotification(meeting);
    return meetingNew;
  }

  @Override
  public void setUserTimezone(String username, String timezone) {
    User user = getUserByUsername(username);
    user.setTimezone(timezone);
    try {
      CalendarSetting calSettings = calendarService.getCalendarSetting(username);
      calSettings.setTimeZone(timezone);
      calendarService.saveCalendarSetting(username, calSettings);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    kittenSaviorDAO.updateUser(user);
  }

  @Override
  public Meeting validateMeeting(Meeting meeting) {
    Meeting meetingValidated = updateMeeting(meeting);
    createCalendarEvent(meeting);
    createNotification(meeting);
    return meetingValidated;
  }

  private void createNotification(Meeting meeting) {
    NotificationContext context = NotificationContextImpl.cloneInstance().append(KittenNotificationPlugin.MEETING, meeting);
    context.getNotificationExecutor().with(context.makeCommand(PluginKey.key(KittenNotificationPlugin.ID))).execute(context);
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
      Option o = meeting.getFinalOption();
      event.setFromDateTime(o.getStartDate());
      event.setToDateTime(o.getEndDate());
      try {
        int calType = calendarService.getTypeOfCalendar(participant, calId);
        switch (calType) {
          case Calendar.TYPE_PRIVATE:
              calendarService.saveUserEvent(participant, calId, event, true);
            break;
          case Calendar.TYPE_PUBLIC:
            calendarService.savePublicEvent(calId, event, true);
            break;
        }
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
  public void addOptionToMeeting(Long meetingId, Option option) {
    kittenSaviorDAO.addOptionToMeetingById(meetingId, option);
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
    if (user == null) {
      // User doesn't exist in our map so we create it here... olé
      user = new User();
      user.setName(username);
      user.setTimezone("GMT-0");
      user.setFirstName(username);
      user.setLastName(username);
      kittenSaviorDAO.createUser(user);
    }
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
  public String getUserTimezone(User user) {
    return getUserTimezone(user.getName());
  }

  @Override
  public String getUserTimezone(String username) {
    try {
      CalendarSetting calSettings = calendarService.getCalendarSetting(username);
      String tz = calSettings.getTimeZone();
      if (tz != null) return tz;
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
    return getUserByUsername(username).getTimezone();
  }
}
