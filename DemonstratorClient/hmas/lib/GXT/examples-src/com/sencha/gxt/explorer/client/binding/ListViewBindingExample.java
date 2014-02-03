/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.binding;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.binding.ListViewBindingExample.StockExchange;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Demonstrates using the ListStoreEditor, and some concept of building multiple
 * editors. Note that as currently written, when a Stock object is saved, it
 * will modify the StockExchange's instances, instead of cloning models before
 * editing them.
 */
@Detail(name = "List View Binding", icon = "listviewbinding", category = "Binding", classes = {Stock.class, StockProperties.class, StockEditor.class})
public class ListViewBindingExample implements EntryPoint, IsWidget, Editor<StockExchange> {
  public static class StockExchange {
    private List<Stock> stocks = TestData.getStocks();

    public List<Stock> getStocks() {
      return stocks;
    }

    public void setStocks(List<Stock> stocks) {
      this.stocks = stocks;
    }
  }

  interface ListDriver extends SimpleBeanEditorDriver<StockExchange, ListViewBindingExample> {
  }

  interface StockDriver extends SimpleBeanEditorDriver<Stock, StockEditor> {
  }

  private ListDriver driver = GWT.create(ListDriver.class);

  private StockDriver itemDriver = GWT.create(StockDriver.class);

  private FramedPanel panel;

  ListView<Stock, String> stockList;
  ListStoreEditor<Stock> stocks;

  @Ignore
  StockEditor stockEditor;

  @Override
  public Widget asWidget() {
    if (panel == null) {
      panel = new FramedPanel();
      panel.setHeadingText("ListView Binding");
      panel.setPixelSize(300, 300);
      panel.setBodyBorder(false);

      panel.addStyleName("margin-10");

      VerticalLayoutContainer c = new VerticalLayoutContainer();

      final StockProperties props = GWT.create(StockProperties.class);

      stockList = new ListView<Stock, String>(new ListStore<Stock>(props.key()), props.name());
      stockList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

      stocks = new ListStoreEditor<Stock>(stockList.getStore());

      c.add(stockList, new VerticalLayoutData(1, 1));
      stockList.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Stock>() {
        @Override
        public void onSelectionChanged(SelectionChangedEvent<Stock> event) {
          if (event.getSelection().size() > 0) {
            edit(event.getSelection().get(0));
          } else {
            stockEditor.setSaveEnabled(false);
          }
        }
      });

      stockEditor = new StockEditor();

      stockEditor.getSaveButton().addSelectHandler(new SelectHandler() {

        @Override
        public void onSelect(SelectEvent event) {
          saveCurrentStock();

        }
      });
      c.add(stockEditor, new VerticalLayoutData(1, -1, new Margins(5)));
      panel.add(c);

      itemDriver.initialize(stockEditor);
      driver.initialize(this);
    }

    driver.edit(new StockExchange());
    return panel;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  protected void edit(Stock stock) {
    itemDriver.edit(stock);
    stockEditor.setSaveEnabled(true);
  }

  protected void saveCurrentStock() {
    Stock edited = itemDriver.flush();
    if (!itemDriver.hasErrors()) {
      stockEditor.setSaveEnabled(false);

      stockList.getStore().update(edited);
    }
  }
}
