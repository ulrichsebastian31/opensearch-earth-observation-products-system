/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.misc;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.examples.resources.client.Resources;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;

@Detail(name = "Draggable (UiBinder)", icon = "draggable", category = "Misc", fit = true, files = "DraggableUiBinderExample.ui.xml")
public class DraggableUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Widget, DraggableUiBinderExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField(provided = true)
  String dummyTextShort = TestData.DUMMY_TEXT_SHORT;

  @UiField
  ContentPanel proxy;
  @UiField
  ContentPanel direct;
  @UiField
  ContentPanel vertical;

  public Widget asWidget() {
    Widget widget = uiBinder.createAndBindUi(this);

    proxy.getHeader().setIcon(Resources.IMAGES.text());
    direct.getHeader().setIcon(Resources.IMAGES.text());
    vertical.getHeader().setIcon(Resources.IMAGES.text());

    new Draggable(proxy);
    new Draggable(direct, direct.getHeader()).setUseProxy(false);
    new Draggable(vertical, vertical.getHeader()).setConstrainHorizontal(true);

    return widget;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
