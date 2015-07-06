package org.exoplatform.addons.codefest.team_c.service;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by TClement on 7/6/15.
 */
public interface KittenSaverService {
  Meeting createMeeting(Meeting meeting);

  Meeting updateMeeting(Meeting meeting);

  void deleteMeeting(Long id);

  List<Meeting> getMeetingByUser(User user);

  List<User> getParticipantsByMeeting(Long meetingId);

  Map<Long, Option> getOptionByMeeting(Long meetingId);

  void addChoiceToMeeting(Long meetingId, Long optionId, Choice choice);
}
