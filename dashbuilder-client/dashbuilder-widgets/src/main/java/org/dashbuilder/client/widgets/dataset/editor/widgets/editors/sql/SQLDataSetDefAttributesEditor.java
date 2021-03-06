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
package org.dashbuilder.client.widgets.dataset.editor.widgets.editors.sql;

import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.dashbuilder.common.client.validation.editors.ValueBoxEditorDecorator;
import org.dashbuilder.dataset.client.DataSetClientServices;
import org.dashbuilder.dataset.client.validation.editors.SQLDataSetDefEditor;
import org.dashbuilder.client.widgets.dataset.editor.widgets.editors.AbstractDataSetDefEditor;
import org.dashbuilder.dataset.def.DataSetDef;
import org.dashbuilder.dataset.def.SQLDataSetDef;

import javax.enterprise.context.Dependent;
import java.util.List;

/**
 * <p>This is the view implementation for Data Set Editor widget for editing sql provider specific attributes.</p>
 *
 * @since 0.3.0 
 */
@Dependent
public class SQLDataSetDefAttributesEditor extends AbstractDataSetDefEditor implements SQLDataSetDefEditor {
    
    interface SQLDataSetDefAttributesEditorBinder extends UiBinder<Widget, SQLDataSetDefAttributesEditor> {}
    private static SQLDataSetDefAttributesEditorBinder uiBinder = GWT.create(SQLDataSetDefAttributesEditorBinder.class);

    @UiField
    FlowPanel sqlAttributesPanel;

    @UiField
    @Path("dataSource")
    ValueBoxEditorDecorator<String> dataSource;

    @UiField
    @Path("dbSchema")
    ValueBoxEditorDecorator<String> dbSchema;

    @UiField
    @Ignore
    RadioButton tableButton;
    
    @UiField
    @Path("dbTable")
    ValueBoxEditorDecorator<String> dbTable;

    @UiField
    @Ignore
    RadioButton queryButton;

    @UiField
    @Path("dbSQL")
    ValueBoxEditorDecorator<String> dbQuery;

    private boolean isEditMode;

    public SQLDataSetDefAttributesEditor() {
        initWidget(uiBinder.createAndBindUi(this));
        
        // Configure radio buttons.
        tableButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) enableTable();
                else enableQuery();
            }
        });

        queryButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) enableQuery();
                else enableTable();
            }
        });
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public void showErrors(List<EditorError> errors) {
        consumeErrors(errors);
    }

    private SQLDataSetDef getDataSetDef() {
        return (SQLDataSetDef) dataSetDef;
    }
    
    @Override
    public void set(DataSetDef dataSetDef) {
        super.set(dataSetDef);
        
        // Enable table by default.
        enableTable();
    }
    
    private void enableTable() {
        dbTable.setEnabled(true);
        dbTable.setVisible(true);
        dbQuery.setEnabled(false);
        dbQuery.setVisible(false);
        tableButton.setValue(true);
        queryButton.setValue(false);
    }

    private void enableQuery() {
        dbTable.setEnabled(false);
        dbTable.setVisible(false);
        dbQuery.setEnabled(true);
        dbQuery.setVisible(true);
        tableButton.setValue(false);
        queryButton.setValue(true);
    }

    public boolean isUsingTable() {
        return dbTable.isVisible();
    }

}
