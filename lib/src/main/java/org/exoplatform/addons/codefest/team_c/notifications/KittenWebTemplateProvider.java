package org.exoplatform.addons.codefest.team_c.notifications;

import java.io.Writer;
import java.util.Calendar;
import java.util.Locale;

import org.exoplatform.commons.api.notification.NotificationContext;
import org.exoplatform.commons.api.notification.NotificationMessageUtils;
import org.exoplatform.commons.api.notification.channel.template.AbstractTemplateBuilder;
import org.exoplatform.commons.api.notification.model.MessageInfo;
import org.exoplatform.commons.api.notification.model.NotificationInfo;
import org.exoplatform.commons.api.notification.model.PluginKey;
//import org.exoplatform.commons.api.notification.plugin.NotificationPluginUtils;
import org.exoplatform.commons.api.notification.service.template.TemplateContext;
import org.exoplatform.container.xml.InitParams;
//import org.exoplatform.social.core.identity.model.Identity;
//import org.exoplatform.social.core.identity.model.Profile;
//import org.exoplatform.social.core.identity.provider.OrganizationIdentityProvider;
//import org.exoplatform.social.core.service.LinkProvider;
//import org.exoplatform.social.notification.Utils;
import org.exoplatform.social.notification.channel.template.WebTemplateProvider;
import org.exoplatform.commons.api.notification.annotation.TemplateConfig;
import org.exoplatform.commons.api.notification.annotation.TemplateConfigs;
import org.exoplatform.commons.notification.template.TemplateUtils;
import org.exoplatform.social.notification.plugin.ActivityCommentPlugin;
import org.exoplatform.social.notification.plugin.ActivityMentionPlugin;
import org.exoplatform.social.notification.plugin.LikePlugin;
import org.exoplatform.social.notification.plugin.NewUserPlugin;
import org.exoplatform.social.notification.plugin.PostActivityPlugin;
import org.exoplatform.social.notification.plugin.PostActivitySpaceStreamPlugin;
import org.exoplatform.social.notification.plugin.RelationshipReceivedRequestPlugin;
import org.exoplatform.social.notification.plugin.RequestJoinSpacePlugin;
//import org.exoplatform.social.notification.plugin.SocialNotificationUtils;
import org.exoplatform.social.notification.plugin.SpaceInvitationPlugin;
import org.exoplatform.webui.utils.TimeConvertUtils;

@TemplateConfigs (
		   templates = {
		       @TemplateConfig( pluginId=ActivityCommentPlugin.ID, template="war:/intranet-notification/templates/ActivityCommentPlugin.gtmpl"),
		       @TemplateConfig( pluginId=ActivityMentionPlugin.ID, template="war:/intranet-notification/templates/ActivityMentionPlugin.gtmpl"),
		       @TemplateConfig( pluginId=LikePlugin.ID, template="war:/intranet-notification/templates/LikePlugin.gtmpl"),
		       @TemplateConfig( pluginId=NewUserPlugin.ID, template="war:/intranet-notification/templates/NewUserPlugin.gtmpl"),
		       @TemplateConfig( pluginId=PostActivityPlugin.ID, template="war:/intranet-notification/templates/PostActivityPlugin.gtmpl"),
		       @TemplateConfig( pluginId=PostActivitySpaceStreamPlugin.ID, template="war:/intranet-notification/templates/PostActivitySpaceStreamPlugin.gtmpl"),
		       @TemplateConfig( pluginId=RelationshipReceivedRequestPlugin.ID, template="war:/intranet-notification/templates/RelationshipReceivedRequestPlugin.gtmpl"),
		       @TemplateConfig( pluginId=RequestJoinSpacePlugin.ID, template="war:/intranet-notification/templates/RequestJoinSpacePlugin.gtmpl"),
		       @TemplateConfig( pluginId=SpaceInvitationPlugin.ID, template="war:/intranet-notification/templates/SpaceInvitationPlugin.gtmpl"),
		       @TemplateConfig( pluginId=KittenNotificationPlugin.ID, template="war:/notification/templates/KittenNotificationPlugin.gtmpl")
		   }
		)
public class KittenWebTemplateProvider extends WebTemplateProvider {
	
	
	private AbstractTemplateBuilder kitten = new AbstractTemplateBuilder() {

		@Override
		protected MessageInfo makeMessage(NotificationContext ctx) {
			NotificationInfo notification = ctx.getNotificationInfo();

			String language = getLanguage(notification);
			TemplateContext templateContext = TemplateContext.newChannelInstance(getChannelKey(), notification.getKey().getId(), language);

			//String remoteId = notification.getValueOwnerParameter(SocialNotificationUtils.REMOTE_ID.getKey());
			//Identity identity = Utils.getIdentityManager().getOrCreateIdentity(OrganizationIdentityProvider.NAME, remoteId, true);
			//Profile userProfile = identity.getProfile();
			templateContext.put("isIntranet", "true");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(notification.getLastModifiedDate());
			templateContext.put("READ", Boolean.valueOf(notification.getValueOwnerParameter(NotificationMessageUtils.READ_PORPERTY.getKey())) ? "read" : "unread");
			templateContext.put("NOTIFICATION_ID", notification.getId());
			templateContext.put("LAST_UPDATED_TIME", TimeConvertUtils.convertXTimeAgoByTimeServer(cal.getTime(), "EE, dd yyyy", new Locale(language), TimeConvertUtils.YEAR));
			//templateContext.put("USER", userProfile.getFullName());
			//templateContext.put("PORTAL_NAME", NotificationPluginUtils.getBrandingPortalName());
			//templateContext.put("PROFILE_URL", LinkProvider.getUserProfileUri(identity.getRemoteId()));
			//templateContext.put("AVATAR", userProfile.getAvatarUrl() != null ? userProfile.getAvatarUrl() : LinkProvider.PROFILE_DEFAULT_AVATAR_URL);
			templateContext.put("TITLE", notification.getTitle());
			//
			String body = TemplateUtils.processGroovy(templateContext);
			//binding the exception throws by processing template
			ctx.setException(templateContext.getException());
			MessageInfo messageInfo = new MessageInfo();
			return messageInfo.body(body).end();
		}
		
		@Override
		protected boolean makeDigest(NotificationContext ctx, Writer writer) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	public KittenWebTemplateProvider(InitParams initParams) {
		super(initParams);
		// TODO Auto-generated constructor stub
		this.templateBuilders.put(PluginKey.key(KittenNotificationPlugin.ID), kitten);
	}
	
}
