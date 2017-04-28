//package org.dashbuilder.client.summit.table;
//
//import com.google.gwt.user.client.ui.IsWidget;
//import org.jboss.errai.common.client.ui.ElementWrapperWidget;
//import org.jboss.errai.ioc.client.api.ManagedInstance;
//import org.uberfire.ext.layout.editor.client.api.RenderingContext;
//import org.uberfire.ext.plugin.client.perspective.editor.api.PerspectiveEditorDragComponent;
//import org.uberfire.ext.plugin.client.perspective.editor.layout.editor.popups.EditHTMLPresenter;
//import org.uberfire.mvp.ParameterizedCommand;
//
//import javax.inject.Inject;
//
//public class LogoDragComponent implements PerspectiveEditorDragComponent {
//
//    @Inject
//    private EditHTMLPresenter htmlEditor;
//
//
//    @Inject
//    private ManagedInstance<LogoWidget> logoWidgets;
//
//    @Inject
//    LogoPreferences logoPreferences;
//
//    @Override
//    public String getDragComponentTitle() {
//        return "Company Logo";
//    }
//
//    @Override
//    public IsWidget getPreviewWidget( RenderingContext container ) {
//        return getShowWidget( container );
//    }
//
//    @Override
//    public IsWidget getShowWidget( RenderingContext context ) {
//        String logoURL = "http://uberfireframework.org/upload/images/uberfire-logo.png";
//        String textAlign = "center";
//        LogoWidget logoWidget = logoWidgets.get();
//
//        logoPreferences.load( prefs -> {
//                                  logoWidget.init( prefs.getLogoURL() ,
//                                                   prefs.getTextAlign(),
//                                                   prefs.getHeight(),
//                                                   prefs.getWidth());
//        }, e -> {
//
//        }
//
//        );
//
//        return ElementWrapperWidget.getWidget( logoWidget.getElement() );
//    }
//
//
//}