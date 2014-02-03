/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.desktopapp.client.persistence;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.desktopapp.client.persistence.FileModel.FileType;

public interface FileModelProperties extends PropertyAccess<FileModel> {
  
  @Path("id")
  ModelKeyProvider<FileModel> key();

  ValueProvider<FileModel, FileType> fileType();

  ValueProvider<FileModel, Date> lastModified();

  ValueProvider<FileModel, String> name();

  ValueProvider<FileModel, Long> size();
  
}

