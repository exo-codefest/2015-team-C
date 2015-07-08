package org.exoplatform.addons.codefest.team_c.service.test;

import org.exoplatform.addons.codefest.team_c.domain.Choice;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.addons.codefest.team_c.domain.Option;
import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by paristote on 7/8/15.
 */
@Path("/kittenTest")
public class TestKittenService implements ResourceContainer {

    private static final Log LOG = ExoLogger.getExoLogger(TestKittenService.class);

    User root = new User("root", "GMT+1"); // Paris
    User john = new User("john", "GMT+7"); // Hanoi

    @Inject
    private KittenSaverService kittenService;


    public TestKittenService(KittenSaverService service) {
        this.kittenService = service;
    }

    private boolean assertTrue(boolean test, String message) {
        if (test) {
            LOG.info("SUCCESS : " + message);
            return true;
        } else {
            LOG.error("FAILURE : " + message);
            return false;
        }
    }

    @GET
    @Path("/createMeeting")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testCreateMeeting() throws ParseException {
        Choice rootYes = new Choice("root", true);
        Choice johnNo = new Choice("john", false);
        Choice johnYes = new Choice("john", true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date d1 = sdf.parse("09/07/2015 10:00");
        Date d2 = sdf.parse("09/07/2015 11:00");
        Date d3 = sdf.parse("09/07/2015 12:00");
        Date d4 = sdf.parse("09/07/2015 13:00");

        Option withRootNoJohn = new Option(new ArrayList<Long>(Arrays.asList(rootYes.getId(), johnNo.getId())), d1, d2);
        Option withRootAndJohn = new Option(new ArrayList<Long>(Arrays.asList(rootYes.getId(), johnYes.getId())), d3, d4);

        Meeting meeting1 = new Meeting();
        meeting1.setCreator(root);
        meeting1.setTitle("Kitten Help");
        meeting1.setDescription("Help Kitten to do not died because of no choice");
        meeting1.setStatus(Meeting.STATUS_OPENED);
        meeting1.setOptions(new ArrayList<Long>(Arrays.asList(withRootNoJohn.getId(), withRootAndJohn.getId())));
        meeting1.setParticipants(new ArrayList<String>(Arrays.asList(root.getName(), john.getName())));

        kittenService.createMeeting(meeting1);

        //-------
        Long id = meeting1.getId();
        Meeting m = kittenService.getMeeting(id);
        if (!assertTrue(m != null, "Meeting "+id+" should exist")) {
            return Response.serverError().entity("Could not create meeting").build();
        }
        //-------

        return Response.ok(meeting1).build();
    }

    @GET
    @Path("/closeMeeting/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testCloseMeeting(@PathParam("id") String id) {
        Meeting meeting = kittenService.getMeeting(Long.valueOf(id));

        List<Option> options = kittenService.getOptionByMeeting(Long.valueOf(id));

        //-------
        if (!assertTrue(options != null && options.size() == 2, "Meeting should have 2 options")) {
            return Response.serverError().entity("Could not retrieve meeting").build();
        }
        //-------

        meeting.setStatus(Meeting.STATUS_CLOSED);
        meeting.setFinalOption(options.get(0));

        kittenService.validateMeeting(meeting);

        return Response.ok(meeting).build();
    }

}
