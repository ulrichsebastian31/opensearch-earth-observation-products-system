/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.AreaSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelProvider;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.model.BrowserData;
import com.sencha.gxt.examples.resources.client.model.BrowserProxy;
import com.sencha.gxt.examples.resources.client.model.ExampleData;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Area Renderer Chart", icon = "arearendererchart", category = "Charts")
public class AreaRendererExample implements IsWidget, EntryPoint {

  interface BrowserProperties extends PropertyAccess<BrowserData> {

    ValueProvider<BrowserProxy, Double> Chrome();

    ValueProvider<BrowserProxy, String> date();

    ValueProvider<BrowserProxy, Double> Firefox();

    ValueProvider<BrowserProxy, Double> IE();

    @Path("date")
    ModelKeyProvider<BrowserProxy> nameKey();

    ValueProvider<BrowserProxy, Double> Opera();

    ValueProvider<BrowserProxy, Double> Other();

    ValueProvider<BrowserProxy, Double> Safari();
  }

  @Override
  public Widget asWidget() {

    BrowserProperties props = GWT.create(BrowserProperties.class);

    final ListStore<BrowserProxy> store = new ListStore<BrowserProxy>(props.nameKey());
    store.addAll(ExampleData.getBrowserData());

    final Chart<BrowserProxy> chart = new Chart<BrowserProxy>(500, 500);
    chart.setStore(store);

    NumericAxis<BrowserProxy> axis = new NumericAxis<BrowserProxy>();
    axis.setPosition(Position.LEFT);
    axis.addField(props.Chrome());
    axis.addField(props.Firefox());
    axis.addField(props.IE());
    axis.addField(props.Opera());
    axis.addField(props.Other());
    axis.addField(props.Safari());
    TextSprite title = new TextSprite("Usage %");
    axis.setDisplayGrid(true);
    title.setFontSize(18);
    axis.setTitleConfig(title);
    chart.addAxis(axis);

    CategoryAxis<BrowserProxy, String> catAxis = new CategoryAxis<BrowserProxy, String>();
    catAxis.setPosition(Position.BOTTOM);
    catAxis.setField(props.date());
    TextSprite sprite = new TextSprite();
    sprite.setRotation(315);
    catAxis.setLabelPadding(-10);
    catAxis.setLabelConfig(sprite);
    catAxis.setLabelTolerance(50);
    chart.addAxis(catAxis);

    Gradient area1 = new Gradient("area1", 0);
    area1.addStop(new Stop(0, new RGB("#2fa2df")));
    area1.addStop(new Stop(100, new RGB("#1d86be")));
    chart.addGradient(area1);

    Gradient area2 = new Gradient("area2", 0);
    area2.addStop(new Stop(0, new RGB("#3c852e")));
    area2.addStop(new Stop(100, new RGB("#2b5f21")));
    chart.addGradient(area2);

    Gradient area3 = new Gradient("area3", 0);
    area3.addStop(new Stop(0, new RGB("#ea6611")));
    area3.addStop(new Stop(100, new RGB("#ba510e")));
    chart.addGradient(area3);

    Gradient area4 = new Gradient("area4", 0);
    area4.addStop(new Stop(0, new RGB("#9ab0d5")));
    area4.addStop(new Stop(100, new RGB("#7694c6")));
    chart.addGradient(area4);

    Gradient area5 = new Gradient("area5", 0);
    area5.addStop(new Stop(0, new RGB("#ba0a19")));
    area5.addStop(new Stop(100, new RGB("#8a0712")));
    chart.addGradient(area5);

    Gradient area6 = new Gradient("area6", 0);
    area6.addStop(new Stop(0, new RGB("#282828")));
    area6.addStop(new Stop(100, new RGB("#0e0e0e")));
    chart.addGradient(area6);

    AreaSeries<BrowserProxy> series = new AreaSeries<BrowserProxy>();
    series.setHighlighting(true);
    series.setYAxisPosition(Position.LEFT);
    series.addYField(props.IE());
    series.addYField(props.Chrome());
    series.addYField(props.Firefox());
    series.addYField(props.Safari());
    series.addYField(props.Opera());
    series.addYField(props.Other());
    series.addColor(area1);
    series.addColor(area2);
    series.addColor(area3);
    series.addColor(area4);
    series.addColor(area5);
    series.addColor(area6);
    series.setStroke(new Color("#666"));
    SeriesToolTipConfig<BrowserProxy> toolTip = new SeriesToolTipConfig<BrowserProxy>();
    toolTip.setTrackMouse(true);
    toolTip.setHideDelay(200);
    toolTip.setLabelProvider(new SeriesLabelProvider<BrowserProxy>() {

      @Override
      public String getLabel(BrowserProxy item, ValueProvider<? super BrowserProxy, ? extends Number> valueProvider) {
        return valueProvider.getPath() + " - " + item.getDate() + " - " + valueProvider.getValue(item) + "%";
      }
    });
    series.setToolTipConfig(toolTip);
    PathSprite highlightLine = new PathSprite();
    highlightLine.setHidden(true);
    highlightLine.addCommand(new MoveTo(0, 0));
    highlightLine.setZIndex(1000);
    highlightLine.setStrokeWidth(5);
    highlightLine.setStroke(new RGB("#444"));
    highlightLine.setOpacity(0.3);
    series.setHighlightLineConfig(highlightLine);
    series.setRenderer(new SeriesRenderer<BrowserProxy>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<BrowserProxy> store) {
        sprite.setOpacity(0.86);
        sprite.redraw();
      }
    });
    chart.addSeries(series);

    Legend<BrowserProxy> legend = new Legend<BrowserProxy>();
    legend.setPosition(Position.RIGHT);
    legend.setItemHiding(true);
    chart.setLegend(legend);

    ToggleButton animation = new ToggleButton("Animate");
    animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setAnimated(event.getValue());
      }
    });
    animation.setValue(true, true);

    ToolBar toolBar = new ToolBar();
    toolBar.add(animation);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("What is the trend in Browser Usage?");
    panel.setPixelSize(800, 600);

    final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
    resize.setMinHeight(600);
    resize.setMinWidth(600);

    panel.addExpandHandler(new ExpandHandler() {
      @Override
      public void onExpand(ExpandEvent event) {
        resize.setEnabled(true);
      }
    });
    panel.addCollapseHandler(new CollapseHandler() {
      @Override
      public void onCollapse(CollapseEvent event) {
        resize.setEnabled(false);
      }
    });

    new Draggable(panel, panel.getHeader()).setUseProxy(false);
    panel.setBodyBorder(true);

    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);

    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    layout.add(toolBar);

    chart.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(chart);

    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
