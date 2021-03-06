/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.theme.blue.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.theme.base.client.menu.SeparatorMenuItemBaseAppearance;

public class BlueSeparatorMenuItemAppearance extends SeparatorMenuItemBaseAppearance {

  public interface BlueSeparatorMenuItemResources extends ClientBundle, SeparatorMenuItemResources {

    @Source({"com/sencha/gxt/theme/base/client/menu/SeparatorMenuItem.css", "BlueSeparatorMenuItem.css"})
    BlueSeparatorMenuItemStyle style();

  }

  public interface BlueSeparatorMenuItemStyle extends SeparatorMenuItemStyle {
  }

  public BlueSeparatorMenuItemAppearance() {
    this(GWT.<BlueSeparatorMenuItemResources> create(BlueSeparatorMenuItemResources.class),
        GWT.<SeparatorMenuItemTemplate> create(SeparatorMenuItemTemplate.class));
  }

  public BlueSeparatorMenuItemAppearance(BlueSeparatorMenuItemResources resources,
      SeparatorMenuItemTemplate template) {
    super(resources, template);
  }

}
