/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

@Detail(name = "PortalLayout (UiBinder)", icon = "portal", category = "Layouts", fit = true, files = "PortalLayoutContainerUiBinderExample.ui.xml")
public class PortalLayoutContainerUiBinderExample implements IsWidget, EntryPoint {

  interface CellTemplates extends XTemplates {

    @XTemplate("<span style='{styles}' qtitle='Change' qtip='{qtip}'>{value}</span>")
    SafeHtml template(SafeStyles styles, String qtip, String value);

  }

  interface MyUiBinder extends UiBinder<Widget, PortalLayoutContainerUiBinderExample> {
  }

  enum ToolConfig {
    GEAR(ToolButton.GEAR), CLOSE(ToolButton.CLOSE);

    private IconConfig config;

    private ToolConfig(IconConfig config) {
      this.config = config;
    }
  }

  private static final StockProperties props = GWT.create(StockProperties.class);

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  @UiField
  PortalLayoutContainer portal;

  @UiField(provided = true)
  String txt = TestData.DUMMY_TEXT_SHORT;

  public Widget asWidget() {
    Widget widget = uiBinder.createAndBindUi(this);

    portal.setColumnWidth(0, .40);
    portal.setColumnWidth(1, .30);
    portal.setColumnWidth(2, .30);

    return widget;
  }

  @UiFactory()
  public Grid<Stock> createGrid() {
    final NumberFormat number = NumberFormat.getFormat("0.00");

    ColumnConfig<Stock, String> nameCol = new ColumnConfig<Stock, String>(props.name(), 200, "Company");
    ColumnConfig<Stock, String> symbolCol = new ColumnConfig<Stock, String>(props.symbol(), 100, "Symbol");
    ColumnConfig<Stock, Double> lastCol = new ColumnConfig<Stock, Double>(props.last(), 75, "Last");

    ColumnConfig<Stock, Double> changeCol = new ColumnConfig<Stock, Double>(props.change(), 100, "Change");
    changeCol.setCell(new AbstractCell<Double>() {

      @Override
      public void render(Context context, Double value, SafeHtmlBuilder sb) {
        SafeStylesBuilder stylesBuilder = new SafeStylesBuilder();
        stylesBuilder.appendTrustedString("color:" + (value < 0 ? "red" : "green") + ";");
        String v = number.format(value);
        CellTemplates cellTemplates = GWT.create(CellTemplates.class);
        SafeHtml template = cellTemplates.template(stylesBuilder.toSafeStyles(), v, v);
        sb.append(template);
      }
    });

    ColumnConfig<Stock, Date> lastTransCol = new ColumnConfig<Stock, Date>(props.lastTrans(), 100, "Last Updated");
    lastTransCol.setCell(new DateCell(DateTimeFormat.getFormat("MM/dd/yyyy")));

    List<ColumnConfig<Stock, ?>> l = new ArrayList<ColumnConfig<Stock, ?>>();
    l.add(nameCol);
    l.add(symbolCol);
    l.add(lastCol);
    l.add(changeCol);
    l.add(lastTransCol);
    ColumnModel<Stock> cm = new ColumnModel<Stock>(l);

    ListStore<Stock> store = new ListStore<Stock>(props.key());
    store.addAll(TestData.getStocks());

    final Grid<Stock> grid = new Grid<Stock>(store, cm);
    grid.getView().setAutoExpandColumn(nameCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);

    // needed to enable quicktips (qtitle for the heading and qtip for the
    // content) that are setup in the change GridCellRenderer
    new QuickTip(grid);

    return grid;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  @UiFactory
  protected ToolButton createToolButton(ToolConfig icon, Portlet portlet) {
    ToolButton toolButton = new ToolButton(icon.config);
    toolButton.setData("portlet", portlet);
    return toolButton;
  }

  @UiHandler({"portlet1Close", "portlet2Close", "portlet3Close", "portlet4Close"})
  protected void onClosePortlet(SelectEvent event) {
    ToolButton tool = (ToolButton) event.getSource();
    Portlet portlet = tool.getData("portlet");
    portlet.removeFromParent();
  }

}
