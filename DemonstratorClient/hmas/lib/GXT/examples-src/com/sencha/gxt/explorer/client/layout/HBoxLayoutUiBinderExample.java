/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.layout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

@Detail(name = "HBoxLayout (UiBinder)", icon = "hboxlayout", category = "Layouts", files = {"HBoxLayoutUiBinderExample.ui.xml"})
public class HBoxLayoutUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Widget, HBoxLayoutUiBinderExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  VBoxLayoutContainer buttonBox;
  @UiField
  CardLayoutContainer layout;

  @UiField
  ToggleButton spaced, multiSpaced, alignTop, alignMiddle, alignBottom, alignStretch, alignStretchMax, flexAllEven,
      flexRatio, flexStretch, packStart, packCenter, packEnd;

  public Widget asWidget() {
    Widget widget = uiBinder.createAndBindUi(this);

    ToggleGroup toggleGroup = new ToggleGroup();
    toggleGroup.add(spaced);
    toggleGroup.add(multiSpaced);
    toggleGroup.add(alignTop);
    toggleGroup.add(alignMiddle);
    toggleGroup.add(alignBottom);
    toggleGroup.add(alignStretch);
    toggleGroup.add(alignStretchMax);
    toggleGroup.add(flexAllEven);
    toggleGroup.add(flexRatio);
    toggleGroup.add(flexStretch);
    toggleGroup.add(packStart);
    toggleGroup.add(packCenter);
    toggleGroup.add(packEnd);
    return widget;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  @UiHandler({
      "spaced", "multiSpaced", "alignTop", "alignMiddle", "alignBottom", "alignStretch", "alignStretchMax",
      "flexAllEven", "flexRatio", "flexStretch", "packStart", "packCenter", "packEnd"})
  public void buttonClicked(SelectEvent event) {
    ToggleButton button = (ToggleButton) event.getSource();

    int index = buttonBox.getWidgetIndex(button);
    layout.setActiveWidget(layout.getWidget(index + 1));
  }
}
