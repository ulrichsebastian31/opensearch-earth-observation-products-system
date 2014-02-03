/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.PropertyDisplayCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.grid.AggregationNumberSummaryRenderer;
import com.sencha.gxt.widget.core.client.grid.AggregationRowConfig;
import com.sencha.gxt.widget.core.client.grid.AggregationSafeHtmlRenderer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.grid.SummaryType.AvgSummaryType;
import com.sencha.gxt.widget.core.client.grid.SummaryType.MaxSummaryType;
import com.sencha.gxt.widget.core.client.grid.SummaryType.MinSummaryType;

@Detail(name = "Aggregation Grid", icon = "aggregationrowgrid", category = "Grid", classes = {Stock.class, StockProperties.class})
public class AggregationGridExample implements IsWidget, EntryPoint {

  @Override
  public Widget asWidget() {

    StockProperties props = GWT.create(StockProperties.class);
    final NumberFormat numberFormat = NumberFormat.getFormat("0.00");
    final NumberFormat currency = NumberFormat.getCurrencyFormat();

    List<ColumnConfig<Stock, ?>> configs = new ArrayList<ColumnConfig<Stock, ?>>();

    ColumnConfig<Stock, String> nameColumn = new ColumnConfig<Stock, String>(props.name(), 200, "Company");
    configs.add(nameColumn);

    ColumnConfig<Stock, String> symbolColumn = new ColumnConfig<Stock, String>(props.symbol(), 100, "Symbol");
    configs.add(symbolColumn);

    ColumnConfig<Stock, Double> lastColumn = new ColumnConfig<Stock, Double>(props.last(), 100, "Last");

    lastColumn.setCell(new PropertyDisplayCell<Double>(new DoublePropertyEditor(currency)));
    configs.add(lastColumn);

    ColumnConfig<Stock, Double> changeColumn = new ColumnConfig<Stock, Double>(props.change(), 100, "Change");

    changeColumn.setCell(new PropertyDisplayCell<Double>(new DoublePropertyEditor(numberFormat)) {
      @Override
      public void render(com.google.gwt.cell.client.Cell.Context context, Double value, SafeHtmlBuilder sb) {
        String style = value < 0 ? "red" : "green";
        sb.appendHtmlConstant("<span style='color:" + style + "'>");
        super.render(context, value, sb);
        sb.appendHtmlConstant("</span>");
      }
    });
    configs.add(changeColumn);

    ColumnConfig<Stock, Date> dateColumn = new ColumnConfig<Stock, Date>(props.lastTrans(), 100, "Date");
    dateColumn.setCell(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)));
    configs.add(dateColumn);

    final ListStore<Stock> store = new ListStore<Stock>(props.key());
    store.addAll(TestData.getStocks());

    ColumnModel<Stock> cm = new ColumnModel<Stock>(configs);

    cm.addHeaderGroup(0, 0, new HeaderGroupConfig("Stock Information", 1, 2));
    cm.addHeaderGroup(0, 2, new HeaderGroupConfig("Stock Performance", 1, 2));

    AggregationRowConfig<Stock> averages = new AggregationRowConfig<Stock>();
    averages.setRenderer(nameColumn, new AggregationSafeHtmlRenderer<Stock>("Average"));

    averages.setRenderer(lastColumn, new AggregationNumberSummaryRenderer<Stock, Number>(currency,
        new AvgSummaryType<Number>()));

    averages.setRenderer(changeColumn, new AggregationNumberSummaryRenderer<Stock, Number>(numberFormat,
        new AvgSummaryType<Number>()));
    cm.addAggregationRow(averages);

    AggregationRowConfig<Stock> max = new AggregationRowConfig<Stock>();
    max.setRenderer(nameColumn, new AggregationSafeHtmlRenderer<Stock>("Maximum"));

    max.setRenderer(lastColumn, new AggregationNumberSummaryRenderer<Stock, Number>(currency,
        new MaxSummaryType<Number>()));

    max.setRenderer(changeColumn, new AggregationNumberSummaryRenderer<Stock, Number>(numberFormat,
        new MaxSummaryType<Number>()));
    cm.addAggregationRow(max);

    AggregationRowConfig<Stock> min = new AggregationRowConfig<Stock>();
    min.setRenderer(nameColumn, new AggregationSafeHtmlRenderer<Stock>("Minimum"));

    min.setRenderer(lastColumn, new AggregationNumberSummaryRenderer<Stock, Number>(currency,
        new MinSummaryType<Number>()));

    min.setRenderer(changeColumn, new AggregationNumberSummaryRenderer<Stock, Number>(numberFormat,
        new MinSummaryType<Number>()));
    cm.addAggregationRow(min);

    FramedPanel cp = new FramedPanel();
    cp.setCollapsible(true);
    cp.setAnimCollapse(false);
    cp.setHeadingText("Aggregation Rows");
    cp.setPixelSize(600, 350);
    cp.addStyleName("margin-10");

    Grid<Stock> grid = new Grid<Stock>(store, cm);
    grid.setBorders(true);
    grid.getView().setAutoExpandColumn(nameColumn);
    cp.add(grid);

    return cp;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
