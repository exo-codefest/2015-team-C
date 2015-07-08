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
package org.exoplatform.addons.codefest.team_c.controller;

import juzu.Action;
import juzu.Path;
import juzu.Response;
import juzu.View;
import juzu.impl.common.Tools;
import juzu.request.SecurityContext;
import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.model.MeetingInfos;
import org.exoplatform.addons.codefest.team_c.model.UserChoice;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
public class KittenSaverController {

  private static final Log LOG = ExoLogger.getExoLogger(KittenSaverController.class);

  @Inject
  KittenSaverService kittenSaverService;

  @Inject
  @Path("index.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.index index;

  @Inject
  @Path("add.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.add add;

  @Inject
  @Path("choose.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.choose choose;

  @Inject
  @Path("config.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.config config;

  @Inject
  @Path("validate.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.validate validate;

  @View
  public Response.Content index(SecurityContext securityContext) throws IOException {

    LOG.info("###### getMeetingByUserId = "+securityContext.getRemoteUser());

    List<MeetingInfos> meetingInfoses = new ArrayList<MeetingInfos>();

    List<Meeting> meetings = kittenSaverService.getMeetingByUserId(securityContext.getRemoteUser());
    for (Meeting meeting : meetings) {
      meetingInfoses.add(new MeetingInfos(meeting, kittenSaverService.getOptionByMeeting(meeting.getId())));
    }

    String tz = kittenSaverService.getUserTimezone(securityContext.getRemoteUser());
    String timzone = TimeZone.getTimeZone(tz).getDisplayName();

    return index
        .with()
        .meetingsCount(meetings.size())
        .meetings(meetingInfoses)
        .timzone(timzone)
        .user(securityContext.getRemoteUser())
        .ok()
        .withCharset(Tools.UTF_8);
  }

  @View
  public Response.Content addView() {
    return add.ok();
  }

  @View
  public Response.Content configView() {
    return config.ok();
  }

  @View
  public Response.Content chooseView(String meetingid, String username) {

    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.valueOf(meetingid));

    return choose
        .with()
        .meeting(meeting)
        .options(options)
        .user(username)
        .ok();
  }

  @View
  public Response.Content validateView(String meetingid) {

    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.valueOf(meetingid));

    List<UserChoice> userChoices = new ArrayList<UserChoice>();
    for (String username : meeting.getParticipants()) {
      for (Option optionUser : options) {
        List<Choice> choicesUser = new ArrayList<Choice>();
        for (Choice choiceall : kittenSaverService.getChoicesByOption(optionUser.getId())) {
          if (choiceall.getParticipant() == username) choicesUser.add(choiceall);
        }
        userChoices.add(new UserChoice(username, choicesUser));
      }
    }

    return validate
        .with()
        .meeting(meeting)
        .options(options)
        .usersChoice(userChoices)
        .ok();
  }

  @Action
  public Response.View validateAction(String meetingid, SecurityContext securityContext) {

    /*Meeting meeting = kittenSaverService.getMeeting(Long.getLong(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.getLong(meetingid));

    List<UserChoice> userChoices = new ArrayList<UserChoice>();
    for (String username : meeting.getParticipants()) {
      for (Option optionUser : options) {
        List<Choice> choicesUser = new ArrayList<Choice>();
        for (Choice choiceall : kittenSaverService.getChoicesByOption(optionUser.getId())) {
          if (choiceall.getParticipant() == username) choicesUser.add(choiceall);
        }
        userChoices.add(new UserChoice(username, choicesUser));
      }
    }*/

    return KittenSaverController_.validateView(meetingid);
  }

  @Action
  public Response.View chooseAction(String meetingid, SecurityContext securityContext) {

    /*Meeting meeting = kittenSaverService.getMeeting(Long.getLong(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.getLong(meetingid));

    List<UserChoice> userChoices = new ArrayList<UserChoice>();
    for (String username : meeting.getParticipants()) {
      for (Option optionUser : options) {
        List<Choice> choicesUser = new ArrayList<Choice>();
        for (Choice choiceall : kittenSaverService.getChoicesByOption(optionUser.getId())) {
          if (choiceall.getParticipant() == username) choicesUser.add(choiceall);
        }
        userChoices.add(new UserChoice(username, choicesUser));
      }
    }*/

    return KittenSaverController_.chooseView(meetingid, securityContext.getRemoteUser());
  }

  @juzu.Action
  public Response.View updateTimezone(String timezone, SecurityContext securityContext) {
    LOG.info("Modify Timezone to "+timezone);
    kittenSaverService.setUserTimezone(securityContext.getRemoteUser(), timezone);
    return KittenSaverController_.index();
  }

  @Action
  public Response.View validateMeeting (String optionid, String meetingid) {
    LOG.info("Validate meeting "+meetingid);
    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingid));
    Option option = kittenSaverService.getOption(Long.valueOf(optionid));
    meeting.setFinalOption(option);
    kittenSaverService.validateMeeting(meeting);
    return KittenSaverController_.index();
  }

}

