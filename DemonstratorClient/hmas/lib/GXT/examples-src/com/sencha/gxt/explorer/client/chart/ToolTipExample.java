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
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent.SeriesItemOverHandler;
import com.sencha.gxt.chart.client.chart.series.LineSeries;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.SeriesToolTipConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Data;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "ToolTip Chart", icon = "tooltipchart", category = "Charts", classes = Data.class)
public class ToolTipExample implements IsWidget, EntryPoint {

  public interface DataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<Data, Double> data1();

    ValueProvider<Data, Double> data2();

    ValueProvider<Data, Double> data3();

    ValueProvider<Data, Double> data4();

    ValueProvider<Data, Double> data5();

    ValueProvider<Data, String> name();

    @Path("name")
    ModelKeyProvider<Data> nameKey();
  }

  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);

  public Widget asWidget() {
    final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
    store.addAll(TestData.getData(12, 20, 100));

    final Chart<Data> chart = new Chart<Data>();
    chart.setStore(store);
    chart.setShadowChart(true);
    chart.setDefaultInsets(20);

    NumericAxis<Data> axis = new NumericAxis<Data>();
    axis.setPosition(Position.LEFT);
    axis.addField(dataAccess.data1());
    TextSprite title = new TextSprite("Number of Hits");
    title.setFontSize(18);
    axis.setTitleConfig(title);
    axis.setDisplayGrid(true);
    PathSprite odd = new PathSprite();
    odd.setOpacity(1);
    odd.setFill(new Color("#ddd"));
    odd.setStroke(new Color("#bbb"));
    odd.setStrokeWidth(0.5);
    odd.setZIndex(0);
    axis.setGridOddConfig(odd);
    axis.setMinimum(0);
    axis.setMaximum(100);
    chart.addAxis(axis);

    CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
    catAxis.setPosition(Position.BOTTOM);
    catAxis.setField(dataAccess.name());
    title = new TextSprite("Month of the Year");
    title.setFontSize(18);
    catAxis.setTitleConfig(title);
    catAxis.setLabelProvider(new LabelProvider<String>() {
      @Override
      public String getLabel(String item) {
        return item.substring(0, 3);
      }
    });
    chart.addAxis(catAxis);

    final LineSeries<Data> series = new LineSeries<Data>();
    series.setYAxisPosition(Position.LEFT);
    series.setYField(dataAccess.data1());
    series.setStroke(RGB.RED);
    series.setShowMarkers(true);
    Sprite marker = Primitives.circle(0, 0, 6);
    marker.setFill(RGB.BLUE);
    marker.setZIndex(11);
    series.setMarkerConfig(marker);
    final SeriesToolTipConfig<Data> config = new SeriesToolTipConfig<Data>();
    config.setLabelProvider(null);
    series.setToolTipConfig(config);
    chart.addSeries(series);

    TextButton regenerate = new TextButton("Reload Data");
    regenerate.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        store.clear();
        store.addAll(TestData.getData(12, 20, 100));
        chart.redrawChart();
      }
    });
    ToggleButton animation = new ToggleButton("Animate");
    animation.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setAnimated(event.getValue());
      }
    });
    ToggleButton shadow = new ToggleButton("Shadow");
    shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setShadowChart(event.getValue());
        chart.redrawChart();
      }
    });
    shadow.setValue(true);

    ToolBar toolBar = new ToolBar();
    toolBar.add(regenerate);
    toolBar.add(animation);
    toolBar.add(shadow);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Rich Tool Tip Chart");
    panel.setPixelSize(620, 500);
    panel.setBodyBorder(true);
    
    final Resizable resize = new Resizable(panel, Dir.E, Dir.SE, Dir.S);
    resize.setMinHeight(400);
    resize.setMinWidth(400);
    
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

    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);

    toolBar.setLayoutData(new VerticalLayoutData(1, -1));
    layout.add(toolBar);

    chart.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(chart);

    final ListStore<Data> pieStore = new ListStore<Data>(dataAccess.nameKey());

    final Chart<Data> pieChart = new Chart<Data>(100, 100);
    pieChart.setDefaultInsets(0);
    pieChart.setStore(pieStore);
    pieChart.setBackground(null);
    pieChart.hide();

    final PieSeries<Data> pie = new PieSeries<Data>();
    pie.setAngleField(dataAccess.data1());
    pie.addColor(RGB.GREEN);
    pie.addColor(RGB.BLUE);
    pie.addColor(RGB.PURPLE);
    pie.addColor(RGB.RED);
    pie.addColor(RGB.YELLOW);
    pieChart.addSeries(pie);

    series.addSeriesItemOverHandler(new SeriesItemOverHandler<Data>() {

      @Override
      public void onSeriesOverItem(SeriesItemOverEvent<Data> event) {
        if (pieChart.getElement() != null) {
          int index = event.getIndex();
          pieStore.clear();
          pieStore.add(new Data("data1", dataAccess.data1().getValue(store.get(index)), 0, 0, 0, 0, 0, 0, 0, 0));
          pieStore.add(new Data("data2", dataAccess.data2().getValue(store.get(index)), 0, 0, 0, 0, 0, 0, 0, 0));
          pieStore.add(new Data("data3", dataAccess.data3().getValue(store.get(index)), 0, 0, 0, 0, 0, 0, 0, 0));
          pieStore.add(new Data("data4", dataAccess.data4().getValue(store.get(index)), 0, 0, 0, 0, 0, 0, 0, 0));
          pieStore.add(new Data("data5", dataAccess.data5().getValue(store.get(index)), 0, 0, 0, 0, 0, 0, 0, 0));
          pieChart.redrawChartForced();
          config.setBodyHtml(pieChart.getElement().getInnerHTML());
          series.setToolTipConfig(config);
        }
      }
    });

    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
