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
package org.dashbuilder.renderer.chartjs;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.google.gwt.user.client.Window;
import org.dashbuilder.dataset.DataColumn;
import org.dashbuilder.displayer.DisplayerSettings;
import org.dashbuilder.displayer.DisplayerType;
import org.dashbuilder.displayer.client.AbstractDisplayer;
import org.dashbuilder.displayer.client.AbstractRendererLibrary;
import org.dashbuilder.displayer.client.Displayer;
import org.dashbuilder.displayer.client.RendererLibLocator;
import org.dashbuilder.renderer.chartjs.lib.ChartJs;

/**
 * Chart JS renderer
 *
 * <p>Pending stuff:
 * <ul>
 *     <li>Values format</li>
 * </ul>
 * </p>
 */
@ApplicationScoped
@Named(ChartJsRenderer.UUID + "_renderer")
public class ChartJsRenderer extends AbstractRendererLibrary {

    public static final String UUID = "Chart JS";

    @PostConstruct
    private void init() {
        ChartJs.ensureInjected();
        publishChartJsFunctions();
        RendererLibLocator.get().registerRenderer(DisplayerType.BARCHART, UUID, false);
    }

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public Displayer lookupDisplayer(DisplayerSettings displayerSettings) {
        DisplayerType type = displayerSettings.getType();
        if ( DisplayerType.BARCHART.equals(type)) return new ChartJsBarChartDisplayer();
        return null;
    }

    private native void publishChartJsFunctions() /*-{
        $wnd.$chartJsFormatValue = @org.dashbuilder.renderer.chartjs.ChartJsRenderer::formatValue(Ljava/lang/Object;);
    }-*/;

    public static String formatValue(Object value) {
        Window.alert(value.toString());
        return value.toString();
    }

    public static String formatValue(AbstractDisplayer displayer, Object value, int column) {
        DataColumn dataColumn = displayer.getDataSetHandler().getLastDataSet().getColumnByIndex(column);
        return displayer.formatValue(value, dataColumn);
    }
}
