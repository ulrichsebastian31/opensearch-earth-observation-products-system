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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.BooleanFilter;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

@Detail(name = "Filter Grid", icon = "filtergrid", category = "Grid", classes = {Stock.class, StockProperties.class})
public class FilterGridExample implements IsWidget, EntryPoint {

  private static final StockProperties props = GWT.create(StockProperties.class);

  @Override
  public Widget asWidget() {
    final NumberFormat number = NumberFormat.getFormat("0.00");
    
    ColumnConfig<Stock, String> nameCol = new ColumnConfig<Stock, String>(props.name(), 200, "Company");
    ColumnConfig<Stock, String> symbolCol = new ColumnConfig<Stock, String>(props.symbol(), 100, "Symbol");
    ColumnConfig<Stock, Double> lastCol = new ColumnConfig<Stock, Double>(props.last(), 75, "Last");
    
    ColumnConfig<Stock, Double> changeCol = new ColumnConfig<Stock, Double>(props.change(), 100, "Change");
    changeCol.setCell(new AbstractCell<Double>() {

      @Override
      public void render(Context context, Double value, SafeHtmlBuilder sb) {
        String style = "style='color: " + (value < 0 ? "red" : "green") + "'";
        String v = number.format(value);
        sb.appendHtmlConstant("<span " + style + " qtitle='Change' qtip='" + v + "'>" + v + "</span>");
      }
    });
    
    
    ColumnConfig<Stock, Date> lastTransCol = new ColumnConfig<Stock, Date>(props.lastTrans(), 100, "Last Updated");
    lastTransCol.setCell(new DateCell(DateTimeFormat.getFormat("MM/dd/yyyy")));
    
    ColumnConfig<Stock, Boolean> splitCol = new ColumnConfig<Stock, Boolean>(props.split(), 75, "Split");
    splitCol.setCell(new AbstractCell<Boolean>() {
      @Override
      public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant(value ? "Yes" : "No");
      }
    });
    
    ColumnConfig<Stock, String> typeCol = new ColumnConfig<Stock, String>(props.industry(), 75, "Type");

    List<ColumnConfig<Stock, ?>> l = new ArrayList<ColumnConfig<Stock, ?>>();
    l.add(nameCol);
    l.add(symbolCol);
    l.add(lastCol);
    l.add(changeCol);
    l.add(lastTransCol);
    l.add(splitCol);
    l.add(typeCol);
    
    ColumnModel<Stock> cm = new ColumnModel<Stock>(l);

    ListStore<Stock> store = new ListStore<Stock>(props.key());
    store.addAll(TestData.getStocks());

    ContentPanel cp = new ContentPanel();
    cp.setHeadingText("Filter Grid");
    cp.getHeader().setIcon(ExampleImages.INSTANCE.table());
    cp.setPixelSize(700, 300);
    cp.addStyleName("margin-10");
    
    final Grid<Stock> grid = new Grid<Stock>(store, cm);
    grid.getView().setAutoExpandColumn(nameCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    
    ListStore<String> typeStore = new ListStore<String>(new ModelKeyProvider<String>() {
      @Override
      public String getKey(String item) {
        return item;
      }
    });
    
    typeStore.add("Auto");
    typeStore.add("Media");
    typeStore.add("Medical");
    typeStore.add("Tech");

    NumericFilter<Stock, Double> lastFilter = new NumericFilter<Stock, Double>(props.last(), new DoublePropertyEditor());
    StringFilter<Stock> nameFilter = new StringFilter<Stock>(props.name());
    DateFilter<Stock> dateFilter = new DateFilter<Stock>(props.lastTrans());
    BooleanFilter<Stock> booleanFilter = new BooleanFilter<Stock>(props.split());
    ListFilter<Stock, String> listFilter = new ListFilter<Stock, String>(props.industry(), typeStore);
    
    GridFilters<Stock> filters = new GridFilters<Stock>();
    filters.initPlugin(grid);
    filters.setLocal(true);
    filters.addFilter(lastFilter);
    filters.addFilter(nameFilter);
    filters.addFilter(dateFilter);
    filters.addFilter(booleanFilter);
    filters.addFilter(listFilter);
    
    cp.setWidget(grid);

    return cp;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
