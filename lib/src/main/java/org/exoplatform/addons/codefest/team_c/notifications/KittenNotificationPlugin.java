package org.exoplatform.addons.codefest.team_c.notifications;
import org.exoplatform.addons.codefest.team_c.domain.Meeting;
import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.model.ArgumentLiteral;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.plugin.BaseNotificationPlugin;
import org.exoplatform.container.xml.InitParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

//import java.util.HashSet;
//import java.util.Set;
//import org.exoplatform.commons.utils.CommonsUtils;
//import org.exoplatform.commons.utils.ListAccess;
//import org.exoplatform.services.log.ExoLogger;
//import org.exoplatform.services.log.Log;
//import org.exoplatform.social.core.identity.model.Identity;
//import org.exoplatform.social.core.identity.model.Meeting;
//import org.exoplatform.social.core.manager.RelationshipManager;

//This class extends BaseNotificationPlugin to push new notification type of Meeting updating event
public class KittenNotificationPlugin extends BaseNotificationPlugin {
    public final static ArgumentLiteral<Meeting> MEETING = new ArgumentLiteral<Meeting>(Meeting.class, "meeting");
    //private static final Log LOG = ExoLogger.getLogger(KittenNotificationPlugin.class);
    public final static String ID = "KittenNotificationPlugin";
    public KittenNotificationPlugin(InitParams initParams) {
        super(initParams);
    }
    @Override
    public String getId() {
        return ID;
    }
    @Override
    public boolean isValid(NotificationContext ctx) {
        return true;
    }
    @Override
    protected NotificationInfo makeNotification(NotificationContext ctx) {
        Meeting meeting = ctx.value(MEETING);

      if (meeting == null) {
        return NotificationInfo.instance()
            .setFrom("root")
            .setDateCreated(new GregorianCalendar())
            .to(new ArrayList<String>(Arrays.asList("root")))
            .setTitle(" root invited you to vote for a meeting: <a href='/portal/intranet/Kitten'>" + meeting.getTitle() + "</a><br/>")
            .key(getId());
      }

        List<String> participants = meeting.getParticipants();

        if (meeting.getFinalOption() == null) {
        	return NotificationInfo.instance()
        			.setFrom("root")
              .setDateCreated(new GregorianCalendar())
        			.to(new ArrayList<String>(participants))
        			.setTitle(" root invited you to vote for a meeting: <a href='/portal/intranet/Kitten'>" + meeting.getTitle()+ "</a><br/>")
        			.key(getId());
        } else {
        	return NotificationInfo.instance()
        			.setFrom("root")
              .setDateCreated(new GregorianCalendar())
        			.to(new ArrayList<String>(participants))
        			.setTitle("root" + " validated the meeting <a href='/portal/intranet/Kitten'>" + meeting.getTitle() + "</a><br/>")
        			.key(getId());
        }
    }
}