/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.layout.border;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(name = "BorderLayout (UiBinder Dynamic Attribute)", icon = "borderlayout", category = "Layouts", fit = true, files = {"BorderLayoutUiBinderDynamicAttributeExample.ui.xml"})
public class BorderLayoutUiBinderDynamicAttributeExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Component, BorderLayoutUiBinderDynamicAttributeExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  BorderLayoutContainer con;

  public Widget asWidget() {
    return uiBinder.createAndBindUi(this);
  }

  @UiFactory
  public FlexTable createFlexTable() {

    FlexTable table = new FlexTable();
    table.getElement().getStyle().setProperty("margin", "10px");
    table.setCellSpacing(8);
    table.setCellPadding(4);

    for (int i = 0; i < LayoutRegion.values().length; i++) {
      final LayoutRegion r = LayoutRegion.values()[i];
      if (r == LayoutRegion.CENTER) {
        continue;
      }

      SelectHandler handler = new SelectHandler() {

        @Override
        public void onSelect(SelectEvent event) {
          TextButton btn = (TextButton) event.getSource();
          String txt = btn.getText();
          if (txt.equals("Expand")) {
            con.expand(r);
          } else if (txt.equals("Collapse")) {
            con.collapse(r);
          } else if (txt.equals("Show")) {
            con.show(r);
          } else {
            con.hide(r);
          }
        }
      };

      table.setHTML(i, 0, "<div style='font-size: 12px; width: 100px'>" + r.name() + ":</span>");
      table.setWidget(i, 1, new TextButton("Expand", handler));
      table.setWidget(i, 2, new TextButton("Collapse", handler));
      table.setWidget(i, 3, new TextButton("Show", handler));
      table.setWidget(i, 4, new TextButton("Hide", handler));
    }
    return table;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
