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
package org.dashbuilder.client.sales.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.dashbuilder.displayer.DisplayerSettingsFactory;
import org.dashbuilder.displayer.client.Displayer;
import org.dashbuilder.displayer.client.DataViewerCoordinator;
import org.dashbuilder.displayer.client.DataViewerHelper;
import org.dashbuilder.dataset.DataSetFactory;
import org.dashbuilder.dataset.group.DateIntervalType;

import static org.dashbuilder.client.sales.SalesConstants.*;
import static org.dashbuilder.dataset.sort.SortOrder.*;

/**
 * A composite widget that represents an entire dashboard sample composed using an UI binder template.
 * <p>The dashboard itself is composed by a set of Displayer instances.</p>
 */
public class SalesTableReports extends Composite {

    interface SalesDashboardBinder extends UiBinder<Widget, SalesTableReports>{}
    private static final SalesDashboardBinder uiBinder = GWT.create(SalesDashboardBinder.class);

    @UiField(provided = true)
    Displayer tableByProduct;

    @UiField(provided = true)
    Displayer tableBySalesman;

    @UiField(provided = true)
    Displayer tableByCountry;

    @UiField(provided = true)
    Displayer tableByYear;

    @UiField(provided = true)
    Displayer tableAll;

    public SalesTableReports() {

        // Create the chart definitions

        tableAll = DataViewerHelper.lookup(
                DataSetFactory.newDSLookup()
                .dataset(SALES_OPPS)
                .buildLookup(),
                DisplayerSettingsFactory.newTableSettings()
                .title("List of Opportunities")
                .titleVisible(true)
                .tablePageSize(8)
                .tableOrderEnabled(true)
                .tableOrderDefault(AMOUNT, DESCENDING)
                .column(COUNTRY, "Country")
                .column(CUSTOMER, "Customer")
                .column(PRODUCT, "Product")
                .column(SALES_PERSON, "Salesman")
                .column(STATUS, "Status")
                .column(CREATION_DATE, "Creation")
                .column(EXPECTED_AMOUNT, "Expected")
                .column(CLOSING_DATE, "Closing")
                .column(AMOUNT, "Amount")
                .filterOn(false, true, true)
                .buildDisplayerSettings());

        tableByCountry = DataViewerHelper.lookup(
                DataSetFactory.newDSLookup()
                .dataset(SALES_OPPS)
                .group(COUNTRY, "Country")
                .count("#Opps")
                .min(AMOUNT, "Min")
                .max(AMOUNT, "Max")
                .avg(AMOUNT, "Average")
                .sum(AMOUNT, "Total")
                .buildLookup(),
                DisplayerSettingsFactory.newTableSettings()
                .title("Country summary")
                .titleVisible(false)
                .tablePageSize(8)
                .tableOrderEnabled(true)
                .tableOrderDefault("Total", DESCENDING)
                .filterOn(false, true, true)
                .buildDisplayerSettings());

        tableByProduct = DataViewerHelper.lookup(
                DataSetFactory.newDSLookup()
                .dataset(SALES_OPPS)
                .group(PRODUCT, "Product")
                .count("#Opps")
                .min(AMOUNT, "Min")
                .max(AMOUNT, "Max")
                .avg(AMOUNT, "Average")
                .sum(AMOUNT, "Total")
                .buildLookup(),
                DisplayerSettingsFactory.newTableSettings()
                .title("Product summary")
                .titleVisible(false)
                .tablePageSize(8)
                .tableOrderEnabled(true)
                .tableOrderDefault("Total", DESCENDING)
                .filterOn(false, true, true)
                .buildDisplayerSettings());

        tableBySalesman = DataViewerHelper.lookup(
                DataSetFactory.newDSLookup()
                .dataset(SALES_OPPS)
                .group(SALES_PERSON, "Sales person")
                .count("#Opps")
                .min(AMOUNT, "Min")
                .max(AMOUNT, "Max")
                .avg(AMOUNT, "Average")
                .sum(AMOUNT, "Total")
                .buildLookup(),
                DisplayerSettingsFactory.newTableSettings()
                .title("Sales by person")
                .titleVisible(false)
                .tablePageSize(8)
                .tableOrderEnabled(true)
                .tableOrderDefault("Total", DESCENDING)
                .filterOn(false, true, true)
                .buildDisplayerSettings());

        tableByYear = DataViewerHelper.lookup(
                DataSetFactory.newDSLookup()
                .dataset(SALES_OPPS)
                .group(CREATION_DATE, "Creation date", DateIntervalType.YEAR)
                .count("#Opps")
                .min(AMOUNT, "Min")
                .max(AMOUNT, "Max")
                .avg(AMOUNT, "Average")
                .sum(AMOUNT, "Total")
                .buildLookup(),
                DisplayerSettingsFactory.newTableSettings()
                .title("Year summary")
                .titleVisible(false)
                .tablePageSize(8)
                .tableOrderEnabled(true)
                .tableOrderDefault("Total", DESCENDING)
                .filterOn(false, true, true)
                .buildDisplayerSettings());

        // Make that charts interact among them
        DataViewerCoordinator viewerCoordinator = new DataViewerCoordinator();
        viewerCoordinator.addViewer(tableByCountry);
        viewerCoordinator.addViewer(tableByProduct);
        viewerCoordinator.addViewer(tableBySalesman);
        viewerCoordinator.addViewer(tableByYear);
        viewerCoordinator.addViewer(tableAll);

        // Init the dashboard from the UI Binder template
        initWidget(uiBinder.createAndBindUi(this));

        // Draw the charts
        viewerCoordinator.drawAll();
    }
}
