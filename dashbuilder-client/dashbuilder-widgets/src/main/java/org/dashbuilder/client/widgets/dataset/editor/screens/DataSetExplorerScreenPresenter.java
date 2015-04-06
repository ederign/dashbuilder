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
package org.dashbuilder.client.widgets.dataset.editor.screens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import org.dashbuilder.client.widgets.dataset.editor.widgets.DataSetExplorer;
import org.dashbuilder.client.widgets.dataset.editor.widgets.events.*;
import org.dashbuilder.dataset.uuid.UUIDGenerator;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.Menus;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@WorkbenchScreen(identifier = "DataSetExplorer")
@Dependent
public class DataSetExplorerScreenPresenter {

    private DataSetExplorer explorerWidget;
    
    private Menus menu = null;
    
    @Inject
    Event<NewDataSetEvent> newDataSetEvent;

    @Inject
    Event<EditDataSetEvent> editDataSetEvent;

    @Inject
    private UUIDGenerator uuidGenerator;

    @OnStartup
    public void onStartup( final PlaceRequest placeRequest) {
        explorerWidget = new DataSetExplorer();
        explorerWidget.addEditDataSetEventHandler(new EditDataSetEventHandler() {
            @Override
            public void onEditDataSet(EditDataSetEvent event) {
                editDataSet(event);
            }
        });
        explorerWidget.addDeleteDataSetEventHandler(new DeleteDataSetEventHandler() {
            @Override
            public void onDeleteDataSet(DeleteDataSetEvent event) {
                deleteDataSet(event);
            }
        });
        this.menu = makeMenuBar();
    }

    @OnClose
    public void onClose() {
        // TODO: Close editor widget.
    }

    // TODO: i18n.
    @WorkbenchPartTitle
    public String getTitle() {
        return "Data Set Explorer Screen";
    }

    @WorkbenchPartView
    public IsWidget getView() {
        return explorerWidget;
    }

    @WorkbenchMenu
    public Menus getMenu() {
        return menu;
    }

    private Menus makeMenuBar() {
        return MenuFactory
                // TODO: i18n.
                .newTopLevelMenu("New data set")
                .respondsWith(getNewCommand())
                .endMenu()
                .build();
    }

    private Command getNewCommand() {
        return new Command() {
            public void execute() {
                newDataSet();
            }
        };
    }
    
    void newDataSet() {
        // TODO: Generate uuid using the backend uuid generator. Perform a RPC call.
        String uuid = uuidGenerator.newUuid();
        GWT.log("Create data set " + uuid);
        NewDataSetEvent event = new NewDataSetEvent(uuid);
        newDataSetEvent.fire(event);
    }
    
    void editDataSet(EditDataSetEvent event) {
        GWT.log("Edit data set " + event.getUuid());
        editDataSetEvent.fire(event);
    }

    void deleteDataSet(DeleteDataSetEvent event) {
        deleteDataSet(event.getUuid());
    }
    
    public void deleteDataSet(String uuid) {
        GWT.log("Delete data set " + uuid);
        // TODO
    }
}