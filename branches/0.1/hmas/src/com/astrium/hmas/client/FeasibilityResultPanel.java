package com.astrium.hmas.client;

import com.astrium.hmas.bean.FeasibilityResult;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.NoSelectionModel;

public class FeasibilityResultPanel extends Composite implements HasText {

	private static FeasibilityResultPanelUiBinder uiBinder = GWT
			.create(FeasibilityResultPanelUiBinder.class);
	@UiField(provided = true)
	CellTable<FeasibilityResult> feasibility_result_panel_cellTable = new CellTable<FeasibilityResult>();
	@UiField Button feasibility_result_panel_show_xml_button;
	public NoSelectionModel<FeasibilityResult> feasibility_result_panel_selectionModelDetails = new NoSelectionModel<FeasibilityResult>();
	public Column<FeasibilityResult, String> feasibility_result_panel_showColumn;
	public boolean hideButtonClicked = false;

	interface FeasibilityResultPanelUiBinder extends
			UiBinder<Widget, FeasibilityResultPanel> {
	}

	public FeasibilityResultPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		feasibility_result_panel_cellTable.getElement().getStyle()
				.setOverflow(Overflow.AUTO);
		Column<FeasibilityResult, Boolean> shopcartColumn = new Column<FeasibilityResult, Boolean>(
				new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(FeasibilityResult object) {
				// Get the value from the selection model.
				return false;
			}
		};

		feasibility_result_panel_cellTable
				.addColumn(shopcartColumn, "Shopcart");
		feasibility_result_panel_cellTable.setColumnWidth(shopcartColumn, 40,
				Unit.PX);

		ButtonCell showButton = new ButtonCell();
		feasibility_result_panel_showColumn = new Column<FeasibilityResult, String>(
				showButton) {
			public String getValue(FeasibilityResult object) {
				if(object.upperLeft == null){
					return "no footprint";
				}else{
					return hideButtonClicked ? "Show" : "Hide";
				}
				
			}
		};

		feasibility_result_panel_cellTable.addColumn(
				feasibility_result_panel_showColumn, "Show");
		feasibility_result_panel_cellTable.setColumnWidth(
				feasibility_result_panel_showColumn, 40, Unit.PX);

		TextColumn<FeasibilityResult> nameColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if (object.platform != null) {
					return object.platform;
				} else {
					return "not informed";
				}

			}
		};
		feasibility_result_panel_cellTable.addColumn(nameColumn, "Platform");

		TextColumn<FeasibilityResult> sensorColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if (object.instrument != null) {
					return object.instrument;
				} else {
					return "not informed";
				}
			}
		};
		feasibility_result_panel_cellTable.addColumn(sensorColumn, "Sensor");

		TextColumn<FeasibilityResult> dateColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if(object.acquisitionDate != null){
					return object.acquisitionDate;
				}else{
					return "not informed";
				}
				
			}
		};

		feasibility_result_panel_cellTable.addColumn(dateColumn, "Date");

		TextColumn<FeasibilityResult> cloudColumn = new TextColumn<FeasibilityResult>() {
			@Override
			public String getValue(FeasibilityResult object) {
				if (object.cloudCover != null) {
					return object.cloudCover;
				} else {
					return "not informed";
				}
			}
		};
		feasibility_result_panel_cellTable.addColumn(cloudColumn,
				"Cloud coverage");

	}

	public FeasibilityResultPanel(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setText(String text) {

	}

	public String getText() {
		return null;
	}

}
