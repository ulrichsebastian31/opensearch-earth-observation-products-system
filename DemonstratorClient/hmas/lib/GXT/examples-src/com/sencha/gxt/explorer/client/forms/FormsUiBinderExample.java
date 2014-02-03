/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.forms;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.model.Stock;
import com.sencha.gxt.examples.resources.client.model.StockProperties;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MinDateValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;

@Detail(name = "Forms Example (UiBinder)", icon = "forms", category = "Forms", files = "FormsUiBinderExample.ui.xml")
public class FormsUiBinderExample implements IsWidget, EntryPoint {

  interface MyUiBinder extends UiBinder<VerticalPanel, FormsUiBinderExample> {
  }

  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  private VerticalPanel vp;

  StockProperties props = GWT.create(StockProperties.class);
  ListStore<Stock> store = new ListStore<Stock>(props.key());

  @UiField(provided = true)
  ComboBox<Stock> combo = new ComboBox<Stock>(store, props.nameLabel());

  @UiField
  DateField date;

  @UiField
  Radio radio;

  @UiField
  Radio radio2;

  @UiField
  TextArea description;

  @UiField(provided = true)
  SpinnerField<Double> spinnerField;

  @UiField(provided = true)
  NumberField<Integer> age;

  @UiField(provided = true)
  Date minValue = new DateWrapper().clearTime().addHours(8).asDate();

  @UiField(provided = true)
  Date maxValue = new DateWrapper().clearTime().addHours(18).addSeconds(1).asDate();

  public Widget asWidget() {
    if (vp == null) {
      store.addAll(TestData.getStocks());

      spinnerField = new SpinnerField<Double>(new DoublePropertyEditor());
      age = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());

      vp = uiBinder.createAndBindUi(this);

      date.addValidator(new MinDateValidator(new Date()));
      description.addValidator(new MinLengthValidator(10));
      spinnerField.setIncrement(.1d);
      spinnerField.setMinValue(-10d);
      spinnerField.setMaxValue(10d);
      spinnerField.getPropertyEditor().setFormat(NumberFormat.getFormat("00.0"));

      // we can set name on radios or use toggle group
      ToggleGroup toggle = new ToggleGroup();
      toggle.add(radio);
      toggle.add(radio2);
    }
    return vp;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  @UiHandler("firstName")
  public void firstNameChanged(ValueChangeEvent<String> event) {
    Info.display("Value Changed", "First name changed to " + event.getValue() == null ? "blank" : event.getValue());
  }

  @UiHandler("age")
  public void onAgeParseError(ParseErrorEvent event) {
    Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a number");
  }

  @UiHandler("date")
  public void onDateParseError(ParseErrorEvent event) {
    Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a date");
  }

  @UiHandler("date")
  public void onDateChanged(ValueChangeEvent<Date> event) {
    String v = event.getValue() == null ? "nothing" : DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM).format(
        event.getValue());
    Info.display("Selected", "You selected " + v);
  }

  @UiHandler("time")
  public void onTimeParseError(ParseErrorEvent event) {
    Info.display("Parse Error", event.getErrorValue() + " could not be parsed as a valid time.");
  }
}
