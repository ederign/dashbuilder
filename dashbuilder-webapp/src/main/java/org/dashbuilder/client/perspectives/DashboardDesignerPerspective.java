/**
 * Copyright (C) 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.client.perspectives;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.dashbuilder.client.resources.i18n.AppConstants;
import org.dashbuilder.displayer.DisplayerSettings;
import org.dashbuilder.displayer.client.PerspectiveCoordinator;
import org.dashbuilder.displayer.client.json.DisplayerSettingsJSONMarshaller;
import org.dashbuilder.displayer.client.widgets.DisplayerEditor;
import org.dashbuilder.displayer.client.widgets.DisplayerEditorPopup;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.panels.impl.MultiListWorkbenchPanelPresenter;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

/**
 * The dashboard composer perspective.
 */
@ApplicationScoped
@WorkbenchPerspective(identifier = "DashboardDesignerPerspective", isTransient = false)
public class DashboardDesignerPerspective {

    @Inject
    private PlaceManager placeManager;

    @Inject
    DisplayerSettingsJSONMarshaller jsonHelper;

    @Inject
    private PerspectiveCoordinator perspectiveCoordinator;

    @Perspective
    public PerspectiveDefinition buildPerspective() {
        PerspectiveDefinition perspective = new PerspectiveDefinitionImpl(MultiListWorkbenchPanelPresenter.class.getName());
        perspective.setName(AppConstants.INSTANCE.dbdesignerpersp_name());
        return perspective;
    }

    @WorkbenchMenu
    public Menus buildMenuBar() {
        return MenuFactory
                .newTopLevelMenu(AppConstants.INSTANCE.dbdesignerpersp_newdisplayer())
                .respondsWith(getNewDisplayerCommand())
                .endMenu().build();
    }

    private Command getNewDisplayerCommand() {
        return new Command() {
            public void execute() {
                /* Displayer settings == null => Create a brand new displayer */
                perspectiveCoordinator.editOn();
                DisplayerEditorPopup displayerEditor = new DisplayerEditorPopup();
                displayerEditor.init(null, new DisplayerEditor.Listener() {

                    public void onClose(DisplayerEditor editor) {
                        perspectiveCoordinator.editOff();
                    }

                    public void onSave(DisplayerEditor editor) {
                        perspectiveCoordinator.editOff();
                        placeManager.goTo(createPlaceRequest(editor.getDisplayerSettings()));
                    }
                });
            }
        };
    }

    private PlaceRequest createPlaceRequest(DisplayerSettings displayerSettings) {
        String json = jsonHelper.toJsonString(displayerSettings);
        Map<String,String> params = new HashMap<String,String>();
        params.put("json", json);
        params.put("edit", "true");
        params.put("clone", "true");
        return new DefaultPlaceRequest("DisplayerScreen", params);
    }
}
