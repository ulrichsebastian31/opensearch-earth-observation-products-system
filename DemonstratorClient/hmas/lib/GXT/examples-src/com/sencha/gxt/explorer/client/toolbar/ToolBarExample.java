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
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.Resources;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.FramedPanel.FramedPanelAppearance;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.ColorMenu;
import com.sencha.gxt.widget.core.client.menu.DateMenu;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Basic ToolBar", icon = "basictoolbar", category = "ToolBar & Menu")
public class ToolBarExample implements IsWidget, EntryPoint {

  interface StockProperties extends PropertyAccess<Stock> {
    ModelKeyProvider<Stock> id();

    LabelProvider<Stock> name();
  }

  @Override
  public Widget asWidget() {
    SelectionHandler<Item> handler = new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        MenuItem item = (MenuItem) event.getSelectedItem();
        Info.display("Action", "You selected the " + item.getText());
      }
    };

    ToolBar toolBar = new ToolBar();

    TextButton item1 = new TextButton("Button w/ Menu");
    item1.setIcon(Resources.IMAGES.menu_show());

    StockProperties props = GWT.create(StockProperties.class);
    ListStore<Stock> store = new ListStore<Stock>(props.id());
    store.addAll(TestData.getStocks());

    final ComboBox<Stock> combo = new ComboBox<Stock>(store, props.name());
    combo.setName("name");
    combo.setForceSelection(true);
    combo.setStore(store);
    combo.setTriggerAction(TriggerAction.ALL);

    Menu menu = new Menu();
    menu.addSelectionHandler(handler);
    menu.add(combo);

    CheckMenuItem menuItem = new CheckMenuItem("I Like Cats");
    menuItem.setChecked(true);
    menu.add(menuItem);

    menuItem = new CheckMenuItem("I Like Dogs");
    menu.add(menuItem);
    item1.setMenu(menu);

    menu.add(new SeparatorMenuItem());

    MenuItem radios = new MenuItem("Radio Options");
    menu.add(radios);

    Menu radioMenu = new Menu();
    radioMenu.addSelectionHandler(handler);

    CheckMenuItem r = new CheckMenuItem("Blue Theme");
    r.setGroup("radios");
    r.setChecked(true);
    radioMenu.add(r);

    r = new CheckMenuItem("Gray Theme");
    r.setGroup("radios");
    radioMenu.add(r);
    radios.setSubMenu(radioMenu);

    MenuItem date = new MenuItem("Choose a Date");
    date.setIcon(Resources.IMAGES.calendar());
    menu.add(date);

    final DateMenu dateMenu = new DateMenu();
    dateMenu.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        Date d = event.getValue();
        DateTimeFormat f = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
        Info.display("Value Changed", "You selected " + f.format(d));
        dateMenu.hide(true);
      }
    });
    date.setSubMenu(dateMenu);

    MenuItem color = new MenuItem("Choose a Color");
    menu.add(color);

    final ColorMenu colorMenu = new ColorMenu();
    colorMenu.getPalette().addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String color = event.getValue();
        Info.display("Color Changed", "You selected " + color);
        colorMenu.hide(true);
      }
    });

    color.setSubMenu(colorMenu);

    toolBar.add(item1);

    toolBar.add(new SeparatorToolItem());

    SplitButton splitItem = new SplitButton("Split Button");
    splitItem.setIcon(Resources.IMAGES.list_items());

    menu = new Menu();
    menu.addSelectionHandler(handler);

    MenuItem item = new MenuItem();
    item.setHTML("<b>Bold</b>");
    menu.add(item);

    item = new MenuItem();
    item.setHTML("<i>Italic</i>");
    menu.add(item);

    item = new MenuItem();
    item.setHTML("<u>Underline</u>");
    menu.add(item);

    splitItem.setMenu(menu);

    toolBar.add(splitItem);

    toolBar.add(new SeparatorToolItem());

    ToggleButton toggle = new ToggleButton("Toggle");
    toggle.setValue(true);
    toolBar.add(toggle);

    toolBar.add(new SeparatorToolItem());

    TextButton scrollerButton = new TextButton("Scrolling Menu");

    Menu scrollMenu = new Menu();
    scrollMenu.addSelectionHandler(handler);
    scrollMenu.setMaxHeight(200);
    for (int i = 0; i < 40; i++) {
      scrollMenu.add(new MenuItem("Item " + i));
    }

    scrollerButton.setMenu(scrollMenu);

    toolBar.add(scrollerButton);

    toolBar.add(new SeparatorToolItem());
    toolBar.add(new FillToolItem());

    // ThemeSelector selector = new ThemeSelector();
    // toolBar.add(selector);

    ContentPanel panel = new ContentPanel(GWT.<ContentPanelAppearance> create(FramedPanelAppearance.class));
    panel.setCollapsible(true);
    panel.setHeadingText("ToolBar & Menu Demo");
    panel.setPixelSize(550, 300);

    VerticalLayoutContainer p = new VerticalLayoutContainer();
    p.setBorders(true);
    p.getElement().getStyle().setBackgroundColor("white");
    panel.add(p);

    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    p.add(toolBar);

    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);
    vp.add(panel);
    return vp;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
