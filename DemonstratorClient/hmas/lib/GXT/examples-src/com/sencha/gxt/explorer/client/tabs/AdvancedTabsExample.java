/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.tabs;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(name = "Advanced Tabs", category = "Tabs", icon = "advancedtabs")
public class AdvancedTabsExample implements IsWidget, EntryPoint {

  private VerticalPanel vp;
  private TabPanel advanced;
  private int index = 0;

  public Widget asWidget() {
    if (vp == null) {
      vp = new VerticalPanel();
      vp.setSpacing(10);

      HorizontalPanel hp = new HorizontalPanel();
      hp.setSpacing(5);

      TextButton add = new TextButton("Add Tab");
      add.addSelectHandler(new SelectHandler() {

        @Override
        public void onSelect(SelectEvent event) {
          addTab();
          advanced.setActiveWidget(advanced.getWidget(advanced.getWidgetCount() - 1));
        }
      });
      hp.add(add);

      ToggleButton toggle = new ToggleButton("Enable Tab Context Menu");

      hp.add(toggle);
      vp.add(hp);

      advanced = new TabPanel();
      advanced.setPixelSize(600, 250);
      advanced.setAnimScroll(true);
      advanced.setTabScroll(true);
      advanced.setCloseContextMenu(true);

      toggle.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          advanced.setCloseContextMenu(event.getValue());
        }
      });
      toggle.setValue(true);

      while (index < 7) {
        addTab();
      }

      advanced.setActiveWidget(advanced.getWidget(6));

      vp.add(advanced);
    }
    return vp;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private void addTab() {
    Label item = new Label("Tab Body " + (index + 1));
    item.addStyleName("pad-text");
    advanced.add(item, new TabItemConfig("New Tab " + ++index, index != 1));
  }

}
