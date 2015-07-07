package org.exoplatform.addons.codefest.team_c.service;

import org.exoplatform.addons.codefest.team_c.domain.User;

import java.util.TimeZone;

/**
 * Created by paristote on 7/7/15.
 */
public interface KittenSettingsService {

    void setTimezoneForUser(String timezoneId, User user);

    void setTimezoneForUser(TimeZone timezone, User user);

    String getUserTimezone(User user);

    String getUserTimezone(String username);

}
