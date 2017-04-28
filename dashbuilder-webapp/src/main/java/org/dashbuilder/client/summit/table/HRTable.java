package org.dashbuilder.client.summit.table;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import org.dashbuilder.client.summit.iframe.ReloadIframeEvent;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.common.client.ui.ElementWrapperWidget;
import org.uberfire.ext.widgets.table.client.UberfirePagedTable;
import org.uberfire.paging.AbstractPageRow;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Dependent
public class HRTable extends Composite implements RequiresResize {


    @Inject
    private ApplicantServiceImpl imp;

    @Inject
    Event<ReloadIframeEvent> reloadIframeEvent;

    @Inject
    ToolbarWidget toolbarWidget;

    private static final int PADDING = 30;

    protected final UberfirePagedTable<Row> dataGrid = new UberfirePagedTable<Row>( 10, null, true, true, true );
    protected final FlowPanel panel = new FlowPanel();
    protected final Button addButton = new Button();
    protected final List<Row> data = new ArrayList<Row>();
    protected final AsyncDataProvider<Row> dataProvider = new AsyncDataProvider<Row>() {
        @Override
        protected void onRangeChanged( final HasData<Row> display ) {
            final ColumnSortList columnSortList = dataGrid.getColumnSortList();
            Collections.sort( data, new Comparator<Row>() {
                @Override
                public int compare( final Row o1, final Row o2 ) {
                    if ( columnSortList == null || columnSortList.size() == 0 || columnSortList.get( 0 )
                            .isAscending() == false ) {
                        return o1.getName().compareTo( o2.getName() );
                    } else {
                        return o2.getName().compareTo( o1.getName() );
                    }
                }
            } );
            Scheduler.get().scheduleFixedDelay( new Scheduler.RepeatingCommand() {
                @Override
                public boolean execute() {
                    updateRowCount( data.size(), true );
                    updateRowData( 0, data );
                    return false;
                }
            }, 1000 );

        }
    };

    @PostConstruct
    public void init() {
        dataGrid.setEmptyTableCaption( "No data" );

        final Column<Row, String> nameColumn = new Column<Row, String>( new TextCell() ) {
            @Override
            public String getValue( Row row ) {
                return row.getName();
            }

            @Override
            public void onBrowserEvent( Cell.Context context, Element elem,
                                        Row object, NativeEvent event) {
                super.onBrowserEvent(context, elem, object, event);
                if ("click".equals(event.getType())) {
                    GWT.log("Click on " + object.getName());
                }
            }
        };
        final Column<Row, String> ageColumn = new Column<Row, String>( new TextCell() ) {
            @Override
            public String getValue( Row row ) {
                return row.getAge();
            }
        };

        final Column<Row, String> countryColumn = new Column<Row, String>( new TextCell() ) {
            @Override
            public String getValue( Row row ) {
                return row.getCountry();
            }
        };

        ButtonCell hireButton = new ButtonCell();
        Column<Row,String> hireColumn = new Column<Row,String>(hireButton) {
            public String getValue(Row object) {
                return "Hire";
            }
        };
        hireColumn.setFieldUpdater(new FieldUpdater<Row, String>() {
            @Override
            public void update(int index, Row object, String value) {
                Window.alert("Hire " + object.getName());
            }
        });

        ButtonCell detailButton = new ButtonCell();

        Column<Row,String> detailCol = new Column<Row,String>(detailButton) {
            public String getValue(Row object) {
                return "Detail";
            }
        };
        detailCol.setFieldUpdater(new FieldUpdater<Row, String>() {
            @Override
            public void update(int index, Row object, String value) {
                Window.alert("Detail " + object.getName());
                reloadIframeEvent.fire(new ReloadIframeEvent(object));
            }
        });


        nameColumn.setSortable( true );
        dataGrid.addColumn( nameColumn, "Name" );
        dataGrid.addColumn( ageColumn, "Age" );
        dataGrid.addColumn( countryColumn, "Country" );
        dataGrid.addColumn( detailCol, "Detail" );
        dataGrid.addColumn( hireColumn, "Hire" );

        addButton.setText( "New Row" );
        addButton.setIcon( IconType.PLUS );
        addButton.getElement().getStyle().setMarginLeft( 10, Style.Unit.PX );
        addButton.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
//                data.add( new Row( data.size() ) );
                dataGrid.refresh();
            }
        } );
        dataProvider.addDataDisplay( dataGrid );

        dataGrid.addColumnSortHandler( new ColumnSortEvent.AsyncHandler( dataGrid ) );

        toolbarWidget.init(this);
        panel.add(ElementWrapperWidget.getWidget(toolbarWidget.getElement() ));
        panel.add( dataGrid );
//        panel.add( addButton );
        initWidget( panel );

    }

    public void filter( String filter ) {
        data.clear();
        imp.filterApplicants( filter ).forEach( p -> {
            data.add( new Row( p.getName(), String.valueOf( p.getAge() ), p.getCountry() ) );
            dataGrid.refresh();

        } );
    }


    public class Row extends AbstractPageRow {


        private final String name;
        private final String age;
        private final String country;

        public Row( String name, String age, String country ) {
            this.name = name;
            this.age = age;
            this.country = country;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getCountry() {
            return country;
        }

        @Override
        public int compareTo( AbstractPageRow o ) {
            return getName().compareTo( ( ( Row ) o ).getName() );
        }
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight() - PADDING;
        int width = getParent().getOffsetWidth() - PADDING;
        setPixelSize( width, height );
    }

}