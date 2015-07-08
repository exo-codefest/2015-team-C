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

import javax.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import juzu.Action;
import juzu.MimeType;
import juzu.Path;
import juzu.Resource;
import juzu.Response;
import juzu.View;
import juzu.impl.common.Tools;
import juzu.plugin.ajax.Ajax;
import juzu.request.SecurityContext;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.model.MeetingInfos;
import org.exoplatform.addons.codefest.team_c.model.UserChoice;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONException;
import org.json.JSONObject;

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
  @Path("timer.gtmpl")
  org.exoplatform.addons.codefest.team_c.templates.timer timer;

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
  public Response.Content addView(SecurityContext securityContext) {    
    List<User> users = new LinkedList<User>();
    User creator = kittenSaverService.getUserByUsername(securityContext.getRemoteUser());
    users.add(creator);
    return add.with().users(users).ok();
  }
  
  @Resource
  @Ajax
  @MimeType.HTML  
  public Response addUser(String username, Long start, Long end) {
    User user = kittenSaverService.getUserByUsername(username);
    if (user == null) {
      return Response.notFound();
    } else {
      return timer.with().u(user).start(start).end(end)
          .ok().withCharset(Tools.UTF_8);
    }
  }
  
  @Resource
  @Ajax
  @MimeType.JSON
  public Response addOption(Long start, Long end, SecurityContext securityContext) throws JSONException {
    User user = kittenSaverService.getUserByUsername(securityContext.getRemoteUser());
    TimeZone timezone = TimeZone.getTimeZone(user.getTimezone());
    Calendar sCal = Calendar.getInstance(timezone);
    sCal.setTimeInMillis(start);
    Calendar eCal = Calendar.getInstance(timezone);
    eCal.setTimeInMillis(end);

    JSONObject json = new JSONObject();
    json.put("start", format(sCal));
    json.put("end", format(eCal));
    
    return Response.ok(json.toString()).withCharset(Tools.UTF_8);
  }
  
  private String format(Calendar time) {
    StringBuilder builder = new StringBuilder();
    builder.append(time.get(Calendar.YEAR)).append("/");
    builder.append(time.get(Calendar.MONTH)).append("/");
    builder.append(time.get(Calendar.DATE)).append("  ");
    builder.append(time.get(Calendar.HOUR)).append(":");
    builder.append(time.get(Calendar.MINUTE));
    return builder.toString();
  }

  @Resource
  @Ajax
  @MimeType.JSON
  public Response addMeeting(String title, String description, String participants, String options, SecurityContext securityContext) throws JSONException {
    List<String> pars = new LinkedList<String>(Arrays.asList(participants.split(",")));
    List<Long> opts = new LinkedList<Long>();
    for (String opt : options.split(",")) {
      String[] time = opt.split("/");
      Date startDate = new Date();
      startDate.setTime(Long.parseLong(time[0]));
      Date endDate = new Date();
      endDate.setTime(Long.parseLong(time[1]));
      Option o = new Option(new LinkedList<Long>(), startDate, endDate);
      kittenSaverService.createOption(o);
      opts.add(o.getId());
    }    
    
    User user = kittenSaverService.getUserByUsername(securityContext.getRemoteUser());
    Meeting meeting = new Meeting(title, description, user, Meeting.STATUS_OPENED, pars, opts, null);
    kittenSaverService.createMeeting(meeting);
    
    JSONObject json = new JSONObject();
    json.put("url", KittenSaverController_.index());
    return Response.ok(json.toString()).withCharset(Tools.UTF_8);
  }

  @View
  public Response.Content configView() {
    return config.ok();
  }

  @View
  public Response.Content chooseView(String meetingid, String username) {

    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.valueOf(meetingid));

    String tz = kittenSaverService.getUserTimezone(username);
    String timzone = TimeZone.getTimeZone(tz).getDisplayName();

    return choose
        .with()
        .meeting(meeting)
        .options(options)
        .timzone(timzone)
        .user(username)
        .ok();
  }

  @View
  public Response.Content validateView(String meetingid, String user) {

    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingid));
    List<Option> options = kittenSaverService.getOptionByMeeting(Long.valueOf(meetingid));

    String tz = kittenSaverService.getUserTimezone(user);
    String timzone = TimeZone.getTimeZone(tz).getDisplayName();

    List<UserChoice> userChoices = new ArrayList<UserChoice>();
    for (String username : meeting.getParticipants()) {
      List<Choice> choicesUser = new ArrayList<Choice>();
      for (Option optionUser : options) {
        for (Choice choiceall : kittenSaverService.getChoicesByOption(optionUser.getId())) {
          if (choiceall.getParticipant() == username) choicesUser.add(choiceall);
        }
      }
      userChoices.add(new UserChoice(username, choicesUser));
    }

    return validate
        .with()
        .meeting(meeting)
        .timzone(timzone)
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

    return KittenSaverController_.validateView(meetingid, securityContext.getRemoteUser());
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

