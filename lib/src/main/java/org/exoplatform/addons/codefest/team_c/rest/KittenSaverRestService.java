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
package org.exoplatform.addons.codefest.team_c.rest;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 7/6/15
 */
@Path("/kittenSavior")
public class KittenSaverRestService implements ResourceContainer {

  private static final Log LOG = ExoLogger.getExoLogger(KittenSaverRestService.class);

  @Inject
  KittenSaverService kittenSaverService;

  public KittenSaverRestService(KittenSaverService kittenService) {
    this.kittenSaverService = kittenService;
  }

  @GET
  @Path("/meetings")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getMeetings(@QueryParam("user") String username) {
    if (username == null || username.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Missing 'user' query parameter.").build();
    }
    List<Meeting> meetings = null;
    try {

      User u = kittenSaverService.getUserByUsername(username);
      meetings = kittenSaverService.getMeetingByUser(u);

    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return Response.serverError().entity(e.getMessage()).build();
    }

    if (meetings == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("No meeting for this user.").build();
    }

    JSONObject resp = null;
    try {
      resp = new JSONObject();
      JSONArray meetingsArray = new JSONArray();
      for (Meeting m : meetings) {
        JSONObject meeting = new JSONObject();
        // Meeting
        meeting.put("id", m.getId());
        meeting.put("name", m.getTitle());
        meeting.put("description", m.getDescription());
        meeting.put("creator", m.getCreator().getName());
        meeting.put("status", m.getStatus());
        // Participants
        JSONArray participants = new JSONArray();
        for (User user : kittenSaverService.getParticipantsByMeeting(m.getId())) {
          participants.put(user.getName());
        }
        meeting.put("participants", participants);
        // Options
        JSONArray options = new JSONArray();
        for (Option opt : kittenSaverService.getOptionByMeeting(m.getId())) {
          JSONObject option = new JSONObject();
          option.put("id", opt.getId());
          option.put("start_timestamp", opt.getStartDate().getTime());
          option.put("end_timestamp", opt.getEndDate().getTime());
          options.put(option);
        }
        if (m.getFinalOption() != null) {
          Option f = m.getFinalOption();
          JSONObject finalOpt = new JSONObject();
          finalOpt.put("id", f.getId());
          finalOpt.put("start_timestamp", f.getStartDate().getTime());
          finalOpt.put("end_timestamp", f.getEndDate().getTime());
          meeting.put("final_option", finalOpt);
        }
        meeting.put("options", options);
        // Finish
        meetingsArray.put(meeting);
      }
      resp.put("meetings", meetingsArray);
    } catch (JSONException e) {
      LOG.error(e.getMessage(), e);
      return Response.serverError().entity(e.getMessage()).build();
    }

    return Response.ok(resp.toString(), MediaType.APPLICATION_JSON).build();
  }


  @GET
  @Path("/meetings/{meetingId}/choices")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getChoices(@PathParam("meetingId") String meetingId) {

    if (meetingId == null || meetingId.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Missing parameter 'meetingId'.").build();
    }

    Meeting meeting = kittenSaverService.getMeeting(Long.valueOf(meetingId));

    JSONObject resp = new JSONObject();
    JSONArray choices = new JSONArray();

    try {

      for (Option option : kittenSaverService.getOptionByMeeting(meeting.getId())) {

        for (Choice choice : kittenSaverService.getChoicesByOption(option.getId())) {

          JSONObject c = new JSONObject();
          c.put("time_id", option.getId());
          c.put("user", choice.getParticipant());
          c.put("choice", choice.getChoice());

          choices.put(c);
        }

      }

      resp.put("choices", choices);
    } catch (JSONException e) {
      LOG.error(e.getMessage(), e);
      return Response.serverError().entity(e.getMessage()).build();
    }

    return Response.ok(resp.toString(), MediaType.APPLICATION_JSON).build();
  }


  @POST
  @Path("/meetings/{meetingId}/options/{optionId}/choices")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response makeChoice(@PathParam("meetingId") String meetingId, @PathParam("optionId") String optionId, Choice choice) {

    if (meetingId == null || meetingId.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Missing parameter 'meetingId'.").build();
    }

    if (optionId == null || optionId.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Missing parameter 'optionId'.").build();
    }

    JSONObject resp = new JSONObject();
    try {
      kittenSaverService.addChoiceToMeeting(Long.valueOf(meetingId), Long.valueOf(optionId), choice);
      resp.put("result", "ok");
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      return Response.serverError().entity(e.getMessage()).build();
    }

    return Response.ok(resp.toString(), MediaType.APPLICATION_JSON).build();
  }

  @GET
  @Path("/users/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUserByUsername(@PathParam("username") String username) {

    if (username == null || username.trim().isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Missing parameter 'username'.").build();
    }

    User u = kittenSaverService.getUserByUsername(username);

    if (u == null) {
      return Response.status(Response.Status.NOT_FOUND).entity("No user by this username.").build();
    }

    JSONObject resp = new JSONObject();
    try {
      resp.put("username", username);
      resp.put("first_name", u.getFirstName());
      resp.put("last_name", u.getLastName());
      resp.put("timezone", u.getTimezone());
    } catch (JSONException e) {
      LOG.error(e.getMessage(), e);
      return Response.serverError().entity(e.getMessage()).build();
    }
    return Response.ok(resp.toString(), MediaType.APPLICATION_JSON).build();
  }

}

