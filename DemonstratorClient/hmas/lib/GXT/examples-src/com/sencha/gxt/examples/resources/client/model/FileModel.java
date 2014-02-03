/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.examples.resources.client.model;

import java.io.Serializable;
import java.util.Date;

public class FileModel implements Serializable {

  private String id;
  private String name;
  private String path;
  private Long size;
  private Date lastModified;

  public FileModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public FileModel(String name, String path) {
    this.name = name;
    this.path = path;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public Long getSize() {
    return size;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setSize(Long size) {
    this.size = size;
  }

}
