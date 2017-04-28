package org.dashbuilder.client.summit.table;

import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Modal;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.uberfire.ext.layout.editor.client.api.HasModalConfiguration;
import org.uberfire.ext.layout.editor.client.api.ModalConfigurationContext;
import org.uberfire.ext.layout.editor.client.api.RenderingContext;
import org.uberfire.ext.plugin.client.perspective.editor.api.PerspectiveEditorDragComponent;
import org.uberfire.ext.properties.editor.model.PropertyEditorChangeEvent;
import org.uberfire.ext.properties.editor.model.PropertyEditorFieldInfo;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Arrays;

@ApplicationScoped
public class HRTableComponent implements PerspectiveEditorDragComponent,
        HasModalConfiguration {


    public static final String RULE = "Rule";
    public static final String ENDPOINT = "EndPoint";


    @Inject
    private ManagedInstance<HRTable> tableTest;

    private ModalConfigurationContext configContext;

    @PostConstruct
    public void setup() {
    }

    @Override
    public String getDragComponentTitle() {
        return "HR Table";
    }

    @Override
    public IsWidget getPreviewWidget( RenderingContext ctx ) {
        return getShowWidget( ctx );
    }

    @Override
    public IsWidget getShowWidget( RenderingContext ctx ) {

        HRTable hrTable = this.tableTest.get();
        if ( ctx != null && ctx.getComponent() != null && ctx.getComponent().getProperties() != null ) {
            if ( ctx.getComponent().getProperties().get( RULE ) == null ) {
                hrTable.filter(  "" );
            } else {
                hrTable.filter( ( ctx.getComponent().getProperties().get( RULE ) ) );
            }
        }
        return hrTable;
    }

    @Override
    public Modal getConfigurationModal( ModalConfigurationContext ctx ) {
        this.configContext = ctx;
        return new EditHRTable( ctx, Arrays.asList( "frontend.drl", "full-stack.drl", "backend.drl" ) );
    }


    public void observeEditComponentEventFromPropertyEditor( @Observes PropertyEditorChangeEvent event ) {

        PropertyEditorFieldInfo property = event.getProperty();
        if ( property.getEventId().equalsIgnoreCase( EditHRTable.PROPERTY_EDITOR_KEY ) ) {
            configContext.setComponentProperty( property.getLabel(), property.getCurrentStringValue() );
        }
    }

}