/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.draw;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.Rotation;
import com.sencha.gxt.chart.client.draw.Scaling;
import com.sencha.gxt.chart.client.draw.path.CurveTo;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.EllipseSprite;
import com.sencha.gxt.chart.client.draw.sprite.ImageSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

@Detail(name = "Basic Draw", icon = "basicdraw", category = "Draw")
public class BasicDrawExample implements IsWidget, EntryPoint {

  @Override
  public Widget asWidget() {
    DrawComponent component = new DrawComponent();

    TextSprite text = new TextSprite();
    text.setText("Hello\nWorld!");
    text.setX(10);
    text.setY(25);
    text.setFont("Helvetica");
    text.setFontSize(18);
    text.setFill(RGB.BLACK);
    component.addSprite(text);

    CircleSprite circle = new CircleSprite();
    circle.setCenterX(120);
    circle.setCenterY(100);
    circle.setRadius(25);
    Scaling scale = new Scaling();
    scale.setX(2);
    scale.setY(2);
    circle.setScaling(scale);
    circle.setStroke(new Color("#999"));
    Gradient gradient = new Gradient("gradient", 21);
    gradient.addStop(0, new Color("#79A933"));
    gradient.addStop(13, new Color("#70A333"));
    gradient.addStop(34, new Color("#559332"));
    gradient.addStop(58, new Color("#277B2F"));
    gradient.addStop(86, new Color("#005F27"));
    gradient.addStop(100, new Color("#005020"));
    circle.setFill(gradient);
    component.addGradient(gradient);
    circle.setStrokeWidth(3);
    component.addSprite(circle);

    RectangleSprite rect = new RectangleSprite();
    rect.setX(125);
    rect.setY(75);
    Rotation rotate = new Rotation();
    rotate.setDegrees(45);
    rotate.setX(125 + 50 / 2);
    rotate.setY(75 + 50 / 2);
    rect.setRotation(rotate);
    rect.setWidth(50);
    rect.setHeight(50);
    rect.setRadius(10);
    rect.setFill(new Color("#bf292f"));
    component.addSprite(rect);

    PathSprite path = new PathSprite();
    path.addCommand(new MoveTo(75, 75));
    path.addCommand(new CurveTo(0, -25, 50, 25, 50, 0, true));
    path.addCommand(new CurveTo(0, -25, -50, 25, -50, 0, true));
    path.setStroke(new Color("#000"));
    path.setStrokeWidth(2);
    path.setFill(new Color("#fc0"));
    path.setFillOpacity(0.25);
    component.addSprite(path);

    EllipseSprite ellipse = new EllipseSprite();
    ellipse.setCenterX(175);
    ellipse.setCenterY(100);
    ellipse.setRadiusX(25);
    ellipse.setRadiusY(40);
    ellipse.setFillOpacity(0.56);
    rotate = new Rotation();
    rotate.setDegrees(315);
    rotate.setX(ellipse.getCenterX() + ellipse.getRadiusX());
    rotate.setY(ellipse.getCenterY() + ellipse.getRadiusY());
    ellipse.setRotation(rotate);
    ellipse.setStroke(new Color("#000"));
    ellipse.setFill(new Color("#2fb92f"));
    ellipse.setStrokeWidth(5);
    component.addSprite(ellipse);

    ImageSprite imageSprite = new ImageSprite(ExampleImages.INSTANCE.music());
    imageSprite.setX(50);
    imageSprite.setY(10);
    component.addSprite(imageSprite);

    ContentPanel panel = new FramedPanel();
    panel.getElement().getStyle().setMargin(10, Unit.PX);
    panel.setCollapsible(true);
    panel.setHeadingText("Basic Draw");
    panel.setPixelSize(320, 300);
    panel.setBodyBorder(true);

    VerticalLayoutContainer layout = new VerticalLayoutContainer();
    panel.add(layout);

    component.setLayoutData(new VerticalLayoutData(1, 1));
    layout.add(component);

    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
