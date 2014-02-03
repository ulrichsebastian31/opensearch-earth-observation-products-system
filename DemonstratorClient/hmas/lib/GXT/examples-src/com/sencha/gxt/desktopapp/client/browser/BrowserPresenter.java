/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.browser;

import com.sencha.gxt.desktopapp.client.Presenter;

public interface BrowserPresenter extends Presenter {

  void bind();

  void onClose();

  void onSave();

  void unbind();

}