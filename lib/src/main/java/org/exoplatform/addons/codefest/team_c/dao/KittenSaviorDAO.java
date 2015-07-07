package org.exoplatform.addons.codefest.team_c.dao;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;

import java.util.List;

/**
 * Created by TClement on 7/6/15.
 */
public interface KittenSaviorDAO {

  Meeting getMeetingById(Long id);

  Meeting createMeeting(Meeting meeting);

  Meeting updateMeeting(Meeting meeting);

  void deleteMeeting(Meeting meeting);

  List<Meeting> getMeetingByUser(User user);

  List<User> getParticipantsByMeetingId(Long meetingId);

  List<Option> getOptionByMeetingId(Long meetingId);

  void addOptionsToMeeting(Meeting meeting, Option option);

  User getUserById(Long id);

  User createUser(User user);

  User updateUser(User user);

  void deleteUser(User user);

  User getUserByUsername(String username);

  String getTimezoneByUser(String username);

  Option getOptionById(Long id);

  Option createOption(Option option);

  Option updateOption(Option option);

  void deleteOption(Option option);

  void addChoiceToOption(Option option, Choice choice);

  void addChoiceToOptionById(Long optionId, Choice choice);

  Choice getChoiceById(Long id);

  Choice createChoice(Choice choice);

  Choice updateChoice(Choice choice);

  void deleteChoice(Choice choice);
}
