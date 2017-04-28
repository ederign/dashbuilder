package org.dashbuilder.client.summit.table;

import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.common.client.dom.Image;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class LogoWidget implements IsElement {


    @Inject
    @DataField
    Div div;

    @Inject
    @DataField
    Image logo;

    public void init( String logoURL, String textAlign, String height, String width ) {
        logo.setAttribute( "src", logoURL );
        logo.setAttribute( "height", height );
        logo.setAttribute( "width", width );
        div.setAttribute( "style", "text-align:"+textAlign );
    }
}
