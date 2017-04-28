package org.dashbuilder.client.summit.iframe;

import org.dashbuilder.client.summit.table.HRTable;

public class ReloadIframeEvent {

    private HRTable.Row row;

    public ReloadIframeEvent(HRTable.Row row) {

        this.row = row;
    }

    public HRTable.Row getRow() {
        return row;
    }
}
