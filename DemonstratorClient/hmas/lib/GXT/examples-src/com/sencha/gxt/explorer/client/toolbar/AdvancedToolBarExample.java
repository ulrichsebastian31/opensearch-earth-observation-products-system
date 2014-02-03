/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.toolbar;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonArrowAlign;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Advanced ToolBar", icon = "advancedtoolbar", category = "ToolBar & Menu", fit = true)
public class AdvancedToolBarExample implements IsWidget, EntryPoint {

  class SamplePanel extends ContentPanel {

    private VerticalLayoutContainer con = new VerticalLayoutContainer();
    
    private ToolBar toolBar = new ToolBar();

    public SamplePanel() {
      setPixelSize(500, 250);
      toolBar.setSpacing(2);

 

      con.add(toolBar,  new VerticalLayoutData(1, -1));

      HTML text = new HTML(TestData.DUMMY_TEXT_LONG);
      text.getElement().getStyle().setOverflowY(Overflow.AUTO);
      con.add(text, new VerticalLayoutData(1, 1));
      
      add(con);
    }

    public ToolBar getToolBar() {
      return toolBar;
    }
  }

  public Widget asWidget() {
    FlowLayoutContainer con = new FlowLayoutContainer();
    con.getScrollSupport().setScrollMode(ScrollMode.ALWAYS);

    con.add(createStandard(), new MarginData(10));
    con.add(createMulti(), new MarginData(10));
    con.add(createMulti2(), new MarginData(10));
    con.add(createMixed(), new MarginData(10));

    return con;
  };

  private ContentPanel createStandard() {
    SamplePanel panel = new SamplePanel();
    panel.setHeadingText("Standard");

    TextButton btn = new TextButton("Cool", ExampleImages.INSTANCE.add16());
    Menu menu = new Menu();
    menu.add(new MenuItem("Ribbons are cool"));
    btn.setMenu(menu);
    panel.getToolBar().add(btn);

    panel.getToolBar().add(new SeparatorToolItem());

    btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
    menu = new Menu();
    menu.add(new MenuItem("Copy me"));
    panel.getToolBar().add(btn);

    btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
    panel.getToolBar().add(btn);

    btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
    panel.getToolBar().add(btn);
    menu = new Menu();
    menu.add(new MenuItem("Ribbons are cool"));
    btn.setMenu(menu);

    panel.getToolBar().add(new SeparatorToolItem());

    ToggleButton toggleBtn = new ToggleButton("Format");
    toggleBtn.setValue(true);
    panel.getToolBar().add(toggleBtn);

    return panel;
  }

  private ContentPanel createMulti() {
    SamplePanel panel = new SamplePanel();
    panel.setHeadingText("Multi Columns");

    ButtonGroup group = new ButtonGroup();
    group.setHeadingText("Clipboard");
    panel.getToolBar().add(group);

    FlexTable table = new FlexTable();
    group.add(table);

    TextButton btn = new TextButton("Cool", ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, btn);

    btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
    Menu menu = new Menu();
    menu.add(new MenuItem("Copy me"));
    btn.setMenu(menu);
    table.setWidget(0, 1, btn);

    btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 0, btn);
    // //
    btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 1, btn);

    group = new ButtonGroup();
    group.setHeadingText("Other Bogus Actions");
    panel.getToolBar().add(group);

    table = new FlexTable();
    group.add(table);

    btn = new TextButton("Cool", ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, btn);

    btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
    menu = new Menu();
    menu.add(new MenuItem("Copy me"));
    btn.setMenu(menu);
    table.setWidget(0, 1, btn);

    btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 0, btn);
    // //
    btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 1, btn);

    return panel;
  }

  private ContentPanel createMulti2() {
    SamplePanel panel = new SamplePanel();
    panel.setHeadingText("Multi Columns No Title");

    ButtonGroup group = new ButtonGroup();
  
    group.setHeadingText("Clipboard");
    group.setHeaderVisible(false);
   

    FlexTable table = new FlexTable();
    group.add(table);

    TextButton btn = new TextButton("Cool", ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, btn);

    btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
    Menu menu = new Menu();
    menu.add(new MenuItem("Copy me"));
    btn.setMenu(menu);
    table.setWidget(0, 1, btn);

    btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 0, btn);

    btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 1, btn);

    group = new ButtonGroup();
    group.setHeadingText("Other Bogus Actions");
    group.setHeaderVisible(false);
    panel.getToolBar().add(group);

    table = new FlexTable();
    group.add(table);

    btn = new TextButton("Cool", ExampleImages.INSTANCE.add16());
    table.setWidget(0, 0, btn);

    btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
    menu = new Menu();
    menu.add(new MenuItem("Copy me"));
    btn.setMenu(menu);
    table.setWidget(0, 1, btn);

    btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 0, btn);

    btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
    table.setWidget(1, 1, btn);

    panel.getToolBar().add(group);
    
    return panel;
  }

  private ContentPanel createMixed() {
    SamplePanel panel = new SamplePanel();
    panel.setHeadingText("Mix and match icon sizes");

    for (int i = 0; i < 2; i++) {
      ButtonGroup group = new ButtonGroup();
      group.setHeadingText("Clipboard");
      panel.getToolBar().add(group);

      FlexTable table = new FlexTable();
      group.add(table);

      TextButton btn = new TextButton("Paste", ExampleImages.INSTANCE.add32());
      btn.setScale(ButtonScale.LARGE);
      btn.setIconAlign(IconAlign.TOP);
      btn.setArrowAlign(ButtonArrowAlign.BOTTOM);

      table.setWidget(0, 0, btn);
      table.getFlexCellFormatter().setRowSpan(0, 0, 3);

      btn = new TextButton("Format", ExampleImages.INSTANCE.add32());
      btn.setScale(ButtonScale.LARGE);
      btn.setIconAlign(IconAlign.TOP);
      btn.setArrowAlign(ButtonArrowAlign.BOTTOM);

      table.setWidget(0, 1, btn);
      table.getFlexCellFormatter().setRowSpan(0, 1, 3);

      btn = new TextButton("Copy", ExampleImages.INSTANCE.add16());
      Menu menu = new Menu();
      menu.add(new MenuItem("Copy me"));
      btn.setMenu(menu);
      table.setWidget(0, 2, btn);

      btn = new TextButton("Cut", ExampleImages.INSTANCE.add16());
      table.setWidget(1, 2, btn);

      btn = new TextButton("Paste", ExampleImages.INSTANCE.add16());
      table.setWidget(2, 2, btn);

      cleanCells(table.getElement());
    }

    return panel;
  }

  private void cleanCells(Element elem) {
    NodeList<Element> tds = elem.<XElement> cast().select("td");
    for (int i = 0; i < tds.getLength(); i++) {
      Element td = tds.getItem(i);
      
      if (!td.hasChildNodes() && td.getClassName().equals("")) {
        td.removeFromParent();
      }
    }
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
