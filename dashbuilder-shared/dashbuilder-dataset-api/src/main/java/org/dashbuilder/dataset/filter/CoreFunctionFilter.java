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
package org.dashbuilder.dataset.filter;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * A core filter function definition
 */
@Portable
public class CoreFunctionFilter extends ColumnFilter {

    protected CoreFunctionType type = null;
    protected List parameters = new ArrayList();

    public CoreFunctionFilter() {
    }

    public CoreFunctionFilter(String columnId, CoreFunctionType type, List parameters) {
        super(columnId);
        this.type = type;
        this.setParameters(parameters);
    }

    public CoreFunctionFilter(String columnId, CoreFunctionType type, Comparable... parameters) {
        super(columnId);
        this.type = type;
        this.setParameters(parameters);
    }

    public CoreFunctionType getType() {
        return type;
    }

    public void setType(CoreFunctionType type) {
        this.type = type;
    }

    public List getParameters() {
        return parameters;
    }

    public void setParameters(Comparable... parameters) {
        this.parameters.clear();
        for (Comparable param : parameters) {
            this.parameters.add(param);
        }
    }

    public void setParameters(List parameters) {
        this.parameters = parameters;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        if (CoreFunctionType.BETWEEN.equals(type)) {
            out.append(columnId).append(" between (");
            appendParameters(out);
            out.append(")");
        }
        else if (CoreFunctionType.GREATER_THAN.equals(type)) {
            out.append(columnId).append(" > ");
            appendParameters(out);
        }
        else if (CoreFunctionType.GREATER_OR_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" >= ");
            appendParameters(out);
        }
        else if (CoreFunctionType.LOWER_THAN.equals(type)) {
            out.append(columnId).append(" < ");
            appendParameters(out);
        }
        else if (CoreFunctionType.LOWER_OR_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" <= ");
            appendParameters(out);
        }
        else if (CoreFunctionType.EQUALS_TO.equals(type)) {
            out.append(columnId).append(" = ");
            appendParameters(out);
        }
        else if (CoreFunctionType.NOT_EQUALS_TO.equals(type)) {
            out.append(columnId).append(" != ");
            appendParameters(out);
        }
        else if (CoreFunctionType.IS_NULL.equals(type)) {
            out.append(columnId).append(" is_null ");
            appendParameters(out);
        }
        else if (CoreFunctionType.NOT_NULL.equals(type)) {
            out.append(columnId).append(" not_null ");
            appendParameters(out);
        }
        else if (CoreFunctionType.TIME_FRAME.equals(type)) {
            out.append(columnId).append(" time_frame ");
            appendParameters(out);
        }
        return out.toString();
    }

    private StringBuilder appendParameters(StringBuilder out) {
        for (int i=0; i< parameters.size();  i++) {
            if (i > 0) out.append(", ");
            out.append(parameters.get(i));
        }
        return out;
    }

    public ColumnFilter cloneInstance() {
        CoreFunctionFilter clone = new CoreFunctionFilter();
        clone.columnId = columnId;
        clone.type = type;
        clone.parameters.addAll(parameters);
        return clone;
    }

    public boolean equals(Object obj) {
        try {
            CoreFunctionFilter other = (CoreFunctionFilter) obj;
            if (!super.equals(other)) return false;

            if (type != null && !type.equals(other.type)) return false;
            if (parameters.size() != other.parameters.size()) return false;
            for (Object param : parameters) {
                if (!other.parameters.contains(param)) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
