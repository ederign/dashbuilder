/**
 * Copyright (C) 2015 JBoss Inc
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
package org.dashbuilder.renderer.client.metric;

import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.Hero;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.dashbuilder.common.client.StringUtils;
import org.dashbuilder.displayer.DisplayerSettings;

public class MetricView extends Composite {

    @UiField
    protected Hero heroPanel;

    @UiField
    protected Panel centerPanel;

    @UiField
    protected Paragraph titlePanel;

    @UiField
    protected Panel metricPanel;

    @UiField
    protected Heading metricHeading;

    protected DisplayerSettings displayerSettings;

    interface Binder extends UiBinder<Widget, MetricView> {}
    private static Binder uiBinder = GWT.create(Binder.class);

    public MetricView(DisplayerSettings displayerSettings) {
        this.displayerSettings = displayerSettings;
        initWidget(uiBinder.createAndBindUi(this));

        int w = displayerSettings.getChartWidth();
        int h = displayerSettings.getChartHeight();
        int mtop = displayerSettings.getChartMarginTop();
        int mbottom = displayerSettings.getChartMarginBottom();
        int mleft = displayerSettings.getChartMarginLeft();
        int mright = displayerSettings.getChartMarginRight();

        // Hero panel (size)
        Style style = heroPanel.getElement().getStyle();
        style.setPadding(0, Style.Unit.PX);
        style.setWidth(w, Style.Unit.PX);
        style.setHeight(h, Style.Unit.PX);
        style.setTextAlign(Style.TextAlign.CENTER);
        style.setVerticalAlign(Style.VerticalAlign.MIDDLE);
        if (!StringUtils.isBlank(displayerSettings.getChartBackgroundColor())) {
            style.setBackgroundColor("#" + displayerSettings.getChartBackgroundColor());
        }

        // Center panel (paddings)
        style = centerPanel.getElement().getStyle();
        style.setPaddingTop(mtop, Style.Unit.PX);
        style.setPaddingBottom(mbottom, Style.Unit.PX);
        style.setPaddingLeft(mleft, Style.Unit.PX);
        style.setPaddingRight(mright, Style.Unit.PX);

        // Title panel
        titlePanel.setVisible(displayerSettings.isTitleVisible());
        titlePanel.setText(displayerSettings.getTitle());

        // TODO: Metric panel
        //metricPanel.clear();
        //metricHeading = new Heading(displayerSettings.getMetricTextSize());
        //metricPanel.add(metricHeading);
    }

    public void updateMetric(String value) {
        metricHeading.setText(value);
    }
}
