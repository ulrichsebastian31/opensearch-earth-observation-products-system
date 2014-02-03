/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.dnd;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.examples.resources.client.model.ExampleData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProxy;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;

@Detail(name = "List to List", icon = "listtolist", category = "Drag and Drop")
public class ListViewDndExample implements IsWidget, EntryPoint {

  interface StockProperties extends PropertyAccess<Stock> {
    @Path("symbol")
    ModelKeyProvider<StockProxy> key();

    @Path("name")
    ValueProvider<StockProxy, String> nameProp();
  }

  @Override
  public Widget asWidget() {
    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);

    FramedPanel panel = new FramedPanel();
    panel.setHeadingText("ListView Append Sorted");
    panel.setPixelSize(500, 225);

    HorizontalLayoutContainer con = new HorizontalLayoutContainer();

    StockProperties props = GWT.create(StockProperties.class);
    ListStore<StockProxy> store = new ListStore<StockProxy>(props.key());
    store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));
    store.addAll(ExampleData.getStocks());
    ListView<StockProxy, String> list1 = new ListView<StockProxy, String>(store, props.nameProp());

    store = new ListStore<StockProxy>(props.key());
    store.addSortInfo(new StoreSortInfo<StockProxy>(props.nameProp(), SortDir.ASC));

    ListView<StockProxy, String> list2 = new ListView<StockProxy, String>(store, props.nameProp());
    list2.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

    new ListViewDragSource<StockProxy>(list1);
    new ListViewDragSource<StockProxy>(list2);

    new ListViewDropTarget<StockProxy>(list1);
    new ListViewDropTarget<StockProxy>(list2);

    con.add(list1, new HorizontalLayoutData(.5, 1, new Margins(5)));
    con.add(list2, new HorizontalLayoutData(.5, 1, new Margins(5, 5, 5, 0)));

    panel.add(con);
    vp.add(panel);

    panel = new FramedPanel();
    panel.setHeadingText("ListView Insert");
    panel.setPixelSize(500, 225);

    con = new HorizontalLayoutContainer();

    props = GWT.create(StockProperties.class);
    store = new ListStore<StockProxy>(props.key());

    store.addAll(ExampleData.getStocks());

    list1 = new ListView<StockProxy, String>(store, props.nameProp());
    
    store = new ListStore<StockProxy>(props.key());

    list2 = new ListView<StockProxy, String>(store, props.nameProp());

    new ListViewDragSource<StockProxy>(list1);
    new ListViewDragSource<StockProxy>(list2);

    ListViewDropTarget<StockProxy> target1 = new ListViewDropTarget<StockProxy>(list1);
    target1.setFeedback(Feedback.INSERT);

    ListViewDropTarget<StockProxy> target2 = new ListViewDropTarget<StockProxy>(list2);
    target2.setFeedback(Feedback.INSERT);

    con.add(list1, new HorizontalLayoutData(.5, 1, new Margins(5)));
    con.add(list2, new HorizontalLayoutData(.5, 1, new Margins(5, 5, 5, 0)));

    panel.add(con);
    vp.add(panel);

    return vp;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

}
