/*
   Copyright (c) 2014 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.charts.client.xy.bar;

import com.ait.lienzo.charts.client.AbstractChart;
import com.ait.lienzo.charts.client.ChartAttribute;
import com.ait.lienzo.charts.client.ChartNodeType;
import com.ait.lienzo.charts.client.axis.Axis;
import com.ait.lienzo.charts.client.axis.CategoryAxis;
import com.ait.lienzo.charts.client.xy.XYChartData;
import com.ait.lienzo.charts.client.xy.XYChartSerie;
import com.ait.lienzo.charts.client.xy.axis.AxisBuilder;
import com.ait.lienzo.charts.client.xy.axis.CategoryAxisBuilder;
import com.ait.lienzo.charts.client.xy.axis.NumericAxisBuilder;
import com.ait.lienzo.charts.shared.core.types.*;
import com.ait.lienzo.client.core.animation.AnimationProperties;
import com.ait.lienzo.client.core.animation.AnimationProperty;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.animation.LayerRedrawManager;
import com.ait.lienzo.client.core.event.*;
import com.ait.lienzo.client.core.shape.*;
import com.ait.lienzo.client.core.shape.json.IFactory;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.types.Point2DArray;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.lienzo.shared.core.types.TextAlign;
import com.ait.lienzo.shared.core.types.TextBaseLine;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>XY chart implementation using rectangles as shapes for values.</p>
 * <p>It can be drawn as a bar chart or a column chart depending on the <code>CHART_ORIENTATION</code> attribute.</p>
 *  
 * <p>Attributes:</p>
 * <ul>
 *     <li><code>X</code>: The chart X position.</li>
 *     <li><code>Y</code>: The chart Y position.</li>
 *     <li><code>WIDTH</code>: The chart width.</li>
 *     <li><code>HEIGHT</code>: The chart height.</li>
 *     <li><code>MARGIN_LEFT</code>: The left margin.</li>
 *     <li><code>MARGIN_RIGHT</code>: The right margin.</li>
 *     <li><code>MARGIN_TOP</code>: The top margin.</li>
 *     <li><code>MARGIN_BOTTOM</code>: The bottom margin.</li>
 *     <li><code>NAME</code>: The chart name, used as title.</li>
 *     <li><code>SHOW_TITLE</code>: Flag for title visibility.</li>
 *     <li><code>FONT_FAMILY</code>: The chart font family.</li>
 *     <li><code>FONT_STYLE</code>: The chart font style.</li>
 *     <li><code>FONT_SIZE</code>: The chart font size.</li>
 *     <li><code>RESIZABLE</code>: Add or avoid the use of the chart resizer.</li>
 *     <li><code>ANIMATED</code>: Enable animations.</li>
 *     <li><code>LEGEND_POSITION</code>: The chart legend position.</li>
 *     <li><code>LEGEND_ALIGN</code>: The chart legend alignment.</li>
 *     <li><code>XY_CHART_DATA</code>: The chart data.</li>
 *     <li><code>CATEGORY_AXIS</code>: The chart category axis.</li>
 *     <li><code>VALUES_AXIS</code>: The chart values axis.</li>
 *     <li><code>SHOW_CATEGORIES_AXIS_TITLE</code>: Show the title for categoreis axis.</li>
 *     <li><code>SHOW_VALUES_AXIS_TITLE</code>: Show the title for values axis.</li>
 *     <li><code>CATEGORIES_AXIS_LABELS_POSITION</code>: The position for the categories axis labels.</li>
 *     <li><code>VALUES_AXIS_LABELS_POSITION</code>: The position for the values axis labels.</li>
 *     <li><code>ALIGN</code>: The chart alignment.</li>
 *     <li><code>DIRECTION</code>: The chart direction.</li>
 *     <li><code>ORIENTATION</code>: The chart orientation (Bar or Column).</li>
 * </ul>
 * 
 * <p>It listens the <code>AttributesChangedEvent</code> for attribute <code>XY_CHART_DATA</code>.</p> 
 */
public class BarChart extends AbstractChart<BarChart>
{
    // Default separation size between bars.
    protected static final double BAR_SEPARATION = 2;
    private ChartLegend legend = null; // The legend.
    final BarChartTooltip tooltip = new BarChartTooltip();

    protected BarChart(JSONObject node, ValidationContext ctx) throws ValidationException
    {
        super(node, ctx);

        setNodeType(ChartNodeType.BAR_CHART);
    }

    public BarChart(XYChartData data)
    {
        setNodeType(ChartNodeType.BAR_CHART);

        setData(data);

        getMetaData().put("creator", "Roger Martinez");
    }

    public final BarChart setCategoriesAxis(CategoryAxis xAxis)
    {
        if (null != xAxis)
        {
            getAttributes().put(ChartAttribute.CATEGORIES_AXIS.getProperty(), xAxis.getJSO());
        }
        else
        {
            getAttributes().delete(ChartAttribute.CATEGORIES_AXIS.getProperty());
        }
        return this;
    }

    protected final Axis.AxisJSO getCategoriesAxis()
    {
        return getAttributes().getObject(ChartAttribute.CATEGORIES_AXIS.getProperty()).cast();
    }

    public final BarChart setValuesAxis(Axis yAxis)
    {
        if (null != yAxis)
        {
            getAttributes().put(ChartAttribute.VALUES_AXIS.getProperty(), yAxis.getJSO());
        }
        else
        {
            getAttributes().delete(ChartAttribute.VALUES_AXIS.getProperty());
        }
        return this;
    }

    protected final Axis.AxisJSO getValuesAxis()
    {
        return getAttributes().getObject(ChartAttribute.VALUES_AXIS.getProperty()).cast();
    }

    public final BarChart setData(XYChartData data)
    {
        if (null != data)
        {
            getAttributes().put(ChartAttribute.XY_CHART_DATA.getProperty(), data.getJSO());
        }
        else
        {
            getAttributes().delete(ChartAttribute.XY_CHART_DATA.getProperty());
        }
        return this;
    }

    public final XYChartData getData()
    {
        XYChartData.XYChartDataJSO jso = getAttributes().getArrayOfJSO(ChartAttribute.XY_CHART_DATA.getProperty()).cast();

        return new XYChartData(jso);
    }

    public final BarChart setShowCategoriesAxisTitle(boolean showCategoriesAxisTitle)
    {
        getAttributes().put(ChartAttribute.SHOW_CATEGORIES_AXIS_TITLE.getProperty(), showCategoriesAxisTitle);
        return this;
    }

    public final boolean isShowCategoriesAxisTitle()
    {
        if (getAttributes().isDefined(ChartAttribute.SHOW_CATEGORIES_AXIS_TITLE))
        {
            return getAttributes().getBoolean(ChartAttribute.SHOW_CATEGORIES_AXIS_TITLE.getProperty());
        }
        return true;
    }

    public final BarChart setShowValuesAxisTitle(boolean showValuesAxisTitle)
    {
        getAttributes().put(ChartAttribute.SHOW_VALUES_AXIS_TITLE.getProperty(), showValuesAxisTitle);
        return this;
    }

    public final boolean isShowValuesAxisTitle()
    {
        if (getAttributes().isDefined(ChartAttribute.SHOW_VALUES_AXIS_TITLE))
        {
            return getAttributes().getBoolean(ChartAttribute.SHOW_VALUES_AXIS_TITLE.getProperty());
        }
        return true;
    }

    public final  BarChart setCategoriesAxisLabelsPosition(LabelsPosition labelsPosition)
    {
        if (null != labelsPosition)
        {
            getAttributes().put(ChartAttribute.CATEGORIES_AXIS_LABELS_POSITION.getProperty(), labelsPosition.getValue());
        }
        else
        {
            getAttributes().delete(ChartAttribute.CATEGORIES_AXIS_LABELS_POSITION.getProperty());
        }
        return this;
    }

    public final LabelsPosition getCategoriesAxisLabelsPosition()
    {
        return LabelsPosition.lookup(getAttributes().getString(ChartAttribute.CATEGORIES_AXIS_LABELS_POSITION.getProperty()));
    }

    public final  BarChart setValuesAxisLabelsPosition(LabelsPosition labelsPosition)
    {
        if (null != labelsPosition)
        {
            getAttributes().put(ChartAttribute.VALUES_AXIS_LABELS_POSITION.getProperty(), labelsPosition.getValue());
        }
        else
        {
            getAttributes().delete(ChartAttribute.VALUES_AXIS_LABELS_POSITION.getProperty());
        }
        return this;
    }

    public final LabelsPosition getValuesAxisLabelsPosition()
    {
        return LabelsPosition.lookup(getAttributes().getString(ChartAttribute.VALUES_AXIS_LABELS_POSITION.getProperty()));
    }
   
    
    @Override
    public JSONObject toJSONObject()
    {
        JSONObject object = new JSONObject();

        object.put("type", new JSONString(getNodeType().getValue()));

        if (!getMetaData().isEmpty())
        {
            object.put("meta", new JSONObject(getMetaData().getJSO()));
        }
        object.put("attributes", new JSONObject(getAttributes().getJSO()));

        return object;
    }

    @Override
    public IFactory<Group> getFactory()
    {
        return new BarChartFactory();
    }

    public static class BarChartFactory extends ChartFactory
    {
        public BarChartFactory()
        {
            super();
            setTypeName(ChartNodeType.BAR_CHART.getValue());
            addAttribute(ChartAttribute.XY_CHART_DATA, true);
            addAttribute(ChartAttribute.CATEGORIES_AXIS, true);
            addAttribute(ChartAttribute.VALUES_AXIS, true);
            addAttribute(ChartAttribute.SHOW_CATEGORIES_AXIS_TITLE, false);
            addAttribute(ChartAttribute.SHOW_VALUES_AXIS_TITLE, false);
            addAttribute(ChartAttribute.CATEGORIES_AXIS_LABELS_POSITION, false);
            addAttribute(ChartAttribute.VALUES_AXIS_LABELS_POSITION, false);
        }

        @Override
        public boolean addNodeForContainer(IContainer<?, ?> container, Node<?> node, ValidationContext ctx)
        {
            return false;
        }

        @Override
        public BarChart create(JSONObject node, ValidationContext ctx) throws ValidationException
        {
            return new BarChart(node, ctx);
        }
    }
    
    protected static boolean isVertical(ChartOrientation orientation) {
        return ChartOrientation.VERTICAL.equals(orientation);
    }

    protected static boolean isPositiveDirection(ChartDirection direction) {
        return ChartDirection.POSITIVE.equals(direction);
    }

    protected boolean isShowCategoriesLabels() {
        return !LabelsPosition.NONE.equals(getCategoriesAxisLabelsPosition());
    }

    protected boolean isShowValuesLabels() {
        return !LabelsPosition.NONE.equals(getValuesAxisLabelsPosition());
    }

    protected void doBuild()
    {

        ChartOrientation orientation = getOrientation();
        
        final BarChartBuilder builder = (isVertical(orientation)) ? new VerticalBarChartBuilder() : new HorizontalBarChartBuilder();
        
        // **** Build all shape instances. ****
        
        // Build the categories axis title shape (Text)
        builder.buildCategoriesAxisTitle()
        // Build the values axis title shape (Text)
                .buildValuesAxisTitle()
        // Build the categories axis intervals shapes (Text and Line)
                .buildCategoriesAxisIntervals()
        // Build the values axis intervals shapes (Text and Line)
                .buildValuesAxisIntervals()
        // Build the values shapes (Rectangles)
                .buildValues();

        // Set positions and sizes for shapes.
        Double chartWidth = getChartWidth();
        Double chartHeight = getChartHeight();
        redraw(builder, chartWidth, chartHeight, true);
        
        // Legend.
        LegendPosition legendPosition = getLegendPosition();
        LegendAlign legendAlign = getLegendAlignment();
        if (!LegendPosition.NONE.equals(legendPosition)) {
            legend = new ChartLegend();
            Group legendGroup = legend.build();
            double xLegend = 0;
            double yLegend = 0;
            // TODO: legendAlign
            switch (legendPosition) {
                case TOP:
                    xLegend = getChartWidth() / 2;
                    yLegend = 5;
                    topArea.add(legendGroup.setX(xLegend).setY(yLegend));
                    break;
                case LEFT:
                    xLegend = 5;
                    yLegend = getChartHeight() / 2;
                    leftArea.add(legendGroup.setX(xLegend).setY(yLegend));
                    break;
                case RIGHT:
                    xLegend = 5;
                    yLegend = getChartHeight() / 2;
                    rightArea.add(legendGroup.setX(xLegend).setY(yLegend));
                    break;
                case INSIDE:
                    xLegend = getChartWidth() / 2;
                    yLegend = 2;
                    chartArea.add(legendGroup.setX(xLegend).setY(yLegend));
                    break;
                default:
                    xLegend = getChartWidth() / 2;
                    yLegend = 5;
                    bottomArea.add(legendGroup.setX(xLegend).setY(yLegend));
                    break;
            }

            // Add legend elements.
            XYChartSerie[] series = getData().getSeries();
            if (series != null && series.length > 0) {
                for (XYChartSerie serie : series) {
                    legend.add(serie);
                }
            }
        }
        
        // Tooltip.
        chartArea.add(tooltip.build());
        
        // Add the attributes event change handlers.
        this.addAttributesChangedHandler(ChartAttribute.XY_CHART_DATA, new AttributesChangedHandler() {
            @Override
            public void onAttributesChanged(AttributesChangedEvent event) {
                GWT.log("BarChart - XYData attribute changed.");
                redraw(builder, getChartWidth(), getChartHeight(), true);
                LayerRedrawManager.get().schedule(getLayer());
            }
        });

        this.addAttributesChangedHandler(ChartAttribute.WIDTH, new AttributesChangedHandler() {
            @Override
            public void onAttributesChanged(AttributesChangedEvent event) {
                GWT.log("BarChart - WIDTH attribute changed.");
                redraw(builder, getChartWidth(), getChartHeight(), false);
            }
        });

        this.addAttributesChangedHandler(ChartAttribute.HEIGHT, new AttributesChangedHandler() {
            @Override
            public void onAttributesChanged(AttributesChangedEvent event) {
                GWT.log("BarChart - WIDTH attribute changed.");
                redraw(builder, getChartWidth(), getChartHeight(), false);
            }
        });

    }
    
    private void redraw(BarChartBuilder builder, Double chartWidth, Double chartHeight, boolean animate) {
        
        // Reload axis builder as data has changed.
        builder.reloadBuilders()
        // Recalculate positions and size for categories axis title shape.
                .setCategoriesAxisTitleAttributes(chartWidth, chartHeight, animate)
        // Recalculate positions and size for categories intervals shapes.
                .setCategoriesAxisIntervalsAttributes(chartWidth, chartHeight, animate)
        // Recalculate positions and size for values axis title shape.
                .setValuesAxisTitleAttributes(chartWidth, chartHeight, animate)
        // Recalculate positions and size for values intervals shapes.
                .setValuesAxisIntervalsAttributes(chartWidth, chartHeight, animate)
        // Recalculate positions, size and add or remove rectangles (if data has changed).
                .setValuesAttributes(chartWidth, chartHeight, animate);
    }

    private abstract class BarChartBuilder<T extends BarChartBuilder> {

        final AxisBuilder[] categoriesAxisBuilder = new AxisBuilder[1];
        final AxisBuilder[] valuesAxisBuilder = new AxisBuilder[1];
        Axis.AxisJSO categoriesAxisJSO;
        Axis.AxisJSO valuesAxisJSO;
        Text categoriesAxisTitle;
        Text valuesAxisTitle;
        Line[] valuesAxisIntervals; // The lines that represents the intervals in the Y axis.
        final List<BarChartLabel> valuesLabels = new LinkedList<BarChartLabel>(); // The texts that represents the interval values in the Y axis.
        final List<BarChartLabel> seriesLabels = new LinkedList<BarChartLabel>(); // The labels for each interval (rectangle) in the X axis.
        final Map<String, List<Rectangle>> seriesValues = new LinkedHashMap(); // The rectangles that represents the data.
        
        public BarChartBuilder() {
           
        }
        
        public AxisBuilder getCategoriesAxisBuilder() {
            return categoriesAxisBuilder[0];
        }

        public AxisBuilder getValuesAxisBuilder() {
            return valuesAxisBuilder[0];
        }
        
        public Axis.AxisJSO getCategoriesAxis() {
            return categoriesAxisJSO;
        }
        
        public Axis.AxisJSO getValuesAxis() {
            return valuesAxisJSO;
        }
        
        public abstract T buildCategoriesAxisTitle();

        public abstract T buildValuesAxisTitle();

        public T buildCategoriesAxisIntervals() {
            if (isShowCategoriesLabels()) {
                List<AxisBuilder.AxisLabel> xAxisLabels = categoriesAxisBuilder[0].getLabels();
                if (xAxisLabels != null) {
                    for (int i = 0; i < xAxisLabels.size(); i++) {
                        AxisBuilder.AxisLabel axisLabel = xAxisLabels.get(i);
                        BarChartLabel label = new BarChartLabel(axisLabel);
                        seriesLabels.add(label);
                        addCategoryAxisIntervalLabel(label.build());

                    }
                }
                
            }
            return (T) this;
        }
        
        protected abstract void addCategoryAxisIntervalLabel(IPrimitive label);
        
        
        public T buildValuesAxisIntervals() {
            // Build the shapes axis instances (line for intervals and text for labels).
            List<AxisBuilder.AxisLabel> yAxisLabels = valuesAxisBuilder[0].getLabels(); 
            valuesAxisIntervals = new Line[yAxisLabels.size() + 1];
            int x = 0;
            for (AxisBuilder.AxisLabel yAxisLabel : yAxisLabels) {
                valuesAxisIntervals[x] = new Line(0,0,0,0).setStrokeColor(ColorName.DARKGREY);
                chartArea.add(valuesAxisIntervals[x]);
                if (isShowValuesLabels()) {
                    BarChartLabel label = new BarChartLabel(yAxisLabel);
                    valuesLabels.add(label);
                    addValuesAxisIntervalLabel(label.build());
                }
                x++;
            }
            return (T) this;

        }

        protected abstract void addValuesAxisIntervalLabel(IPrimitive label);
        
        public T buildValues() {
            // Build the chart values as rectangle shapes.
            XYChartSerie[] series = getData().getSeries();
            for (int numSerie = 0; numSerie < series.length; numSerie++) {
                XYChartSerie serie = series[numSerie];
                buildSerieValues(serie, numSerie);
            }
            return (T) this;
        }
        
        protected void buildSerieValues(XYChartSerie serie, int numSerie) {
            List<AxisBuilder.AxisValue> xAxisValues = categoriesAxisBuilder[0].getValues(getData().getCategoryAxisProperty());

            if (xAxisValues != null) {
                List<Rectangle> bars = new LinkedList();
                for (int i = 0; i < xAxisValues.size(); i++) {
                    AxisBuilder.AxisValue axisValue = xAxisValues.get(i);
                    final Rectangle bar = new Rectangle(0,0);
                    bar.setID(getBarId(numSerie, i));
                    bars.add(bar);
                    chartArea.add(bar);
                }
                seriesValues.put(serie.getName(), bars);

            }
            
        }
        
        protected String getBarId(int numSerie, int numValue) {
            return "value"+numSerie+""+numValue;
        }

        protected void removeSerieValues(final String serieName) {
            if (serieName != null) {
                List<Rectangle> barsForSerie = seriesValues.get(serieName);
                if (seriesValues != null) {
                    for (final Rectangle bar : barsForSerie) {
                        setShapeAttributes(bar, null, null, 0d, 0d, true);
                    }
                }
                seriesValues.remove(serieName);
            }

        }
        
        public abstract T setCategoriesAxisTitleAttributes(Double width, Double height, boolean animate);
        public abstract T setValuesAxisTitleAttributes(Double width, Double height, boolean animate);
        public abstract T setCategoriesAxisIntervalsAttributes(Double width, Double height, boolean animate);
        public abstract T setValuesAxisIntervalsAttributes(Double width, Double height, boolean animate);

        public T setValuesAttributes(Double width, Double height, boolean animate) {
            XYChartSerie[] series = getData().getSeries();

            // Find removed series in order to remove bar rectangle instances.
            for (String removedSerieName : categoriesAxisBuilder[0].getDataSummary().getRemovedSeries()) {
                removeSerieValues(removedSerieName);
                if (legend != null) legend.remove(removedSerieName);
            }

            // Iterate over all series.
            for (int numSerie = 0; numSerie < series.length; numSerie++) {
                final XYChartSerie serie = series[numSerie];
                if (serie != null) {

                    // If a new serie is added, build new bar rectangle instances.
                    if (categoriesAxisBuilder[0].getDataSummary().getAddedSeries().contains(serie.getName())) {
                        buildCategoriesAxisIntervals();
                        buildSerieValues(serie, numSerie);
                        if (legend != null) legend.add(serie);
                    }

                    setValuesAttributesForSerie(serie, numSerie, width, height, animate);
                }
            }
            return (T) this;
        }
        
        protected abstract T setValuesAttributesForSerie(final XYChartSerie serie, int numSerie, Double width, Double height, boolean animate);
        
        public abstract T reloadBuilders();

        protected void seriesValuesAlpha(int numSerie, int numValue, double alpha) {
            String barId = getBarId(numSerie, numValue);
            for (Map.Entry<String, List<Rectangle>> entry : seriesValues.entrySet()) {
                List<Rectangle> values = entry.getValue();
                if (values != null && !values.isEmpty()) {
                    for (Rectangle value : values) {
                        String id = value.getID();
                        if (!barId.equals(id)) {
                            AnimationProperties animationProperties = new AnimationProperties();
                            animationProperties.push(AnimationProperty.Properties.ALPHA(alpha));
                            value.animate(AnimationTweener.LINEAR, animationProperties, ANIMATION_DURATION);
                        }
                    }
                }
            }
        }
        
        
    }

    private class VerticalBarChartBuilder extends BarChartBuilder<VerticalBarChartBuilder> {

        final BarChartLabelFormatter valuesLabelFormatter = new BarChartLabelFormatter(valuesLabels);
        final BarChartLabelFormatter categoriesLabelFormatter = new BarChartLabelFormatter(seriesLabels);

        public VerticalBarChartBuilder() {
            // Build categories axis builder.
            categoriesAxisJSO = BarChart.this.getCategoriesAxis();
            AxisBuilder.AxisDirection direction = isPositiveDirection(getDirection()) ? AxisBuilder.AxisDirection.DESC : AxisBuilder.AxisDirection.ASC;
            if (categoriesAxisJSO.getType().equals(Axis.AxisType.CATEGORY)) {
                categoriesAxisBuilder[0] = new CategoryAxisBuilder(getData(), getChartWidth(), direction, categoriesAxisJSO);
            } else if (categoriesAxisJSO.getType().equals(Axis.AxisType.NUMBER)) {
                categoriesAxisBuilder[0] = new NumericAxisBuilder(getData(), getChartWidth(),direction, categoriesAxisJSO);
            } else {
                // TODO: categoriesAxisBuilder = new DateAxisBuilder(getData(), xAxisJSO);
            }

            // Build values axis builder.
            valuesAxisJSO = BarChart.this.getValuesAxis();
            if (valuesAxisJSO.getType().equals(Axis.AxisType.CATEGORY)) {
                throw new RuntimeException("CategoryAxis type cannot be used in BarChart (vertical) for the values axis.");
            } else if (valuesAxisJSO.getType().equals(Axis.AxisType.NUMBER)) {
                valuesAxisBuilder[0] = new NumericAxisBuilder(getData(), getChartHeight(),direction, valuesAxisJSO);
            } else {
                // TODO: valuesAxisBuilder = new DateAxisBuilder(getData(), yAxisJSO);
            }
        }

        public VerticalBarChartBuilder buildCategoriesAxisTitle() {
            if (isShowCategoriesAxisTitle()) {
                // Build the X axis line and title.
                categoriesAxisTitle = new Text(getCategoriesAxis().getTitle(), getFontFamily(), getFontStyle(), getFontSize()).setFillColor(ColorName.SILVER).setX(getChartWidth() / 2).setY(30).setTextAlign(TextAlign.CENTER).setTextBaseLine(TextBaseLine.MIDDLE);
                bottomArea.add(categoriesAxisTitle);
            }
            return this;
        }

        public VerticalBarChartBuilder buildValuesAxisTitle() {
            if (isShowValuesAxisTitle()) {
                // Build the Y axis line and title.
                valuesAxisTitle = new Text(getValuesAxis().getTitle(), getFontFamily(), getFontStyle(), getFontSize()).setFillColor(ColorName.SILVER).setX(10).setY(getChartHeight() / 2).setTextAlign(TextAlign.RIGHT).setTextBaseLine(TextBaseLine.MIDDLE).setRotationDegrees(270);
                leftArea.add(valuesAxisTitle);
            }
            return this;
        }
        
        @Override
        protected void addCategoryAxisIntervalLabel(IPrimitive label) {
            if (!isCategoriesAxisLabelsPositionTop()) bottomArea.add(label);
            else topArea.add(label);
        }

        @Override
        protected void addValuesAxisIntervalLabel(IPrimitive label) {
            if (isValuesAxisLabelsPositionLeft()) leftArea.add(label);
            else rightArea.add(label);
        }

        public VerticalBarChartBuilder setCategoriesAxisTitleAttributes(Double width, Double height, boolean animate) {
            if (categoriesAxisTitle != null) setShapeAttributes(categoriesAxisTitle, width / 2, 30d, null, null, animate);
            return this;
        }
        
        public VerticalBarChartBuilder setValuesAxisTitleAttributes(Double width, Double height, boolean animate) {
            if (valuesAxisTitle != null) setShapeAttributes(valuesAxisTitle, null, height / 2, null, null, animate);
            return this;
        }
        
        @Override
        public VerticalBarChartBuilder setValuesAxisIntervalsAttributes(Double width, Double height, boolean animate) {
            List<AxisBuilder.AxisLabel> labels = valuesAxisBuilder[0].getLabels();

            double maxLabelWidth = isValuesAxisLabelsPositionLeft() ? getMarginLeft() : getMarginRight();
            double maxLabelHeight  = getChartHeight() / labels.size();
            for (int i = 0; i < labels.size(); i++) {
                AxisBuilder.AxisLabel label = labels.get(i);
                double position = label.getPosition();
                valuesAxisIntervals[i].setPoints(new Point2DArray(new Point2D(0, position), new Point2D(width, position)));
                if (isShowValuesLabels()) {
                    BarChartLabel chartLabel = valuesLabels.get(i);
                    double xPos = 0;
                    if (isValuesAxisLabelsPositionLeft()) {
                        // Left.
                        double marginLeft = getMarginLeft();
                        double lw = chartLabel.getLabelWidth();
                        xPos = (lw + 5 > marginLeft) ? 0 : marginLeft - lw - 5;
                    } else {
                        // Right.
                        xPos = 5;
                    }

                    chartLabel.setAttributes(xPos, position - 5, null, null, animate);
                }
                valuesLabelFormatter.format(maxLabelWidth, maxLabelHeight);
            }
            return this;
        }
        
        private boolean isValuesAxisLabelsPositionLeft() {
            return getValuesAxisLabelsPosition().equals(LabelsPosition.LEFT);
            
        }

        private boolean isCategoriesAxisLabelsPositionTop() {
            return getCategoriesAxisLabelsPosition().equals(LabelsPosition.TOP);

        }

        public VerticalBarChartBuilder setCategoriesAxisIntervalsAttributes(Double width, Double height, boolean animate) {
            if (isShowCategoriesLabels()) {
                List<AxisBuilder.AxisLabel> labels = categoriesAxisBuilder[0].getLabels();

                if (labels != null && !labels.isEmpty()) {
                    // Check max labels size.
                    double maxLabelWidth = getChartWidth() / labels.size();
                    double maxLabelHeight = !isCategoriesAxisLabelsPositionTop() ? getMarginBottom() : getMarginTop();
                    for (int i = 0; i < labels.size(); i++) {
                        AxisBuilder.AxisLabel label = labels.get(i);
                        double position = label.getPosition();
                        BarChartLabel chartLabel = seriesLabels.get(i);
                        chartLabel.setAttributes(position - (maxLabelWidth / 2), 10d, null, null, animate);
                    }
                    categoriesLabelFormatter.format(maxLabelWidth, maxLabelHeight);
                } else {
                    seriesLabels.clear();
                }
            }
            
            return this;
        }

        protected VerticalBarChartBuilder setValuesAttributesForSerie(final XYChartSerie serie, final int numSerie, Double width, Double height, boolean animate) {
            XYChartSerie[] series = getData().getSeries();

            // Rebuild bars for serie values
            List<AxisBuilder.AxisValue> valuesAxisValues = valuesAxisBuilder[0].getValues(serie.getValuesAxisProperty());
            List<AxisBuilder.AxisValue> categoryAxisValues = categoriesAxisBuilder[0].getValues(getData().getCategoryAxisProperty());
            List<Rectangle> bars = seriesValues.get(serie.getName());

            if (categoryAxisValues != null && categoryAxisValues.size() > 0) {
                for (int i = 0; i < categoryAxisValues.size(); i++) {
                    AxisBuilder.AxisValue categoryAxisvalue = categoryAxisValues.get(i);
                    AxisBuilder.AxisValue valueAxisvalue = valuesAxisValues.get(i);
                    double yAxisValuePosition = valueAxisvalue.getPosition();
                    Object yValue = valueAxisvalue.getValue();
                    Object xValue = categoryAxisvalue.getValue();
                    final String yValueFormatted = valuesAxisBuilder[0].format(yValue);
                    final String xValueFormatted = categoriesAxisBuilder[0].format(xValue);

                    // Obtain width and height values for the bar.
                    double barHeight = yAxisValuePosition;
                    double barWidth = getWithForBar(width, series.length, categoryAxisValues.size());
                    if (barWidth <= 0) barWidth = 1;
                    
                    // Calculate bar positions.
                    double y = height - barHeight;
                    double x = (barWidth * series.length * i) + (barWidth * numSerie) + (getValuesAxis().getSegments() * (i +1));
                    double alpha = 1d;

                    // If current bar is not in Y axis intervals (max / min values), resize it and apply an alpha.
                    boolean isOutOfChartArea = y < 0;
                    if (isOutOfChartArea) {
                        alpha = 0.1d;
                        barHeight = getChartHeight();
                        y = 0;
                    }

                    // Obtain the shape instance, add mouse handlers and reposition/resize it.
                    final Rectangle barObject = bars.get(i);
                    barObject.moveToTop();
                    barObject.setDraggable(true);

                    // Mouse events for the bar shape.
                    final int numValue = i;
                    barObject.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                        @Override
                        public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                            double x = barObject.getX();
                            double y = barObject.getY();
                            double width = barObject.getWidth();
                            double height = barObject.getHeight();
                            double xTooltip = x + width/2;
                            double yTooltip = y + height/2;
                            seriesValuesAlpha(numSerie, numValue, 0.5d);
                            tooltip.show(xTooltip, yTooltip, xValueFormatted, yValueFormatted);
                        }
                    });
                    
                    barObject.addNodeMouseExitHandler(new NodeMouseExitHandler() {
                        @Override
                        public void onNodeMouseExit(NodeMouseExitEvent event) {
                            seriesValuesAlpha(numSerie, numValue, 1d);
                            tooltip.hide();
                        }
                    });
                    
                    barObject.addNodeDragEndHandler(new NodeDragEndHandler() {
                        @Override
                        public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                            double x = nodeDragEndEvent.getX();
                            double y = nodeDragEndEvent.getY();
                            if (x < chartArea.getX() || x > (chartArea.getX() + getChartWidth()) ||
                                    y < chartArea.getY() || y > (chartArea.getY() + getChartHeight())) {
                                // Remove the series from data.
                                XYChartData data = getData();
                                data.removeSerie(serie);
                                // Force firing attributes changed event in order to capture it and redraw the chart.
                                setData(data);
                            }
                        }
                    });
                    setShapeAttributes(barObject, x, y, barWidth - BAR_SEPARATION, barHeight, serie.getColor(), alpha, animate);
                }
            }
            return this;
        }

        public VerticalBarChartBuilder reloadBuilders() {
            // Rebuild data summary as columns, series and values can have been modified.
            categoriesAxisBuilder[0].reload(getData(), seriesValues.keySet(), getChartWidth());
            valuesAxisBuilder[0].reload(getData(), seriesValues.keySet(), getChartHeight());
            return this;
        }

        protected double getWithForBar(final double chartWidth, final int numSeries, final int valuesCount) {
            // If exist more than one serie, and no stacked attribute is set, split each serie bar into the series count value.
            return getAvailableWidth(chartWidth, valuesCount) / valuesCount / numSeries;
        }

        protected double getAvailableWidth(final double chartWidth, final int valuesCount) {
            int yAxisDivisions = getValuesAxis().getSegments();
            return chartWidth - (yAxisDivisions * (valuesCount+1) );
        }
        
    }

    private class HorizontalBarChartBuilder extends BarChartBuilder<HorizontalBarChartBuilder> {

        final BarChartLabelFormatter valuesLabelFormatter = new BarChartLabelFormatter(valuesLabels);
        final BarChartLabelFormatter categoriesLabelFormatter = new BarChartLabelFormatter(seriesLabels);

        public HorizontalBarChartBuilder() {
            // Build X axis builder.
            valuesAxisJSO = BarChart.this.getValuesAxis();
            AxisBuilder.AxisDirection direction = isPositiveDirection(getDirection()) ? AxisBuilder.AxisDirection.ASC : AxisBuilder.AxisDirection.DESC;
            if (valuesAxisJSO.getType().equals(Axis.AxisType.CATEGORY)) {
                throw new RuntimeException("CategoryAxis type cannot be used in BarChart (horizontal) for the values axis.");
            } else if (valuesAxisJSO.getType().equals(Axis.AxisType.NUMBER)) {
                valuesAxisBuilder[0] = new NumericAxisBuilder(getData(), getChartWidth(), direction, valuesAxisJSO);
            } else {
                // TODO: yAxisBuilder = new DateAxisBuilder(getData(), yAxisJSO);
            }

            // Build Y axis builder.
            categoriesAxisJSO = BarChart.this.getCategoriesAxis();
            if (categoriesAxisJSO.getType().equals(Axis.AxisType.CATEGORY)) {
                categoriesAxisBuilder[0] = new CategoryAxisBuilder(getData(), getChartHeight(), direction, categoriesAxisJSO);
            } else if (categoriesAxisJSO.getType().equals(Axis.AxisType.NUMBER)) {
                categoriesAxisBuilder[0] = new NumericAxisBuilder(getData(), getChartHeight(), direction, categoriesAxisJSO);
            } else {
                // TODO: xAxisBuilder = new DateAxisBuilder(getData(), xAxisJSO);
            }
        }

        public HorizontalBarChartBuilder buildCategoriesAxisTitle() {
            if (isShowCategoriesAxisTitle()) {
                // Build the X axis line and title.
                categoriesAxisTitle = new Text(getCategoriesAxis().getTitle(), getFontFamily(), getFontStyle(), getFontSize()).setFillColor(ColorName.SILVER).setX(10).setY(getChartHeight() / 2).setTextAlign(TextAlign.RIGHT).setTextBaseLine(TextBaseLine.MIDDLE).setRotationDegrees(270);
                leftArea.add(categoriesAxisTitle);
            }
            return this;
        }

        public HorizontalBarChartBuilder buildValuesAxisTitle() {
            if (isShowValuesAxisTitle()) {
                // Build the Y axis line and title.
                valuesAxisTitle = new Text(getValuesAxis().getTitle(), getFontFamily(), getFontStyle(), getFontSize()).setFillColor(ColorName.SILVER).setX(getChartWidth() / 2).setY(30).setTextAlign(TextAlign.CENTER).setTextBaseLine(TextBaseLine.MIDDLE);
                bottomArea.add(valuesAxisTitle);
            }
            return this;
        }
        
        @Override
        protected void addCategoryAxisIntervalLabel(IPrimitive label) {
            if (isCategoriesAxisLabelsPositionLeft()) leftArea.add(label);
            else rightArea.add(label);
        }

        @Override
        protected void addValuesAxisIntervalLabel(IPrimitive label) {
            if (isValuesAxisLabelsPositionTop()) topArea.add(label);
            else bottomArea.add(label);
        }

        public HorizontalBarChartBuilder setCategoriesAxisTitleAttributes(Double width, Double height, boolean animate) {
            if (categoriesAxisTitle != null) setShapeAttributes(categoriesAxisTitle, null, height / 2, null, null, animate);
            return this;
        }

        public HorizontalBarChartBuilder setValuesAxisTitleAttributes(Double width, Double height, boolean animate) {
            if (valuesAxisTitle != null) setShapeAttributes(valuesAxisTitle, width / 2, 30d, null, null, animate);
            return this;
        }
        
        @Override
        public HorizontalBarChartBuilder setValuesAxisIntervalsAttributes(Double width, Double height, boolean animate) {
            List<AxisBuilder.AxisLabel> labels = valuesAxisBuilder[0].getLabels();

            if (labels != null && !labels.isEmpty()) {
                double maxLabelWidth = getChartWidth() / labels.size();
                double maxLabelHeight = isValuesAxisLabelsPositionTop() ? getMarginTop() : getMarginBottom();
                for (int i = 0; i < labels.size(); i++) {
                    AxisBuilder.AxisLabel label = labels.get(i);
                    double position = label.getPosition();
                    valuesAxisIntervals[i].setPoints(new Point2DArray(new Point2D(position, 0), new Point2D(position, height)));
                    if (isShowValuesLabels()) {
                        BarChartLabel chartLabel = valuesLabels.get(i);
                        double yPos = 0;
                        if (!isValuesAxisLabelsPositionTop()) {
                            // Top.
                            double marginTop = getMarginTop();
                            double lh = chartLabel.getLabelHeight();
                            yPos = (lh + 5 > marginTop) ? 0 : marginTop - lh - 5;
                        } else {
                            // Bottom.
                            yPos = 5;
                        }
                        chartLabel.setAttributes(position, yPos, null, null, animate);
                    }
                }
                valuesLabelFormatter.format(maxLabelWidth, maxLabelHeight);
            }

            return this;
        }

        private boolean isValuesAxisLabelsPositionTop() {
            return getValuesAxisLabelsPosition().equals(LabelsPosition.TOP);

        }

        public HorizontalBarChartBuilder setCategoriesAxisIntervalsAttributes(Double width, Double height, boolean animate) {
            if (isShowCategoriesLabels()) {
                List<AxisBuilder.AxisLabel> labels = categoriesAxisBuilder[0].getLabels();

                if (labels != null && !labels.isEmpty()) {
                    double maxLabelWidth = isCategoriesAxisLabelsPositionLeft() ? getMarginLeft() : getMarginRight();
                    double maxLabelHeight = getChartHeight() / labels.size();
                    for (int i = 0; i < labels.size(); i++) {
                        AxisBuilder.AxisLabel label = labels.get(i);
                        double position = label.getPosition();
                        BarChartLabel chartLabel = seriesLabels.get(i);
                        double xPos = 0;
                        if (isCategoriesAxisLabelsPositionLeft()) {
                            // Left.
                            double margin = getMarginLeft();
                            double lw = chartLabel.getLabelWidth();
                            xPos = (lw + 5 > margin) ? 0 : margin - lw - 5;
                        } else {
                            // Right.
                            xPos = 5;
                        }
                        chartLabel.setAttributes(xPos, position, null, null, animate);
                    }
                    categoriesLabelFormatter.format(maxLabelWidth, maxLabelHeight);
                } else {
                    seriesLabels.clear();
                }
            }

            return this;
        }

        private boolean isCategoriesAxisLabelsPositionLeft() {
            return getCategoriesAxisLabelsPosition().equals(LabelsPosition.LEFT);

        }
        
        protected HorizontalBarChartBuilder setValuesAttributesForSerie(final XYChartSerie serie, final int numSerie, Double width, Double height, boolean animate) {
            XYChartSerie[] series = getData().getSeries();

            // Rebuild bars for serie values
            List<AxisBuilder.AxisValue> yAxisValues = categoriesAxisBuilder[0].getValues(getData().getCategoryAxisProperty());
            List<AxisBuilder.AxisValue> xAxisValues = valuesAxisBuilder[0].getValues(serie.getValuesAxisProperty());
            List<AxisBuilder.AxisLabel> xAxisLabels = valuesAxisBuilder[0].getLabels();
            List<Rectangle> bars = seriesValues.get(serie.getName());

            if (yAxisValues != null && yAxisValues.size() > 0) {
                for (int i = 0; i < yAxisValues.size(); i++) {
                    AxisBuilder.AxisValue xAxisvalue = xAxisValues.get(i);
                    AxisBuilder.AxisValue yAxisvalue = yAxisValues.get(i);

                    double xAxisValuePosition = xAxisvalue.getPosition();
                    Object xValue = xAxisvalue.getValue();
                    Object yValue = yAxisvalue.getValue();
                    final String xValueFormatted = valuesAxisBuilder[0].format(xValue);
                    final String yValueFormatted = categoriesAxisBuilder[0].format(yValue);

                    // Obtain width and height values for the bar.
                    int valuesSize = yAxisValues.size();
                    double barWidth = xAxisValuePosition;
                    double barHeight = getHeightForBar(height, series.length, valuesSize);
                    if (barHeight <= 0) barHeight = 1;
                    
                    // Calculate bar positions.
                    double x = 0;
                    double y = (barHeight * series.length * i) + (barHeight * numSerie);
                    double alpha = 1d;

                    // If current bar is not in Y axis intervals (max / min values), resize it and apply an alpha.
                    double lastXIntervalPosition = xAxisLabels.get(xAxisLabels.size() - 1).getPosition();
                    boolean isOutOfChartArea = barWidth > lastXIntervalPosition;
                    if (isOutOfChartArea) {
                        alpha = 0.1d;
                        barWidth = width;
                    }

                    // Obtain the shape instance, add mouse handlers and reposition/resize it.
                    final Rectangle barObject = bars.get(i);
                    barObject.setDraggable(true);

                    final int numValue = i;
                    barObject.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
                        @Override
                        public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                            double x = barObject.getX();
                            double y = barObject.getY();
                            double width = barObject.getWidth();
                            double height = barObject.getHeight();
                            double xTooltip = x + width/2;
                            double yTooltip = y - height/2;
                            seriesValuesAlpha(numSerie, numValue, 0.5d);
                            tooltip.show(xTooltip, yTooltip, yValueFormatted, xValueFormatted);
                        }
                    });

                    barObject.addNodeMouseExitHandler(new NodeMouseExitHandler() {
                        @Override
                        public void onNodeMouseExit(NodeMouseExitEvent event) {
                            seriesValuesAlpha(numSerie, numValue, 1d);
                            tooltip.hide();
                        }
                    });
                    
                    barObject.addNodeDragEndHandler(new NodeDragEndHandler() {
                        @Override
                        public void onNodeDragEnd(NodeDragEndEvent nodeDragEndEvent) {
                            double x = nodeDragEndEvent.getX();
                            double y = nodeDragEndEvent.getY();
                            if (x < chartArea.getX() || x > (chartArea.getX() + getChartWidth()) ||
                                    y < chartArea.getY() || y > (chartArea.getY() + getChartHeight())) {
                                // Remove the series from data.
                                XYChartData data = getData();
                                data.removeSerie(serie);
                                // Force firing attributes changed event in order to capture it and redraw the chart.
                                setData(data);
                            }
                        }
                    });
                    setShapeAttributes(barObject, x, y, barWidth, barHeight - BAR_SEPARATION, serie.getColor(), alpha, animate);
                }
            }
            return this;
        }

        public HorizontalBarChartBuilder reloadBuilders() {
            // Rebuild data summary as columns, series and values can have been modified.
            categoriesAxisBuilder[0].reload(getData(), seriesValues.keySet(), getChartHeight());
            valuesAxisBuilder[0].reload(getData(), seriesValues.keySet(), getChartWidth());
            return this;
        }

        protected double getHeightForBar(double chartHeight, int numSeries, int valuesCount) {
            // If exist more than one serie, and no stacked attribute is set, split each serie bar into the series count value.
            return chartHeight / valuesCount / numSeries;
        }
        
    }
    
}