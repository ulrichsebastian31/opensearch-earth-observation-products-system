/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.tree;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.BaseDto;
import com.sencha.gxt.examples.resources.client.model.FolderDto;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Detail(name = "Context Menu Tree", category = "Tree", icon = "contextmenutree")
public class ContextMenuTreeExample implements IsWidget, EntryPoint {

  private int count = 1;

  class KeyProvider implements ModelKeyProvider<BaseDto> {
    @Override
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private TreeStore<BaseDto> store;
  private FolderDto root = TestData.getMusicRootFolder();

  @Override
  public Widget asWidget() {
    FlowLayoutContainer con = new FlowLayoutContainer();
    con.addStyleName("margin-10");

    store = new TreeStore<BaseDto>(new KeyProvider());

    loadStore(store, root);

    final Tree<BaseDto, String> tree = new Tree<BaseDto, String>(store, new ValueProvider<BaseDto, String>() {

      @Override
      public String getValue(BaseDto object) {
        return object.getName();
      }

      @Override
      public void setValue(BaseDto object, String value) {
      }

      @Override
      public String getPath() {
        return "name";
      }
    });
    tree.setWidth(300);
    tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

    TextButton b = new TextButton("Reset");
    b.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        store.clear();
        loadStore(store, root);
      }
    });

    Menu contextMenu = new Menu();

    MenuItem insert = new MenuItem();
    insert.setText("Insert Item");
    insert.setIcon(ExampleImages.INSTANCE.add());
    insert.addSelectionHandler(new SelectionHandler<Item>() {

      @Override
      public void onSelection(SelectionEvent<Item> event) {
        BaseDto sel = tree.getSelectionModel().getSelectedItem();
        if (sel != null) {
          FolderDto child = new FolderDto(count * 100, "Add Child " + count++);
          store.add(sel, child);
          tree.setExpanded(sel, true);
        }
      }
    });

    contextMenu.add(insert);

    MenuItem remove = new MenuItem();
    remove.setText("Remove Selected");
    remove.setIcon(ExampleImages.INSTANCE.delete());
    remove.addSelectionHandler(new SelectionHandler<Item>() {

      @Override
      public void onSelection(SelectionEvent<Item> event) {
        List<BaseDto> selected = tree.getSelectionModel().getSelectedItems();
        for (BaseDto sel : selected) {
          store.remove(sel);
        }
      }

    });

    contextMenu.add(remove);

    tree.setContextMenu(contextMenu);

    b.setLayoutData(new MarginData(4));
    con.add(b);
    con.add(tree);
    return con;
  }

  private void loadStore(TreeStore<BaseDto> store, FolderDto root) {
    for (BaseDto base : root.getChildren()) {
      store.add(base);
      if (base instanceof FolderDto) {
        processFolder(store, (FolderDto) base);
      }
    }
  }

  private void processFolder(TreeStore<BaseDto> store, FolderDto folder) {
    for (BaseDto child : folder.getChildren()) {
      store.add(folder, child);
      if (child instanceof FolderDto) {
        processFolder(store, (FolderDto) child);
      }
    }
  }
}
