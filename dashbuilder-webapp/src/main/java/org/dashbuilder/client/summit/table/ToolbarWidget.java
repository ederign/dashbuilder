package org.dashbuilder.client.summit.table;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.ListItem;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Dependent
@Templated
public class ToolbarWidget implements IsElement {

    @Inject
    @DataField
    ListItem all;

    @Inject
    @DataField
    Anchor allLink;

    @Inject
    @DataField
    ListItem full;

    @Inject
    @DataField
    Anchor fullLink;

    @Inject
    @DataField
    ListItem front;

    @Inject
    @DataField
    Anchor frontLink;

    @Inject
    @DataField
    ListItem back;

    @Inject
    @DataField
    Anchor backLink;

    private HRTable hrTable;

    @PostConstruct
    public void setup() {
        fullLink.setOnclick(e -> processClick(full,
                                              "FULL"));
        allLink.setOnclick(e -> processClick(all,
                                             ""));
        frontLink.setOnclick(e -> processClick(front,
                                               "FRONT"));
        backLink.setOnclick(e -> processClick(back,
                                              "BACK"));
    }

    private void processClick(ListItem selectedList,
                              String selected) {
        cleanup();
        DOMUtil.addCSSClass(selectedList,
                            "selected");
        hrTable.filter(selected);
    }

    private void cleanup() {
        DOMUtil.removeCSSClass(full,
                               "selected");
        DOMUtil.removeCSSClass(all,
                               "selected");
        DOMUtil.removeCSSClass(front,
                               "selected");
        DOMUtil.removeCSSClass(back,
                               "selected");
    }

    public void init(HRTable hrTable) {
        this.hrTable = hrTable;
    }
}
