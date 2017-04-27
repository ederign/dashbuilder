/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.dashbuilder.client.summit;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.uberfire.client.mvp.UberElement;

@Dependent
public class IframePresenter {

    private final View view;

    @Inject
    public IframePresenter(final View view) {
        this.view = view;
    }

    public void init() {
        view.load("http://uol.com.br");
    }

    public View getView() {
        return view;
    }

    public interface View extends UberElement<IframePresenter> {

        void load(String url);
    }
}
