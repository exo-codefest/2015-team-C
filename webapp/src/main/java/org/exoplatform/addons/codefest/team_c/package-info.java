/*
 * Copyright (C) 2015 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

@Application(defaultController = KittenSaverController.class)
@Portlet
@Bindings({
    @Binding(value = KittenSaviorDAO.class, implementation = KittenSaviorDAOImpl.class),
    @Binding(value = KittenSaverService.class, implementation = KittenSaverServiceImpl.class),
    @Binding(value = CalendarService.class)
})
@Scripts({
    @Script(id = "jquery", value = "javascript/jquery-1.7.1.js"),    
    @Script(id = "timer", value = "javascript/timer.js", depends = "jquery"),
    @Script(id = "band", value = "javascript/band.js", depends = "timer"),
    @Script(id= "timeSelector",value = "javascript/timeSelector.js", depends = "band"),
    @Script(id = "kitten", value = "javascript/kitten.js", depends = "timeSelector"),
})
@Stylesheets({
    @Stylesheet(id = "main.css", value = "styles/main.css")
})
@Less({
    @Stylesheet(id = "style-less", value = "less/style.less")
})
@Assets("*")
package org.exoplatform.addons.codefest.team_c;

import juzu.Application;
import juzu.plugin.asset.Assets;
import juzu.plugin.asset.Script;
import juzu.plugin.asset.Scripts;
import juzu.plugin.asset.Stylesheets;
import juzu.plugin.asset.Stylesheet;
import juzu.plugin.binding.Binding;
import juzu.plugin.binding.Bindings;
import juzu.plugin.less4j.Less;
import juzu.plugin.portlet.Portlet;
import org.exoplatform.addons.codefest.team_c.controller.KittenSaverController;
import org.exoplatform.addons.codefest.team_c.dao.KittenSaviorDAO;
import org.exoplatform.addons.codefest.team_c.dao.impl.KittenSaviorDAOImpl;
import org.exoplatform.addons.codefest.team_c.service.KittenSaverService;
import org.exoplatform.addons.codefest.team_c.service.impl.KittenSaverServiceImpl;
import org.exoplatform.calendar.service.CalendarService;
