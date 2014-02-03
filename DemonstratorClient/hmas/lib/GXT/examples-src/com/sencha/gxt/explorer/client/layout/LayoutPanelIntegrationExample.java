/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.layout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "LayoutPanel Integration", icon = "layoutpanel", category = "Layouts")
public class LayoutPanelIntegrationExample implements IsWidget, EntryPoint {

  @Override
  public Widget asWidget() {
    final VerticalLayoutContainer con = new VerticalLayoutContainer();
    con.setPixelSize(400, 300);
    
    ToolBar t = new ToolBar();
    final ToggleButton b1 = new ToggleButton("Toggle Size");
    b1.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        if (b1.getValue()) {
          con.setPixelSize(600, 400);
        } else {
          con.setPixelSize(400, 300);
        }

      }
    });
    t.add(b1);
    con.add(t, new VerticalLayoutData(1, -1));

    ContentPanel cp1 = new ContentPanel();
    cp1.setHeadingText("North");
    ContentPanel cp2 = new ContentPanel();
    cp2.setHeadingText("Center");
    ContentPanel cp3 = new ContentPanel();
    cp3.setHeadingText("East");

    DockLayoutPanel dock = new DockLayoutPanel(Unit.PCT);
    dock.addNorth(cp1, 25);
    dock.addEast(cp3, 15);
    dock.add(cp2);

    con.add(dock, new VerticalLayoutData(1, 1, new Margins(10)));

    con.setBorders(true);
    con.addStyleName("margin-10");
    return con;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
