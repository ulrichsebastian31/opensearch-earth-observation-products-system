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
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Direction;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.FxElement;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent;
import com.sencha.gxt.fx.client.animation.AfterAnimateEvent.AfterAnimateHandler;
import com.sencha.gxt.fx.client.animation.Fx;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

@Detail(name = "Fx (UiBinder)", icon = "fx", category = "Misc", fit = true, files = "FxUiBinderExample.ui.xml")
public class FxUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<Widget, FxUiBinderExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  ContentPanel cp;

  public Widget asWidget() {
    return uiBinder.createAndBindUi(this);
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  @UiHandler("slide")
  public void onSlide(SelectEvent event) {
    if (cp.isVisible()) {
      cp.getElement().<FxElement> cast().slideOut(Direction.UP);
    } else {
      cp.getElement().<FxElement> cast().slideIn(Direction.DOWN);
    }
  }

  @UiHandler("fade")
  public void onFade(SelectEvent event) {
    cp.getElement().<FxElement> cast().fadeToggle();
  }

  @UiHandler("move")
  public void onMove(SelectEvent event) {
    Rectangle bounds = cp.getElement().getBounds();
    cp.getElement().<FxElement> cast().setXY(bounds.getX() + 50, bounds.getY() + 50, new Fx());
  }

  @UiHandler("blink")
  public void onBlink(SelectEvent event) {
    cp.getElement().<FxElement> cast().blink();
  }

  @UiHandler("event")
  public void onEvent(SelectEvent event) {
    Fx fx = new Fx();
    fx.addAfterAnimateHandler(new AfterAnimateHandler() {
      @Override
      public void onAfterAnimate(AfterAnimateEvent event) {
        if (cp.isCollapsed()) {
          cp.expand();
        } else {
          cp.collapse();
        }
      }
    });
    cp.getElement().<FxElement> cast().blink(fx, 50);
  }

  @UiHandler("reset")
  public void onReset(SelectEvent event) {
    cp.setPosition(100, 100);
  }
}
