/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.NorthSouthContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Overflow ToolBar", icon = "overflowtoolbar", category = "ToolBar & Menu")
public class OverflowToolBarExample implements IsWidget, EntryPoint {

  public Widget asWidget() {
    final Window window = new Window();

    window.setHeadingText("Overflow Example");
    window.setPixelSize(400, 200);
    window.setMinWidth(50);

    ToolBar toolBar = new ToolBar();
    // toolBar.setSpacing(2);
    toolBar.setBorders(true);
    // toolBar.addStyleName(ThemeStyles.getStyle().borderBottom());

    ButtonGroup group = new ButtonGroup();
    group.setHeadingText("Clipboard");
    toolBar.add(group);

    FlexTable table = new FlexTable();
    group.add(table);

    TextButton button = new TextButton("Cool");
    button.setIcon(ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, button);

    button = new TextButton("Copy");
    button.setIcon(ExampleImages.INSTANCE.add16());
    table.setWidget(0, 1, button);

    button = new TextButton("Add");
    button.setIcon(ExampleImages.INSTANCE.user_add());
    table.setWidget(1, 0, button);

    button = new TextButton("Delete");
    button.setIcon(ExampleImages.INSTANCE.user_delete());
    table.setWidget(1, 1, button);

    toolBar.add(new FillToolItem());

    group = new ButtonGroup();
    group.setHeadingText("Other Bogus Actions");
    toolBar.add(group);

    table = new FlexTable();
    group.add(table);

    button = new TextButton("Cool");
    button.setIcon(ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, button);

    button = new TextButton("Copy");
    button.setIcon(ExampleImages.INSTANCE.add16());
    table.setWidget(0, 1, button);

    button = new TextButton("Add");
    button.setIcon(ExampleImages.INSTANCE.user_add());
    table.setWidget(1, 0, button);

    button = new TextButton("Delete");
    button.setIcon(ExampleImages.INSTANCE.user_delete());
    table.setWidget(1, 1, button);

    NorthSouthContainer comp = new NorthSouthContainer();

    // comp.getElement().getStyle().setBackgroundColor(ThemeStyles.getStyle().backgroundColorLight());
    comp.setNorthWidget(toolBar);
    window.add(comp);

    window.setMinButtonWidth(100);
    window.setButtonAlign(BoxLayoutPack.CENTER);
    window.addButton(new TextButton("Save"));
    window.addButton(new TextButton("Cancel"));
    window.addButton(new TextButton("Close"));
    window.addButton(new TextButton("Highlight"));
    window.addButton(new TextButton("Shutdown"));

    TextButton b = new TextButton("ToolBar Overflow Example");
    b.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        window.show();
      }
    });

    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);
    vp.add(b);
    return vp;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
