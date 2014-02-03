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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.examples.resources.client.ExampleService;
import com.sencha.gxt.examples.resources.client.ExampleServiceAsync;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.examples.resources.client.model.BaseDto;
import com.sencha.gxt.examples.resources.client.model.FolderDto;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.state.client.TreeStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

@Detail(name = "Async Tree", category = "Tree", icon = "asynctree")
public class AsyncTreeExample implements IsWidget, EntryPoint {

  class KeyProvider implements ModelKeyProvider<BaseDto> {
    @Override
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  }

  @Override
  public Widget asWidget() {
    ContentPanel panel = new ContentPanel();
    panel.setHeadingText("Async Tree");
    panel.setPixelSize(315, 400);
    panel.addStyleName("margin-10");
    
    VerticalLayoutContainer con = new VerticalLayoutContainer();
    panel.add(con);

    final ExampleServiceAsync service = GWT.create(ExampleService.class);
    RpcProxy<BaseDto, List<BaseDto>> proxy = new RpcProxy<BaseDto, List<BaseDto>>() {

      @Override
      public void load(BaseDto loadConfig, AsyncCallback<List<BaseDto>> callback) {
        service.getMusicFolderChildren((FolderDto) loadConfig, callback);
      }
    };

    TreeLoader<BaseDto> loader = new TreeLoader<BaseDto>(proxy) {
      @Override
      public boolean hasChildren(BaseDto parent) {
        return parent instanceof FolderDto;
      }
    };

    TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());
    loader.addLoadHandler(new ChildTreeStoreBinding<BaseDto>(store));

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
    tree.setLoader(loader);

    TreeStateHandler<BaseDto> stateHandler = new TreeStateHandler<BaseDto>(tree);
    stateHandler.loadState();
    tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

    ToolBar buttonBar = new ToolBar();

    buttonBar.add(new TextButton("Expand All", new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        tree.expandAll();
      }
    }));
    buttonBar.add(new TextButton("Collapse All", new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        tree.collapseAll();
      }

    }));

    con.add(buttonBar, new VerticalLayoutData(1, -1));
    con.add(tree, new VerticalLayoutData(1, 1));
    
    return panel;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }
}
