package org.exoplatform.addons.codefest.team_c.service;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;

import java.util.List;
import java.util.TimeZone;

/**
 * Created by TClement on 7/6/15.
 */
public interface KittenSaverService {

  Meeting getMeeting(Long id);

  Meeting createMeeting(Meeting meeting);

  Meeting updateMeeting(Meeting meeting);

  List<Meeting> getMeetingByUserId(String userId);

  List<Meeting> getMeetingByUser(User user);

  List<User> getParticipantsByMeeting(Long meetingId);

  List<Option> getOptionByMeeting(Long meetingId);

  void addChoiceToMeeting(Long meetingId, Long optionId, Choice choice);

  List<Choice> getChoicesByOption(Long optionId);

  User getUserByUsername(String username);

  void setTimezoneForUser(String timezoneId, User user);

  void setTimezoneForUser(TimeZone timezone, User user);

  String getUserTimezone(User user);

  String getUserTimezone(String username);

}
