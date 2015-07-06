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

import org.exoplatform.addons.codefest.team_c.dao.MeetingDAO;
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
public class MeetingDAOImpl implements MeetingDAO {

  private Map<Long, Meeting> meetings;

  public MeetingDAOImpl() {
    meetings = new HashMap<Long, Meeting>();
  }

  @Override
  public Meeting createMeeting(Meeting meeting) {
    Long id = Meeting.counterId++;
    meeting.setId(id);
    return meetings.put(id, meeting);
  }

  @Override
  public Meeting updateMeeting(Meeting meeting) {
    return meetings.put(meeting.getId(), meeting);
  }

  @Override
  public void deleteMeeting(Long id) {
    meetings.remove(id);
  }

  @Override
  public List<Meeting> getMeetingByUser(User user) {
    List<Meeting> userMeetings = new ArrayList<Meeting>();
    for (Long meetingId: this.meetings.keySet()) {
      if (meetings.get(meetingId).getParticipants().contains(user)) {
        userMeetings.add(meetings.get(meetingId));
      }
    }
    return userMeetings;
  }

  @Override
  public List<User> getParticipantsByMeeting(Long meetingId) {
    return meetings.get(meetingId).getParticipants();
  }

  @Override
  public Map<Long, Option> getOptionByMeeting(Long meetingId) {
    return meetings.get(meetingId).getOptions();
  }

  @Override
  public void addChoiceToMeeting(Long meetingId, Long optionId, Choice choice) {
    meetings.get(meetingId).getOptions().get(optionId).getChoices().put(choice.getId(), choice);
  }

}

