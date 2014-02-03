/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.desktop.client.layout.DesktopLayoutType;
import com.sencha.gxt.desktop.client.widget.EnumComboBox;
import com.sencha.gxt.desktopapp.client.images.DesktopImages;
import com.sencha.gxt.desktopapp.client.utility.Utility;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;

/**
 * Provides a profile view capable of displaying current profile settings and
 * allowing users to change them.
 * 
 * @see ProfileView
 */
public class ProfileViewImpl implements ProfileView, Editor<ProfileModel> {

  /**
   * Uses the GWT Editor / Driver framework to create a driver capable of
   * transferring data between the model to the view (editor).
   */
  interface ProfileDriver extends SimpleBeanEditorDriver<ProfileModel, ProfileViewImpl> {
  }

  private ProfilePresenter profilePresenter;

  private Window window;
  private VerticalLayoutContainer verticalLayoutContainer;
  private FieldLabel desktopLayoutTypeFieldLabel;
  private VerticalLayoutData layoutData;
  private Margins margins;
  private EnumComboBox<DesktopLayoutType> desktopLayoutTypeComboBox;
  private TextButton cancelButton;
  private SelectHandler cancelButtonSelectHandler;
  private TextButton okayButton;
  private SelectHandler okayButtonSelectHandler;
  private SimpleBeanEditorDriver<ProfileModel, Editor<? super ProfileModel>> profileDriver;

  /**
   * Creates a profile view that uses the services of the specified profile
   * presenter to perform the profile update.
   * 
   * @param profilePresenter the profile presenter
   */
  public ProfileViewImpl(ProfilePresenter profilePresenter) {
    this.profilePresenter = profilePresenter;
  }

  @Override
  public SimpleBeanEditorDriver<ProfileModel, Editor<? super ProfileModel>> getProfileDriver() {
    if (profileDriver == null) {
      profileDriver = GWT.create(ProfileDriver.class);
      profileDriver.initialize(this);
    }
    return profileDriver;
  }

  @Override
  public void hide() {
    getWindow().hide();
  }

  @Override
  public void onValidationError() {
    new AlertMessageBox("Validation Errors", "Please correct the errors and try again").show();
  }

  @Override
  public void show() {
    getWindow().setFocusWidget(getDesktopLayoutTypeComboBox());
    getWindow().show();
  }

  /**
   * Provides GWT Editor / Driver framework support for displaying and selecting
   * the desktop layout type.
   * 
   * @return a combo box capable of displaying and selecting the desktop layout
   *         type
   */
  @Path("desktopLayoutType")
  EnumComboBox<DesktopLayoutType> getDesktopLayoutTypeComboBox() {
    if (desktopLayoutTypeComboBox == null) {
      desktopLayoutTypeComboBox = new EnumComboBox<DesktopLayoutType>(DesktopLayoutType.values());
    }
    return desktopLayoutTypeComboBox;
  }

  private TextButton getCancelButton() {
    if (cancelButton == null) {
      cancelButton = new TextButton("Cancel");
      cancelButton.addSelectHandler(getCancelButtonSelectHandler());
    }
    return cancelButton;
  }

  private SelectHandler getCancelButtonSelectHandler() {
    if (cancelButtonSelectHandler == null) {
      cancelButtonSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getProfilePresenter().onCancel();
        }
      };
    }
    return cancelButtonSelectHandler;
  }

  private FieldLabel getDesktopLayoutTypeFieldLabel() {
    if (desktopLayoutTypeFieldLabel == null) {
      desktopLayoutTypeFieldLabel = new FieldLabel(getDesktopLayoutTypeComboBox(), "Desktop Layout Type");
    }
    return desktopLayoutTypeFieldLabel;
  }

  private VerticalLayoutData getLayoutData() {
    if (layoutData == null) {
      layoutData = new VerticalLayoutData(1, -1, getMargins());
    }
    return layoutData;
  }

  private Margins getMargins() {
    if (margins == null) {
      margins = new Margins(5);
    }
    return margins;
  }

  private TextButton getOkayButton() {
    if (okayButton == null) {
      okayButton = new TextButton("Okay");
      okayButton.addSelectHandler(getOkayButtonSelectHandler());
    }
    return okayButton;
  }

  private SelectHandler getOkayButtonSelectHandler() {
    if (okayButtonSelectHandler == null) {
      okayButtonSelectHandler = new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          getProfilePresenter().onOkay();
        }
      };
    }
    return okayButtonSelectHandler;
  }

  private ProfilePresenter getProfilePresenter() {
    return profilePresenter;
  }

  private VerticalLayoutContainer getVerticalLayoutContainer() {
    if (verticalLayoutContainer == null) {
      verticalLayoutContainer = new VerticalLayoutContainer();
      verticalLayoutContainer.add(getDesktopLayoutTypeFieldLabel(), getLayoutData());
      Utility.alignLabels(LabelAlign.TOP, verticalLayoutContainer);
    }
    return verticalLayoutContainer;
  }

  private Window getWindow() {
    if (window == null) {
      window = new Window();
      window.setHeadingText("Profile Update");
      window.getHeader().setIcon(DesktopImages.INSTANCE.user_edit());
      window.setPixelSize(200, 200);
      window.setButtonAlign(BoxLayoutPack.END);
      window.setModal(true);
      window.setBlinkModal(true);
      window.setClosable(false);
      window.setOnEsc(false);
      window.add(getVerticalLayoutContainer());
      window.addButton(getCancelButton());
      window.addButton(getOkayButton());
    }
    return window;
  }

}
