package org.exoplatform.addons.codefest.team_c.service.impl;

import org.exoplatform.addons.codefest.team_c.domain.User;
import org.exoplatform.addons.codefest.team_c.service.KittenSettingsService;
import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by paristote on 7/7/15.
 */
public class KittenSettingsServiceImpl implements KittenSettingsService {

    private final String KEY_PREFIX = "Timezone_Setting";

    private SettingService settingService;

    public KittenSettingsServiceImpl(SettingService settings) {
        this.settingService = settings;
    }

    private String key(String username) {
        if (username == null)
            throw new IllegalArgumentException("Cannot use null as a key in the settings service.");

        return String.format("%s_%s", KEY_PREFIX, username);
    }

    @Override
    public void setTimezoneForUser(String timezoneId, User user) {
        List<String> timezones = Arrays.asList(TimeZone.getAvailableIDs());
        if (timezones.contains(timezoneId)) {
            SettingValue<String> value = SettingValue.create(timezoneId);
            settingService.set(Context.USER, Scope.GLOBAL, key(user.getName()), value);
        } else {
            // Incorrect timezone
            throw new IllegalArgumentException(String.format("Incorrect timezone: %s is not a valid timezone ID.", timezoneId));
        }
    }

    @Override
    public void setTimezoneForUser(TimeZone timezone, User user) {

        if (timezone != null) {
            setTimezoneForUser(timezone.getID(), user);
        } else {
            // Incorrect timezone
            throw new IllegalArgumentException(String.format("Incorrect timezone: null is not a valid timezone."));
        }
    }

    @Override
    public String getUserTimezone(User user) {
        return getUserTimezone(user.getName());
    }

    @Override
    public String getUserTimezone(String username) {
        SettingValue<String> value = (SettingValue<String>)settingService.get(Context.USER, Scope.GLOBAL, key(username));
        return value == null ? null : value.getValue() ;
    }
}
