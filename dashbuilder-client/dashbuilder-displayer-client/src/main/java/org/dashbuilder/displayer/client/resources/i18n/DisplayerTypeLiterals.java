/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dashbuilder.displayer.client.resources.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface DisplayerTypeLiterals extends ConstantsWithLookup {

    public static final DisplayerTypeLiterals INSTANCE = GWT.create(DisplayerTypeLiterals.class);

    public static final String DST_PREFIX = "DISPLAYER_SUBTYPE_";

    String displayer_type_selector_tab_bar();

    String displayer_type_selector_tab_pie();

    String displayer_type_selector_tab_line();

    String displayer_type_selector_tab_area();

    String displayer_type_selector_tab_bubble();

    String displayer_type_selector_tab_meter();

    String displayer_type_selector_tab_metric();

    String displayer_type_selector_tab_map();

    String displayer_type_selector_tab_table();


    // Subtype enum literals

    String DISPLAYER_SUBTYPE_LINE();

    String DISPLAYER_SUBTYPE_SMOOTH();

    String DISPLAYER_SUBTYPE_AREA();

    String DISPLAYER_SUBTYPE_STACKED();

    String DISPLAYER_SUBTYPE_STEPPED();

    String DISPLAYER_SUBTYPE_BAR();

    String DISPLAYER_SUBTYPE_COLUMN();

    String DISPLAYER_SUBTYPE_HISTOGRAM();

    String DISPLAYER_SUBTYPE_PIE();

    String DISPLAYER_SUBTYPE_PIE_3D();

    String DISPLAYER_SUBTYPE_DONUT();

    String DISPLAYER_SUBTYPE_MAP_REGIONS();

    String DISPLAYER_SUBTYPE_MAP_MARKERS();

}
