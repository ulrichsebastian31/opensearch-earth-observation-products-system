/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.RoundNumberProvider;
import com.sencha.gxt.chart.client.chart.series.Primitives;
import com.sencha.gxt.chart.client.chart.series.ScatterSeries;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.DrawFx;
import com.sencha.gxt.chart.client.draw.HSV;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Data;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.fx.client.animation.Animator;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Scatter Renderer Chart", icon = "scatterrendererchart", category = "Charts", classes = {
    Data.class, DrawFx.class})
public class ScatterRendererExample implements IsWidget, EntryPoint {

  public interface DataPropertyAccess extends PropertyAccess<Data> {
    ValueProvider<Data, Double> data1();

    ValueProvider<Data, Double> data2();

    ValueProvider<Data, Double> data3();

    ValueProvider<Data, String> name();

    @Path("name")
    ModelKeyProvider<Data> nameKey();
  }

  private static final DataPropertyAccess dataAccess = GWT.create(DataPropertyAccess.class);
  private ScatterSeries<Data> series = new ScatterSeries<Data>();
  private ValueProvider<Data, Double> radiusField;
  private ValueProvider<Data, Double> colorField;
  private ValueProvider<Data, Double> grayField;
  private double maxRadius = 50;
  private RGB minColor = new RGB(250, 20, 20);
  private RGB maxColor = new RGB(127, 0, 220);
  private RGB minGray = new RGB(20, 20, 20);
  private RGB maxGray = new RGB(220, 220, 220);
  private ArrayList<RGB> colors = new ArrayList<RGB>();
  private ArrayList<RGB> grays = new ArrayList<RGB>();
  private Chart<Data> chart;

  public Widget asWidget() {
    // set up colors
    colors.add(new RGB(250, 20, 20));
    colors.add(new RGB(20, 250, 20));
    colors.add(new RGB(20, 20, 250));
    colors.add(new RGB(127, 0, 240));
    colors.add(new RGB(213, 70, 121));
    colors.add(new RGB(44, 153, 201));
    colors.add(new RGB(146, 6, 157));
    colors.add(new RGB(49, 149, 0));
    colors.add(new RGB(249, 153, 0));

    grays.add(new RGB(20, 20, 20));
    grays.add(new RGB(80, 80, 80));
    grays.add(new RGB(120, 120, 120));
    grays.add(new RGB(180, 180, 180));
    grays.add(new RGB(220, 220, 220));
    grays.add(new RGB(250, 250, 250));

    final ListStore<Data> store = new ListStore<Data>(dataAccess.nameKey());
    store.addAll(TestData.getData(12, 0, 100));

    chart = new Chart<Data>();
    chart.setStore(store);
    chart.setDefaultInsets(30);
    chart.setShadowChart(true);

    series.setXField(dataAccess.data1());
    series.setYField(dataAccess.data2());
    Sprite marker = Primitives.circle(0, 0, 20);
    marker.setFill(RGB.BLUE);
    series.setMarkerConfig(marker);
    TextSprite textConfig = new TextSprite();
    textConfig.setFill(new RGB("#333"));
    textConfig.setTextAnchor(TextAnchor.MIDDLE);
    SeriesLabelConfig<Data> labelConfig = new SeriesLabelConfig<Data>();
    labelConfig.setSpriteConfig(textConfig);
    labelConfig.setLabelContrast(true);
    labelConfig.setValueProvider(dataAccess.data3(), new RoundNumberProvider<Double>());
    series.setLabelConfig(labelConfig);
    chart.addSeries(series);

    TextButton regenerate = new TextButton("Reload Data");
    regenerate.addSelectHandler(new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        store.clear();
        store.addAll(TestData.getData(12, 0, 100));
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
    animation.setValue(true, true);
    ToggleButton shadow = new ToggleButton("Shadow");
    shadow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        chart.setShadowChart(event.getValue());
        chart.redrawChart();
      }
    });
    shadow.setValue(true);

    TextButton xAxis = new TextButton("Select X Axis");
    Menu xAxisMenu = new Menu();
    MenuItem xAxisData1 = new MenuItem("data1");
    xAxisData1.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setXField(dataAccess.data1());
        series.drawSeries();
      }
    });
    xAxisMenu.add(xAxisData1);
    MenuItem xAxisData2 = new MenuItem("data2");
    xAxisData2.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setXField(dataAccess.data2());
        series.drawSeries();
      }
    });
    xAxisMenu.add(xAxisData2);
    MenuItem xAxisData3 = new MenuItem("data3");
    xAxisData3.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setXField(dataAccess.data3());
        series.drawSeries();
      }
    });
    xAxisMenu.add(xAxisData3);
    xAxis.setMenu(xAxisMenu);

    TextButton yAxis = new TextButton("Select Y Axis");
    Menu yAxisMenu = new Menu();
    MenuItem yAxisData1 = new MenuItem("data1");
    yAxisData1.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setYField(dataAccess.data1());
        series.drawSeries();
      }
    });
    yAxisMenu.add(yAxisData1);
    MenuItem yAxisData2 = new MenuItem("data2");
    yAxisData2.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setYField(dataAccess.data2());
        series.drawSeries();
      }
    });
    yAxisMenu.add(yAxisData2);
    MenuItem yAxisData3 = new MenuItem("data3");
    yAxisData3.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        series.setYField(dataAccess.data3());
        series.drawSeries();
      }
    });
    yAxisMenu.add(yAxisData3);
    yAxis.setMenu(yAxisMenu);

    TextButton color = new TextButton("Select Color");
    Menu colorMenu = new Menu();
    MenuItem colorData1 = new MenuItem("data1");
    colorData1.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        colorField = dataAccess.data1();
        grayField = null;
        refresh();
      }
    });
    colorMenu.add(colorData1);
    MenuItem colorData2 = new MenuItem("data2");
    colorData2.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        colorField = dataAccess.data2();
        grayField = null;
        refresh();
      }
    });
    colorMenu.add(colorData2);
    MenuItem colorData3 = new MenuItem("data3");
    colorData3.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        colorField = dataAccess.data3();
        grayField = null;
        refresh();
      }
    });
    colorMenu.add(colorData3);
    MenuItem colorFrom = new MenuItem("Color From");
    Menu colorFromMenu = new Menu();
    for (int i = 0; i < colors.size(); i++) {
      final int index = i;
      MenuItem colorFromItem = new MenuItem(colors.get(index).toString());
      colorFromItem.addActivateHandler(new ActivateHandler<Item>() {
        @Override
        public void onActivate(ActivateEvent<Item> event) {
          minColor = colors.get(index);
          refresh();
        }
      });
      colorFromMenu.add(colorFromItem);
    }
    colorFrom.setSubMenu(colorFromMenu);
    colorMenu.add(colorFrom);
    MenuItem colorTo = new MenuItem("Color To");
    Menu colorToMenu = new Menu();
    for (int i = 0; i < colors.size(); i++) {
      final int index = i;
      MenuItem colorToItem = new MenuItem(colors.get(index).toString());
      colorToItem.addActivateHandler(new ActivateHandler<Item>() {
        @Override
        public void onActivate(ActivateEvent<Item> event) {
          maxColor = colors.get(index);
          refresh();
        }
      });
      colorToMenu.add(colorToItem);
    }
    colorTo.setSubMenu(colorToMenu);
    colorMenu.add(colorTo);
    color.setMenu(colorMenu);

    TextButton grayscale = new TextButton("Select GrayScale");
    Menu grayMenu = new Menu();
    MenuItem grayData1 = new MenuItem("data1");
    grayData1.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        grayField = dataAccess.data1();
        colorField = null;
        refresh();
      }
    });
    grayMenu.add(grayData1);
    MenuItem grayData2 = new MenuItem("data2");
    grayData2.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        grayField = dataAccess.data2();
        colorField = null;
        refresh();
      }
    });
    grayMenu.add(grayData2);
    MenuItem grayData3 = new MenuItem("data3");
    grayData3.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        grayField = dataAccess.data3();
        colorField = null;
        refresh();
      }
    });
    grayMenu.add(grayData3);
    MenuItem grayFrom = new MenuItem("Gray From");
    Menu grayFromMenu = new Menu();
    for (int i = 0; i < grays.size(); i++) {
      final int index = i;
      MenuItem grayFromItem = new MenuItem(grays.get(index).toString());
      grayFromItem.addActivateHandler(new ActivateHandler<Item>() {
        @Override
        public void onActivate(ActivateEvent<Item> event) {
          minGray = grays.get(index);
          refresh();
        }
      });
      grayFromMenu.add(grayFromItem);
    }
    grayFrom.setSubMenu(grayFromMenu);
    grayMenu.add(grayFrom);
    MenuItem grayTo = new MenuItem("Gray To");
    Menu grayToMenu = new Menu();
    for (int i = 0; i < grays.size(); i++) {
      final int index = i;
      MenuItem grayToItem = new MenuItem(grays.get(index).toString());
      grayToItem.addActivateHandler(new ActivateHandler<Item>() {
        @Override
        public void onActivate(ActivateEvent<Item> event) {
          maxGray = grays.get(index);
          refresh();
        }
      });
      grayToMenu.add(grayToItem);
    }
    grayTo.setSubMenu(grayToMenu);
    grayMenu.add(grayTo);
    grayscale.setMenu(grayMenu);

    TextButton radius = new TextButton("Select Radius");
    Menu radiusMenu = new Menu();
    MenuItem radiusData1 = new MenuItem("data1");
    radiusData1.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        radiusField = dataAccess.data1();
        refresh();
      }
    });
    radiusMenu.add(radiusData1);
    MenuItem radiusData2 = new MenuItem("data2");
    radiusData2.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        radiusField = dataAccess.data2();
        refresh();
      }
    });
    radiusMenu.add(radiusData2);
    MenuItem radiusData3 = new MenuItem("data3");
    radiusData3.addSelectionHandler(new SelectionHandler<Item>() {
      @Override
      public void onSelection(SelectionEvent<Item> event) {
        radiusField = dataAccess.data3();
        refresh();
      }
    });
    radiusMenu.add(radiusData3);
    MenuItem maximumRadius = new MenuItem("Max Radius");
    Menu maximumRadiusMenu = new Menu();
    MenuItem radius20 = new MenuItem("20");
    radius20.addActivateHandler(new ActivateHandler<Item>() {
      @Override
      public void onActivate(ActivateEvent<Item> event) {
        maxRadius = 20;
        refresh();
      }
    });
    maximumRadiusMenu.add(radius20);
    MenuItem radius30 = new MenuItem("30");
    radius30.addActivateHandler(new ActivateHandler<Item>() {
      @Override
      public void onActivate(ActivateEvent<Item> event) {
        maxRadius = 30;
        refresh();
      }
    });
    maximumRadiusMenu.add(radius30);
    MenuItem radius40 = new MenuItem("40");
    radius40.addActivateHandler(new ActivateHandler<Item>() {
      @Override
      public void onActivate(ActivateEvent<Item> event) {
        maxRadius = 40;
        refresh();
      }
    });
    maximumRadiusMenu.add(radius40);
    MenuItem radius50 = new MenuItem("50");
    radius50.addActivateHandler(new ActivateHandler<Item>() {
      @Override
      public void onActivate(ActivateEvent<Item> event) {
        maxRadius = 50;
        refresh();
      }
    });
    maximumRadiusMenu.add(radius50);
    MenuItem radius60 = new MenuItem("60");
    radius60.addActivateHandler(new ActivateHandler<Item>() {
      @Override
      public void onActivate(ActivateEvent<Item> event) {
        maxRadius = 60;
        refresh();
      }
    });
    maximumRadiusMenu.add(radius60);
    maximumRadius.setSubMenu(maximumRadiusMenu);
    radiusMenu.add(maximumRadius);
    radius.setMenu(radiusMenu);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Scatter Renderer Chart");
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
    
    ToolBar toolBar = new ToolBar();
    toolBar.add(regenerate);
    toolBar.add(animation);
    toolBar.add(shadow);
    toolBar.add(xAxis);
    toolBar.add(yAxis);
    toolBar.add(color);
    toolBar.add(grayscale);
    toolBar.add(radius);

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

  private SeriesRenderer<Data> color(final ValueProvider<Data, Double> fieldName, final RGB minColor,
      final RGB maxColor, final double minValue, final double maxValue) {
    final HSV min = new HSV(minColor);
    final HSV max = new HSV(maxColor);
    return new SeriesRenderer<Data>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<Data> store) {
        double value = fieldName.getValue(store.get(index));
        double delta1 = delta(minValue, maxValue, min.getHue(), max.getHue(), value);
        double delta2 = delta(minValue, maxValue, min.getSaturation(), max.getSaturation(), value);
        double delta3 = delta(minValue, maxValue, min.getValue(), max.getValue(), value);
        if(chart.isAnimated()) {
          createFillAnimator(sprite, new RGB(new HSV((int) delta1, (int) delta2, (int) delta3))).run(500);
        } else {
          sprite.setFill(new RGB(new HSV((int) delta1, (int) delta2, (int) delta3)));
          sprite.redraw();
        }
      }
    };
  }

  private Animator createFillAnimator(final Sprite sprite, RGB color) {
    if (!(sprite.getFill() instanceof RGB)) {
      return null;
    }

    RGB origin = (RGB) sprite.getFill();
    final int originR = origin.getRed();
    final int originG = origin.getGreen();
    final int originB = origin.getBlue();
    final int deltaR = color.getRed() - originR;
    final int deltaG = color.getGreen() - originG;
    final int deltaB = color.getBlue() - originB;

    return new Animator() {
      @Override
      protected void onUpdate(double progress) {
        sprite.setFill(new RGB(originR + (int) (deltaR * progress), originG + (int) (deltaG * progress), originB
            + (int) (deltaB * progress)));
        sprite.redraw();
      }
    };
  }

  private Animator createRadiusAnimator(final CircleSprite sprite, double radius) {
    final double origin = sprite.getRadius();
    final double delta = radius - origin;
    return new Animator() {
      @Override
      protected void onUpdate(double progress) {
        sprite.setRadius(origin + (delta * progress));
        sprite.redraw();
      }
    };
  }

  private double delta(double x, double y, double a, double b, double theta) {
    return a + (b - a) * (y - theta) / (y - x);
  }

  private SeriesRenderer<Data> grayscale(final ValueProvider<Data, Double> fieldName, final RGB minColor,
      final RGB maxColor, final double minValue, final double maxValue) {
    return new SeriesRenderer<Data>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<Data> store) {
        double value = fieldName.getValue(store.get(index));
        double ans = delta(minValue, maxValue, minColor.getGreen(), maxColor.getGreen(), value);
        if(chart.isAnimated()) {
          createFillAnimator(sprite, new RGB((int) Math.round(ans), (int) Math.round(ans), (int) Math.round(ans))).run(
            500);
        } else {
          sprite.setFill(new RGB((int) Math.round(ans), (int) Math.round(ans), (int) Math.round(ans)));
          sprite.redraw();
        }
      }
    };
  }

  private SeriesRenderer<Data> radius(final ValueProvider<Data, Double> fieldName, final double minRadius,
      final double maxRadius, final double minValue, final double maxValue) {
    return new SeriesRenderer<Data>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<Data> store) {
        double scale = delta(maxValue, minValue, maxRadius, minRadius, fieldName.getValue(store.get(index)));
        scale = maxRadius - scale + minRadius;
        if(chart.isAnimated()) {
          createRadiusAnimator((CircleSprite) sprite, scale).run(500);
        } else {
          ((CircleSprite) sprite).setRadius(scale);
          sprite.redraw();
        }
      }
    };
  }

  private void refresh() {

    series.setRenderer(new SeriesRenderer<Data>() {
      @Override
      public void spriteRenderer(Sprite sprite, int index, ListStore<Data> store) {
        if (colorField != null) {
          color(colorField, minColor, maxColor, 0, 100).spriteRenderer(sprite, index, store);
        }
        if (grayField != null) {
          grayscale(grayField, minGray, maxGray, 0, 100).spriteRenderer(sprite, index, store);
        }
        if (radiusField != null) {
          radius(radiusField, 10, maxRadius, 0, 100).spriteRenderer(sprite, index, store);
        }
      }
    });
    if(radiusField != null) {
      series.setShadowRenderer(radius(radiusField, 10, maxRadius, 0, 100));
    }

    series.drawSeries();
  }
}
