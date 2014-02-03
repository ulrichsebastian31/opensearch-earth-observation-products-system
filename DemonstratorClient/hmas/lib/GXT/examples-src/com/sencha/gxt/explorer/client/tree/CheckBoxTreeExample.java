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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.data.shared.LabelProvider;
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
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;

@Detail(name = "CheckBox Tree", category = "Tree", icon = "checkboxtree")
public class CheckBoxTreeExample implements IsWidget, EntryPoint {

  class KeyProvider implements ModelKeyProvider<BaseDto> {
    @Override
    public String getKey(BaseDto item) {
      return (item instanceof FolderDto ? "f-" : "m-") + item.getId().toString();
    }
  }

  @Override
  public Widget asWidget() {
    FlowLayoutContainer con = new FlowLayoutContainer();

    TreeStore<BaseDto> store = new TreeStore<BaseDto>(new KeyProvider());

    FolderDto root = TestData.getMusicRootFolder();
    for (BaseDto base : root.getChildren()) {
      store.add(base);
      if (base instanceof FolderDto) {
        processFolder(store, (FolderDto) base);
      }
    }

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
    tree.setCheckable(true);
    tree.setCheckStyle(CheckCascade.TRI);
    tree.setAutoLoad(true);
    
    final DelayedTask task = new DelayedTask() {
      
      @Override
      public void onExecute() {
        Info.display("Action", "Check changed: " + tree.getCheckedSelection().size() + " item(s) selected");
      }
    };

    tree.addCheckChangedHandler(new CheckChangedHandler<BaseDto>() {
      @Override
      public void onCheckChanged(CheckChangedEvent<BaseDto> event) {
        task.delay(100);
      }
    });

    SimpleComboBox<String> cascade = new SimpleComboBox<String>(new LabelProvider<String>() {
      @Override
      public String getLabel(String item) {
        return item;
      }
    });
    cascade.setTriggerAction(TriggerAction.ALL);
    cascade.setEditable(false);
    cascade.add("Tri");
    cascade.add("Parent");
    cascade.add("Children");
    cascade.add("None");
    cascade.setValue("Tri");

    cascade.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String val = event.getValue();
        if ("Parent".equals(val)) {
          tree.setCheckStyle(CheckCascade.PARENTS);
        } else if ("Children".equals(val)) {
          tree.setCheckStyle(CheckCascade.CHILDREN);
        } else if ("Tri".equals(val)) {
          tree.setCheckStyle(CheckCascade.TRI);
        } else {
          tree.setCheckStyle(CheckCascade.NONE);
        }
      }
    });

    final SimpleComboBox<String> checkNodes = new SimpleComboBox<String>(new LabelProvider<String>() {

      @Override
      public String getLabel(String item) {
        return item;
      }
    });

    checkNodes.setTriggerAction(TriggerAction.ALL);
    checkNodes.setEditable(false);
    checkNodes.add("Both");
    checkNodes.add("Leaf");
    checkNodes.add("Parent");
    checkNodes.setValue("Both");

    checkNodes.addValueChangeHandler(new ValueChangeHandler<String>() {

      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        String val = event.getValue();
        if ("Parent".equals(val)) {
          tree.setCheckNodes(CheckNodes.PARENT);
        } else if ("Leaf".equals(val)) {
          tree.setCheckNodes(CheckNodes.LEAF);
        } else {
          tree.setCheckNodes(CheckNodes.BOTH);
        }

      }
    });

    ToolBar toolBar = new ToolBar();
    toolBar.addStyleName(ThemeStyles.getStyle().borderBottom());

    TextButton b = new TextButton("Get Checked");
    b.addSelectHandler(new SelectHandler() {

      @Override
      public void onSelect(SelectEvent event) {
        StringBuffer sb = new StringBuffer();
        List<BaseDto> checked = tree.getCheckedSelection();
        for (int i = 0; i < checked.size(); i++) {
          BaseDto item = checked.get(i);
          sb.append(", " + (String) item.getName());
        }
        String s = sb.toString();
        if (s.length() > 1) s = s.substring(2);

        Info.display("Checked Items (" + checked.size() + ")", Format.ellipse(s, 100));
      }
    });

    toolBar.add(b);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(new LabelToolItem("Cascade Behavior: "));
    toolBar.add(cascade);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(new LabelToolItem("CheckNode Behavior: "));
    toolBar.add(checkNodes);

    con.add(toolBar);
    con.add(tree, new MarginData(10));
    return con;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
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
