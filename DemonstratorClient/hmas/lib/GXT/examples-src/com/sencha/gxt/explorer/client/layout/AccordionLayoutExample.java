/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.layout;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.NameImageModel;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Detail(name = "AccordionLayout", icon = "accordionlayout", category = "Layouts")
public class AccordionLayoutExample implements IsWidget, EntryPoint {

  private ContentPanel panel;

  public Widget asWidget() {
    if (panel == null) {
      panel = new ContentPanel();
      panel.setHeadingText("AccordionLayout");
      panel.setBodyBorder(false);
      panel.setPixelSize(200, 325);
      panel.addStyleName("margin-10");
      panel.getHeader().setIcon(ExampleImages.INSTANCE.accordion());

      AccordionLayoutContainer con = new AccordionLayoutContainer();
      con.setExpandMode(ExpandMode.SINGLE_FILL);
      panel.add(con);

      AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);

      ContentPanel cp = new ContentPanel(appearance);
      cp.setAnimCollapse(false);
      cp.setHeadingText("Online Users");
      con.add(cp);
      con.setActiveWidget(cp);

      TreeStore<NameImageModel> store = new TreeStore<NameImageModel>(NameImageModel.KP);

      Tree<NameImageModel, String> tree = new Tree<NameImageModel, String>(store,
          new ValueProvider<NameImageModel, String>() {

            @Override
            public String getValue(NameImageModel object) {
              return object.getName();
            }

            @Override
            public void setValue(NameImageModel object, String value) {
            }

            @Override
            public String getPath() {
              return "name";
            }
          });

      tree.setIconProvider(new IconProvider<NameImageModel>() {
        public ImageResource getIcon(NameImageModel model) {
          if (null == model.getImage()) {
            return null;
          } else if ("user-girl" == model.getImage()) {
            return ExampleImages.INSTANCE.userFemale();
          } else if ("user-kid" == model.getImage()) {
            return ExampleImages.INSTANCE.userKid();
          } else {
            return ExampleImages.INSTANCE.user();
          }
        }
      });

      NameImageModel m = newItem("Family", null);
      store.add(m);

      store.add(m, newItem("Darrell", "user"));
      store.add(m, newItem("Maro", "user-girl"));
      store.add(m, newItem("Lia", "user-kid"));
      store.add(m, newItem("Alec", "user-kid"));
      store.add(m, newItem("Andrew", "user-kid"));

      tree.setExpanded(m, true);

      m = newItem("Friends", null);
      store.add(m);

      store.add(m, newItem("Bob", "user"));
      store.add(m, newItem("Mary", "user-girl"));
      store.add(m, newItem("Sally", "user-girl"));
      store.add(m, newItem("Jack", "user"));

      tree.setExpanded(m, true);

      cp.add(tree);

      cp = new ContentPanel(appearance);
      cp.setAnimCollapse(false);
      cp.setBodyStyleName("pad-text");
      cp.setHeadingText("Settings");
      cp.add(new Label(TestData.DUMMY_TEXT_SHORT));
      con.add(cp);

      cp = new ContentPanel(appearance);
      cp.setAnimCollapse(false);
      cp.setBodyStyleName("pad-text");
      cp.setHeadingText("Stuff");
      cp.add(new Label(TestData.DUMMY_TEXT_SHORT));
      con.add(cp);

      cp = new ContentPanel(appearance);
      cp.setAnimCollapse(false);
      cp.setBodyStyleName("pad-text");
      cp.setHeadingText("More Stuff");
      cp.add(new Label(TestData.DUMMY_TEXT_SHORT));
      con.add(cp);

    }
    return panel;
  }

  private NameImageModel newItem(String text, String iconStyle) {
    return new NameImageModel(text, iconStyle);
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

}
