/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.NumberCell;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.RadialAxis;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent.SeriesSelectionHandler;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.RadarSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesHighlighter;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.examples.resources.client.FormattedNumericFilter;
import com.sencha.gxt.examples.resources.client.model.Data;
import com.sencha.gxt.examples.resources.client.model.RadarData;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent.SortChangeHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

@Detail(name = "Dashboard", icon = "dashboard", category = "Charts", classes = {
    Data.class, RadarData.class, FormattedNumericFilter.class})
public class DashboardExample implements IsWidget, EntryPoint {

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

  public interface RadarDataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<RadarData, Double> data();

    ValueProvider<RadarData, String> name();

    @Path("name")
    ModelKeyProvider<RadarData> nameKey();
  }

  private VBoxLayoutContainer vbox;
  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);
  private static final RadarDataPropertyAccess radarDataAccess = GWT.create(RadarDataPropertyAccess.class);
  private final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
  private final ListStore<RadarData> radarStore = new ListStore<RadarData>(radarDataAccess.nameKey());
  private final TextField name = new TextField();
  private final SpinnerField<Double> price = new SpinnerField<Double>(new DoublePropertyEditor());
  private final SpinnerField<Double> revenue = new SpinnerField<Double>(new DoublePropertyEditor());
  private final SpinnerField<Double> growth = new SpinnerField<Double>(new DoublePropertyEditor());
  private final SpinnerField<Double> product = new SpinnerField<Double>(new DoublePropertyEditor());
  private final SpinnerField<Double> market = new SpinnerField<Double>(new DoublePropertyEditor());
  private Grid<Data> grid;
  private Data currentData;
  private Chart<RadarData> radarChart;
  private FramedPanel panel;

  private final static String[] radarLabels = {"Price", "Revenue %", "Growth %", "Product %", "Market %"};

  private final static String[] companies = {
      "3m Co", "Alcoa Inc", "Altria Group Inc", "American Express Company", "American International Group, Inc.",
      "AT&T Inc", "Boeing Co.", "Caterpillar Inc.", "Citigroup, Inc.", "E.I. du Pont de Nemours and Company",
      "Exxon Mobil Corp", "General Electric Company", "General Motors Corporation", "Hewlett-Packard Co",
      "Honeywell Intl Inc", "Intel Corporation", "International Business Machines", "Johnson & Johnson",
      "JP Morgan & Chase & Co", "McDonald\"s Corporation", "Merck & Co., Inc.", "Microsoft Corporation", "Pfizer Inc",
      "The Coca-Cola Company", "The Home Depot, Inc.", "The Procter & Gamble Company",
      "United Technologies Corporation", "Verizon Communications", "Wal-Mart Stores, Inc."};

  @Override
  public Widget asWidget() {
    if (panel == null) {
      for (int i = 0; i < companies.length; i++) {
        store.add(new Data(companies[i], Math.random() * 100, Math.random() * 100, Math.random() * 100,
            Math.random() * 100, Math.random() * 100, 0, 0, 0, 0));
      }

      final Chart<Data> barChart = new Chart<Data>();
      barChart.setStore(store);
      barChart.setShadowChart(true);
      barChart.setAnimated(true);

      CategoryAxis<Data, String> catAxis = new CategoryAxis<Data, String>();
      catAxis.setPosition(Position.BOTTOM);
      catAxis.setField(dataAccess.name());
      TextSprite rotation = new TextSprite();
      rotation.setRotation(270);
      catAxis.setLabelConfig(rotation);
      catAxis.setLabelProvider(new LabelProvider<String>() {
        @Override
        public String getLabel(String item) {
          if (item.length() > 8) {
            return item.substring(0, 8) + "...";
          } else {
            return item;
          }
        }
      });
      barChart.addAxis(catAxis);

      final BarSeries<Data> bar = new BarSeries<Data>();
      bar.setYAxisPosition(Position.LEFT);
      bar.addYField(dataAccess.data1());
      SeriesLabelConfig<Data> barLabelConfig = new SeriesLabelConfig<Data>();
      rotation = rotation.copy();
      rotation.setTextAnchor(TextAnchor.END);
      rotation.setTextBaseline(TextBaseline.MIDDLE);
      barLabelConfig.setSpriteConfig(rotation);
      bar.setLabelConfig(barLabelConfig);
      bar.addColor(new RGB(148, 174, 10));
      bar.setColumn(true);
      bar.setHighlighter(new SeriesHighlighter() {
        @Override
        public void highlight(Sprite sprite) {
          if (sprite instanceof RectangleSprite) {
            RectangleSprite bar = (RectangleSprite) sprite;
            bar.setStroke(new RGB(85, 85, 204));
            bar.setStrokeWidth(3);
            bar.setFill(new RGB("#a2b5ca"));
            bar.redraw();
          }
        }

        @Override
        public void unHighlight(Sprite sprite) {
          if (sprite instanceof RectangleSprite) {
            RectangleSprite bar = (RectangleSprite) sprite;
            bar.setStroke(Color.NONE);
            bar.setStrokeWidth(0);
            bar.setFill(new RGB(148, 174, 10));
            bar.redraw();
          }
        }
      });
      bar.addSeriesSelectionHandler(new SeriesSelectionHandler<Data>() {
        @Override
        public void onSeriesSelection(SeriesSelectionEvent<Data> event) {
          grid.getSelectionModel().select(event.getIndex(), false);
        }
      });
      barChart.addSeries(bar);

      ContentPanel eastPanel = new ContentPanel();
      eastPanel.setHeadingText("Company Details");
      eastPanel.addStyleName("white-bg");
      vbox = new VBoxLayoutContainer();

      name.setReadOnly(true);
      FieldLabel nameLabel = new FieldLabel(name, "Name");

      price.setIncrement(1d);
      price.setMinValue(0d);
      price.setMaxValue(100d);
      price.setAllowBlank(false);
      price.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
      price.addValueChangeHandler(new ValueChangeHandler<Double>() {
        @Override
        public void onValueChange(ValueChangeEvent<Double> event) {
          if (currentData != null) {
            Double value = event.getValue();
            if (value != null) {
              int storeIndex = store.indexOf(currentData);
              currentData.setData1(value);
              store.update(currentData);
              bar.drawSeries();
              bar.highlight(storeIndex);
              updateRadarStore(storeIndex);
              radarChart.redrawChart();
            }
          }
        }
      });
      FieldLabel priceLabel = new FieldLabel(price, "Price $");

      revenue.setIncrement(1d);
      revenue.setMinValue(0d);
      revenue.setMaxValue(100d);
      revenue.setAllowBlank(false);
      revenue.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
      revenue.addValueChangeHandler(new ValueChangeHandler<Double>() {
        @Override
        public void onValueChange(ValueChangeEvent<Double> event) {
          if (currentData != null) {
            Double value = event.getValue();
            if (value != null) {
              int storeIndex = store.indexOf(currentData);
              currentData.setData2(value);
              store.update(currentData);
              updateRadarStore(storeIndex);
              radarChart.redrawChart();
            }
          }
        }
      });
      FieldLabel revenueLabel = new FieldLabel(revenue, "Revenue %");

      growth.setIncrement(1d);
      growth.setMinValue(0d);
      growth.setMaxValue(100d);
      growth.setAllowBlank(false);
      growth.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
      growth.addValueChangeHandler(new ValueChangeHandler<Double>() {
        @Override
        public void onValueChange(ValueChangeEvent<Double> event) {
          if (currentData != null) {
            Double value = event.getValue();
            if (value != null) {
              int storeIndex = store.indexOf(currentData);
              currentData.setData3(value);
              store.update(currentData);
              updateRadarStore(storeIndex);
              radarChart.redrawChart();
            }
          }
        }
      });
      FieldLabel growthLabel = new FieldLabel(growth, "Growth %");

      product.setIncrement(1d);
      product.setMinValue(0d);
      product.setMaxValue(100d);
      product.setAllowBlank(false);
      product.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
      product.addValueChangeHandler(new ValueChangeHandler<Double>() {
        @Override
        public void onValueChange(ValueChangeEvent<Double> event) {
          if (currentData != null) {
            Double value = event.getValue();
            if (value != null) {
              int storeIndex = store.indexOf(currentData);
              currentData.setData4(value);
              store.update(currentData);
              updateRadarStore(storeIndex);
              radarChart.redrawChart();
            }
          }
        }
      });
      FieldLabel productLabel = new FieldLabel(product, "Product %");

      market.setIncrement(1d);
      market.setMinValue(0d);
      market.setMaxValue(100d);
      market.setAllowBlank(false);
      market.getPropertyEditor().setFormat(NumberFormat.getFormat("0.00"));
      market.addValueChangeHandler(new ValueChangeHandler<Double>() {
        @Override
        public void onValueChange(ValueChangeEvent<Double> event) {
          if (currentData != null) {
            Double value = event.getValue();
            if (value != null) {
              int storeIndex = store.indexOf(currentData);
              currentData.setData5(event.getValue());
              store.update(currentData);
              updateRadarStore(storeIndex);
              radarChart.redrawChart();
            }
          }
        }
      });
      FieldLabel marketLabel = new FieldLabel(market, "Market %");

      radarChart = createRadar();
      vbox.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
      vbox.add(nameLabel);
      vbox.add(priceLabel);
      vbox.add(revenueLabel);
      vbox.add(growthLabel);
      vbox.add(productLabel);
      vbox.add(marketLabel);
      vbox.add(radarChart);
      eastPanel.add(vbox, new VerticalLayoutData(1, 1, new Margins(20, 0, 0, 0)));

      ContentPanel centerPanel = new ContentPanel();
      centerPanel.setHeadingText("Company Data");

      List<ColumnConfig<Data, ?>> columns = new ArrayList<ColumnConfig<Data, ?>>();
      columns.add(new ColumnConfig<Data, String>(dataAccess.name(), 120, "Name"));
      ColumnConfig<Data, Double> priceColumnConfig = new ColumnConfig<Data, Double>(dataAccess.data1(), 75, "Price $");
      priceColumnConfig.setCell(new NumberCell<Double>(NumberFormat.getFormat("0.00")));
      columns.add(priceColumnConfig);
      ColumnConfig<Data, Double> revenueColumnConfig = new ColumnConfig<Data, Double>(dataAccess.data2(), 75,
          "Revenue %");
      revenueColumnConfig.setCell(new NumberCell<Double>(NumberFormat.getFormat("0.00")));
      columns.add(revenueColumnConfig);
      ColumnConfig<Data, Double> growthColumnConfig = new ColumnConfig<Data, Double>(dataAccess.data3(), 75, "Growth %");
      growthColumnConfig.setCell(new NumberCell<Double>(NumberFormat.getFormat("0.00")));
      columns.add(growthColumnConfig);
      ColumnConfig<Data, Double> productColumnConfig = new ColumnConfig<Data, Double>(dataAccess.data4(), 75,
          "Product %");
      productColumnConfig.setCell(new NumberCell<Double>(NumberFormat.getFormat("0.00")));
      columns.add(productColumnConfig);
      ColumnConfig<Data, Double> marketColumnConfig = new ColumnConfig<Data, Double>(dataAccess.data5(), 75, "Market %");
      marketColumnConfig.setCell(new NumberCell<Double>(NumberFormat.getFormat("0.00")));
      columns.add(marketColumnConfig);

      grid = new Grid<Data>(store, new ColumnModel<Data>(columns));
      grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      grid.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<Data>() {
        @Override
        public void onBeforeSelection(BeforeSelectionEvent<Data> event) {
          price.finishEditing();
          revenue.finishEditing();
          growth.finishEditing();
          product.finishEditing();
          market.finishEditing();
        }
      });
      grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Data>() {
        @Override
        public void onSelectionChanged(SelectionChangedEvent<Data> event) {
          if (event.getSelection().size() > 0) {
            int index = store.indexOf(event.getSelection().get(0));
            if (currentData != null) {
              bar.unHighlight(store.indexOf(currentData));
              currentData = null;
            }
            if (index >= 0) {
              bar.highlight(index);
              currentData = event.getSelection().get(0);
              // update radar chart
              updateRadarStore(index);
              radarChart.redrawChart();
            }
          }
        }
      });
      grid.addSortChangeHandler(new SortChangeHandler() {
        @Override
        public void onSortChange(SortChangeEvent event) {
          barChart.redrawChart();
        }
      });
      centerPanel.add(grid);

      StringFilter<Data> nameFilter = new StringFilter<Data>(dataAccess.name());
      FormattedNumericFilter data1Filter = new FormattedNumericFilter(dataAccess.data1(), new DoublePropertyEditor(),
          "0.00");
      FormattedNumericFilter data2Filter = new FormattedNumericFilter(dataAccess.data2(), new DoublePropertyEditor(),
          "0.00");
      FormattedNumericFilter data3Filter = new FormattedNumericFilter(dataAccess.data3(), new DoublePropertyEditor(),
          "0.00");
      FormattedNumericFilter data4Filter = new FormattedNumericFilter(dataAccess.data4(), new DoublePropertyEditor(),
          "0.00");
      FormattedNumericFilter data5Filter = new FormattedNumericFilter(dataAccess.data5(), new DoublePropertyEditor(),
          "0.00");

      GridFilters<Data> filters = new GridFilters<Data>();
      filters.initPlugin(grid);
      filters.setLocal(true);
      filters.addFilter(nameFilter);
      filters.addFilter(data1Filter);
      filters.addFilter(data2Filter);
      filters.addFilter(data3Filter);
      filters.addFilter(data4Filter);
      filters.addFilter(data5Filter);
      store.addStoreFilterHandler(new StoreFilterHandler<Data>() {
        @Override
        public void onFilter(StoreFilterEvent<Data> event) {
          barChart.redrawChart();
          currentData = store.get(0);
          updateRadarStore(0);
          radarChart.redrawChart();
        }
      });

      BorderLayoutContainer container = new BorderLayoutContainer();
      BorderLayoutData centerLayoutData = new BorderLayoutData();
      centerLayoutData.setMargins(new Margins(5, 5, 0, 0));
      container.setCenterWidget(centerPanel, centerLayoutData);
      SimpleContainer barChartContainer = new SimpleContainer();
      barChartContainer.add(barChart);
      barChartContainer.setBorders(true);
      container.setNorthWidget(barChartContainer, new BorderLayoutData(200));
      BorderLayoutData eastLayoutData = new BorderLayoutData(330);
      eastLayoutData.setMargins(new Margins(5, 0, 0, 0));
      container.setEastWidget(eastPanel, eastLayoutData);

      panel = new FramedPanel();
      panel.setLayoutData(new BoxLayoutData(new Margins(10)));
      panel.setHeadingText("Company Data");
      panel.setPixelSize(870, 720);
      panel.add(container);
    }
    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private Chart<RadarData> createRadar() {
    currentData = store.get(0);
    updateRadarStore(0);

    final Chart<RadarData> chart = new Chart<RadarData>(320, 320);
    chart.setStore(radarStore);
    chart.setAnimated(true);
    chart.setDefaultInsets(50);

    RadialAxis<RadarData, String> axis = new RadialAxis<RadarData, String>();
    axis.setCategoryField(radarDataAccess.name());
    axis.setSteps(5);
    chart.addAxis(axis);

    RadarSeries<RadarData> radar = new RadarSeries<RadarData>();
    radar.setYField(radarDataAccess.data());
    radar.setFill(new RGB(194, 214, 240));
    radar.setStrokeWidth(0.5);
    radar.setLineRenderer(new SeriesRenderer<RadarData>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<RadarData> store) {
        sprite.setOpacity(0.5);
      }
    });
    radar.setShowMarkers(true);
    Sprite marker = Primitives.circle(0, 0, 4);
    marker.setFill(new RGB(69, 109, 159));
    radar.setMarkerConfig(marker);
    chart.addSeries(radar);

    return chart;
  }

  private void updateRadarStore(int index) {
    radarStore.clear();
    Data data = store.get(index);
    if (data != null) {
      FormPanelHelper.reset(vbox);
      name.setValue(data.getName());
      radarStore.add(new RadarData(radarLabels[0], data.getData1()));
      price.setValue(data.getData1());
      radarStore.add(new RadarData(radarLabels[1], data.getData2()));
      revenue.setValue(data.getData2());
      radarStore.add(new RadarData(radarLabels[2], data.getData3()));
      growth.setValue(data.getData3());
      radarStore.add(new RadarData(radarLabels[3], data.getData4()));
      product.setValue(data.getData4());
      radarStore.add(new RadarData(radarLabels[4], data.getData5()));
      market.setValue(data.getData5());
    }
  }
}
