/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.toolbar;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.ColorMenu;
import com.sencha.gxt.widget.core.client.menu.DateMenu;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

@Detail(name = "Basic ToolBar (UiBinder)", icon = "basictoolbar", category = "ToolBar & Menu", files = "ToolBarUiBinderExample.ui.xml")
public class ToolBarUiBinderExample implements IsWidget, EntryPoint {

  interface StockProperties extends PropertyAccess<Stock> {
    ModelKeyProvider<Stock> id();

    LabelProvider<Stock> name();
  }

  interface MyUiBinder extends UiBinder<Widget, ToolBarUiBinderExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  Menu scrollMenu;
  @UiField
  DateMenu dateMenu;
  @UiField
  ColorMenu colorMenu;

  StockProperties props = GWT.create(StockProperties.class);

  public Widget asWidget() {
    Widget component = uiBinder.createAndBindUi(this);

    for (int i = 0; i < 40; i++) {
      scrollMenu.add(new MenuItem("Item " + i));
    }

    dateMenu.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        Date d = event.getValue();
        DateTimeFormat f = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
        Info.display("Value Changed", "You selected " + f.format(d));
        dateMenu.hide(true);
      }
    });

    colorMenu.getPalette().addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String color = event.getValue();
        Info.display("Color Changed", "You selected " + color);
        colorMenu.hide(true);
      }
    });

    return component;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  @UiFactory
  public ComboBox<Stock> createComboBox() {
    ListStore<Stock> store = new ListStore<Stock>(props.id());
    store.addAll(TestData.getStocks());
    ComboBox<Stock> combo = new ComboBox<Stock>(store, props.name());
    combo.setName("name");
    combo.setForceSelection(true);
    combo.setTriggerAction(TriggerAction.ALL);
    return combo;
  }

  @UiHandler({"menu1", "splitButtonMenu"})
  public void onMenuSelection(SelectionEvent<Item> event) {
    MenuItem item = (MenuItem) event.getSelectedItem();
    Info.display("Action", "You selected the " + item.getText());
  }
}
