package org.dashbuilder.client.summit.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.shared.event.ModalHiddenEvent;
import org.gwtbootstrap3.client.shared.event.ModalHiddenHandler;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.plugin.client.resources.i18n.CommonConstants;
import org.uberfire.ext.properties.editor.client.PropertyEditorWidget;
import org.uberfire.ext.properties.editor.model.PropertyEditorCategory;
import org.uberfire.ext.properties.editor.model.PropertyEditorEvent;
import org.uberfire.ext.properties.editor.model.PropertyEditorFieldInfo;
import org.uberfire.ext.properties.editor.model.PropertyEditorType;
import org.uberfire.ext.widgets.common.client.common.popups.BaseModal;
import org.uberfire.ext.widgets.common.client.common.popups.ButtonPressed;
import org.uberfire.ext.widgets.common.client.common.popups.footers.ModalFooterOKCancelButtons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditHRTable extends BaseModal {

    public static String PROPERTY_EDITOR_KEY = "EditHRTable";

    private final ModalConfigurationContext configContext;
    private List<String> rules;

    @UiField
    PropertyEditorWidget propertyEditor;

    private ButtonPressed buttonPressed = ButtonPressed.CLOSE;

    private Map<String, String> lastParametersSaved = new HashMap<String, String>();

    interface Binder
            extends
            UiBinder<Widget, EditHRTable> {

    }

    private static Binder uiBinder = GWT.create( Binder.class );

    public EditHRTable( ModalConfigurationContext configContext, List<String> rules ) {
        this.configContext = configContext;
        this.rules = rules;
        setTitle( CommonConstants.INSTANCE.EditComponent() );
        setBody( uiBinder.createAndBindUi( EditHRTable.this ) );
        propertyEditor.handle( generateEvent( generateScreenSettingsCategory() ) );
        saveOriginalState();
        add( new ModalFooterOKCancelButtons(
                     new Command() {
                         @Override
                         public void execute() {
                             okButton();
                         }
                     },
                     new Command() {
                         @Override
                         public void execute() {
                             cancelButton();
                         }
                     }
             )
        );
        addHiddenHandler();

    }

    private void saveOriginalState() {
        lastParametersSaved = new HashMap<String, String>();
        Map<String, String> layoutComponentProperties = configContext.getComponentProperties();
        for ( String key : layoutComponentProperties.keySet() ) {
            lastParametersSaved.put( key, layoutComponentProperties.get( key ) );
        }
    }

    protected void addHiddenHandler() {
        addHiddenHandler( new ModalHiddenHandler() {
            @Override
            public void onHidden( ModalHiddenEvent hiddenEvent ) {
                if ( userPressedCloseOrCancel() ) {
                    revertChanges();
                    configContext.configurationCancelled();
                }
            }
        } );
    }

    private boolean userPressedCloseOrCancel() {
        return ButtonPressed.CANCEL.equals( buttonPressed ) || ButtonPressed.CLOSE.equals( buttonPressed );
    }

    private void revertChanges() {
        configContext.resetComponentProperties();
        for ( String key : lastParametersSaved.keySet() ) {
            configContext.setComponentProperty( key, lastParametersSaved.get( key ) );
        }
    }

    public void show() {
        super.show();
    }

    void okButton() {
        buttonPressed = ButtonPressed.OK;

        configContext.configurationFinished();

        hide();
    }

    void cancelButton() {
        buttonPressed = ButtonPressed.CANCEL;
        hide();
    }

    @Override
    public void hide() {
        super.hide();
    }


    private PropertyEditorCategory generateScreenSettingsCategory() {

        PropertyEditorCategory category = new PropertyEditorCategory( "Table Properties" );

        final Map<String, String> parameters = configContext.getComponentProperties();
        String rule = parameters.get( HRTableComponent.RULE );
        String endPoint = parameters.get( HRTableComponent.ENDPOINT );

        if ( rule == null ) {
            rule = rules.get( 1 );
            configContext.setComponentProperty( HRTableComponent.RULE, rule );
        }
        if ( endPoint == null ) {
            endPoint = "http://kie-endpoint";
        }

        category.withField( new PropertyEditorFieldInfo( HRTableComponent.RULE,
                                                         rule, PropertyEditorType.COMBO )
                                    .withComboValues( rules )
                                    .withKey( configContext.hashCode() + rule ) );
        category.withField( new PropertyEditorFieldInfo( HRTableComponent.ENDPOINT, endPoint,
                                                         PropertyEditorType.TEXT )
                                    .withKey( configContext.hashCode() + endPoint ) );

        propertyEditor.setLastOpenAccordionGroupTitle( category.getName() );

        return category;
    }

    private PropertyEditorEvent generateEvent( PropertyEditorCategory category ) {
        PropertyEditorEvent event = new PropertyEditorEvent( PROPERTY_EDITOR_KEY, category );
        return event;
    }

    protected ModalConfigurationContext getConfigContext() {
        return this.configContext;
    }

}