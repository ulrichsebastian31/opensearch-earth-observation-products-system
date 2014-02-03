/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.dnd;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.examples.resources.client.model.ExampleData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProxy;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

@Detail(name = "Grid to Grid", icon = "gridtogrid", category = "Drag and Drop")
public class GridDndExample implements IsWidget, EntryPoint {

  private VerticalPanel vp;

  interface StockProperties extends PropertyAccess<Stock> {
    @Path("symbol")
    ModelKeyProvider<StockProxy> key();

    @Path("name")
    ValueProvider<StockProxy, String> nameProp();
  }

  @Override
  public Widget asWidget() {
    if (vp == null){
      vp = new VerticalPanel();
      vp.setSpacing(10);

      FramedPanel panel = new FramedPanel();
      panel.setHeadingText("Grid Append Sorted");
      panel.setPixelSize(500, 225);

      HorizontalLayoutContainer con = new HorizontalLayoutContainer();

      StockProperties props = GWT.create(StockProperties.class);
      ListStore<StockProxy> store = new ListStore<StockProxy>(props.key());
      store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));
      store.addAll(ExampleData.getStocks());

      Grid<StockProxy> grid1 = new Grid<StockProxy>(store, createColumnList(props));
      grid1.setBorders(true);
      grid1.getView().setForceFit(true);

      store = new ListStore<StockProxy>(props.key());
      store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));

      Grid<StockProxy> grid2 = new Grid<StockProxy>(store, createColumnList(props));
      grid2.setBorders(true);
      grid2.getView().setForceFit(true);

      new GridDragSource<StockProxy>(grid1);
      new GridDragSource<StockProxy>(grid2);

      new GridDropTarget<StockProxy>(grid1);
      new GridDropTarget<StockProxy>(grid2);

      con.add(grid1, new HorizontalLayoutData(.5, 1, new Margins(5)));
      con.add(grid2, new HorizontalLayoutData(.5, 1, new Margins(5, 5, 5, 0)));

      panel.add(con);
      vp.add(panel);

      panel = new FramedPanel();
      panel.setHeadingText("Grid Insert");
      panel.setPixelSize(500, 225);

      con = new HorizontalLayoutContainer();

      store = new ListStore<StockProxy>(props.key());
      store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));
      store.addAll(ExampleData.getStocks());

      grid1 = new Grid<StockProxy>(store, createColumnList(props));
      grid1.setBorders(true);
      grid1.getView().setForceFit(true);

      store = new ListStore<StockProxy>(props.key());
      store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));

      grid2 = new Grid<StockProxy>(store, createColumnList(props));
      grid2.setBorders(true);
      grid2.getView().setForceFit(true);
      new GridDragSource<StockProxy>(grid1);
      new GridDragSource<StockProxy>(grid2);

      GridDropTarget<StockProxy> target1 = new GridDropTarget<StockProxy>(grid1);
      target1.setFeedback(Feedback.INSERT);

      GridDropTarget<StockProxy> target2 = new GridDropTarget<StockProxy>(grid2);
      target2.setFeedback(Feedback.INSERT);

      con.add(grid1, new HorizontalLayoutData(.5, 1, new Margins(5)));
      con.add(grid2, new HorizontalLayoutData(.5, 1, new Margins(5, 5, 5, 0)));

      panel.add(con);
      vp.add(panel);
    }
    return vp;
  }

  private ColumnModel<StockProxy> createColumnList(StockProperties props) {
    ColumnConfig<StockProxy, String> cc1 = new ColumnConfig<StockProxy, String>(props.nameProp());
    cc1.setHeader(SafeHtmlUtils.fromString("Stock Name"));

    List<ColumnConfig<StockProxy, ?>> l = new ArrayList<ColumnConfig<StockProxy, ?>>();
    l.add(cc1);
    return new ColumnModel<StockProxy>(l);
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

}
