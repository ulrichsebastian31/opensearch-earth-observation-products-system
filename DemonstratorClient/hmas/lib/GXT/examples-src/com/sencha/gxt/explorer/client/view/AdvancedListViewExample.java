/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.view;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.Formatter;
import com.sencha.gxt.core.client.XTemplates.FormatterFactories;
import com.sencha.gxt.core.client.XTemplates.FormatterFactory;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.ListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.examples.resources.client.ExampleService;
import com.sencha.gxt.examples.resources.client.ExampleServiceAsync;
import com.sencha.gxt.examples.resources.client.model.Photo;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

@Detail(name = "Advanced ListView", icon = "advancedlistview", category = "Templates & Lists")
public class AdvancedListViewExample implements IsWidget, EntryPoint {

  interface DetailRenderer extends XTemplates {
    @XTemplate(source = "AdvancedListViewDetail.html")
    public SafeHtml render(Photo photo);
  }

  @FormatterFactories(@FormatterFactory(factory = ShortenFactory.class, name = "shorten"))
  interface Renderer extends XTemplates {
    @XTemplate(source = "ListViewExample.html")
    public SafeHtml renderItem(Photo photo, Style style);
  }

  interface Resources extends ClientBundle {
    @Source("ListViewExample.css")
    Style css();
  }

  static class Shorten implements Formatter<String> {

    private int length;

    public Shorten(int length) {
      this.length = length;
    }

    @Override
    public String format(String data) {
      return Format.ellipse(data, length);
    }
  }

  static class ShortenFactory {
    public static Shorten getFormat(int length) {
      return new Shorten(length);
    }
  }

  interface Style extends CssResource {
    String over();

    String select();

    String thumb();

    String thumbWrap();
  }

  private Dialog chooser;
  private HTML details;

  private Image image;
  private DetailRenderer renderer;
  private SimpleComboBox<String> sort;
  private ListStore<Photo> store;
  private ListView<Photo, Photo> view;
  private VerticalPanel vp;

  public Widget asWidget() {
    if (vp == null) {
      renderer = GWT.create(DetailRenderer.class);

      final ExampleServiceAsync service = GWT.create(ExampleService.class);

      RpcProxy<Object, List<Photo>> proxy = new RpcProxy<Object, List<Photo>>() {
        @Override
        public void load(Object loadConfig, AsyncCallback<List<Photo>> callback) {
          service.getPhotos(callback);
        }
      };

      Loader<Object, List<Photo>> loader = new Loader<Object, List<Photo>>(proxy);

      ModelKeyProvider<Photo> kp = new ModelKeyProvider<Photo>() {
        @Override
        public String getKey(Photo item) {
          return item.getName();
        }
      };

      store = new ListStore<Photo>(kp);
      loader.addLoadHandler(new ListStoreBinding<Object, Photo, List<Photo>>(store));
      store.addSortInfo(new StoreSortInfo<Photo>(new Comparator<Photo>() {

        @Override
        public int compare(Photo o1, Photo o2) {
          String v = sort.getCurrentValue();
          if (v.equals("Name")) {
            return o1.getName().compareToIgnoreCase(o2.getName());
          } else if (v.equals("File Size")) {
            return o1.getSize() < o2.getSize() ? -1 : 1;
          } else {
            o1.getDate().compareTo(o2.getDate());
          }
          return 0;
        }
      }, SortDir.ASC));
      loader.load();

      chooser = new Dialog();
      chooser.setId("img-chooser-dlg");
      chooser.setHeadingText("Choose an Image");
      chooser.setWidth(500);
      chooser.setHeight(300);
      chooser.setModal(true);
      chooser.setBodyStyle("background: none");
      chooser.setBodyBorder(false);
      chooser.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
      chooser.setHideOnButtonClick(true);

      chooser.addHideHandler(new HideHandler() {

        @Override
        public void onHide(HideEvent event) {
          Photo photo = view.getSelectionModel().getSelectedItem();
          if (photo != null) {
            if (chooser.getHideButton() == chooser.getButtonById(PredefinedButton.OK.name())) {
              image.setUrl(photo.getPath());
              image.setVisible(true);
            }
          }
        }
      });

      BorderLayoutContainer con = new BorderLayoutContainer();
      chooser.add(con);

      VerticalLayoutContainer main = new VerticalLayoutContainer();
      main.setBorders(true);

      ToolBar bar = new ToolBar();
      bar.add(new LabelToolItem("Filter:"));

      StoreFilterField<Photo> field = new StoreFilterField<Photo>() {

        @Override
        protected boolean doSelect(Store<Photo> store, Photo parent, Photo item, String filter) {
          String name = item.getName().toLowerCase();
          if (name.indexOf(filter.toLowerCase()) != -1) {
            return true;
          }
          return false;
        }

        @Override
        protected void onFilter() {
          super.onFilter();
          view.getSelectionModel().select(0, false);
        }

      };
      field.setWidth(100);
      field.bind(store);

      bar.add(field);
      bar.add(new SeparatorToolItem());
      bar.add(new LabelToolItem("Sort By:"));

      sort = new SimpleComboBox<String>(new StringLabelProvider<String>());
      sort.setTriggerAction(TriggerAction.ALL);
      sort.setEditable(false);
      sort.setForceSelection(true);
      sort.setWidth(120);
      sort.add("Name");
      sort.add("File Size");
      sort.add("Last Modified");
      sort.setValue("Name");
      sort.addSelectionHandler(new SelectionHandler<String>() {
        @Override
        public void onSelection(SelectionEvent<String> event) {
          store.applySort(false);
        }
      });
      bar.add(sort);

      main.add(bar, new VerticalLayoutData(1, -1));

      final Resources resources = GWT.create(Resources.class);
      resources.css().ensureInjected();
      final Style style = resources.css();

      final Renderer r = GWT.create(Renderer.class);

      ListViewCustomAppearance<Photo> appearance = new ListViewCustomAppearance<Photo>("." + style.thumbWrap(),
          style.over(), style.select()) {
        @Override
        public void renderEnd(SafeHtmlBuilder builder) {
          String markup = new StringBuilder("<div class=\"").append(CommonStyles.get().clear()).append("\"></div>").toString();
          builder.appendHtmlConstant(markup);
        }

        @Override
        public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
          builder.appendHtmlConstant("<div class='" + style.thumbWrap() + "' style='border: 1px solid white'>");
          builder.append(content);
          builder.appendHtmlConstant("</div>");
        }

      };

      view = new ListView<Photo, Photo>(store, new IdentityValueProvider<Photo>() {

        @Override
        public void setValue(Photo object, Photo value) {

        }
      }, appearance);
      view.setCell(new SimpleSafeHtmlCell<Photo>(new AbstractSafeHtmlRenderer<Photo>() {

        @Override
        public SafeHtml render(Photo object) {
          return r.renderItem(object, style);
        }
      }));
      view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      view.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Photo>() {

        @Override
        public void onSelectionChanged(SelectionChangedEvent<Photo> event) {
          AdvancedListViewExample.this.onSelectionChange(event);
        }
      });
      view.setBorders(false);

      main.add(view, new VerticalLayoutData(1, 1));

      details = new HTML();
      details.addStyleName(ThemeStyles.getStyle().border());
      details.getElement().getStyle().setBackgroundColor("white");

      BorderLayoutData eastData = new BorderLayoutData(150);
      eastData.setSplit(true);

      BorderLayoutData centerData = new BorderLayoutData();
      centerData.setMargins(new Margins(0, 5, 0, 0));

      con.setCenterWidget(main, centerData);
      con.setEastWidget(details, eastData);

      TextButton b = new TextButton("Choose");
      b.addSelectHandler(new SelectHandler() {

        @Override
        public void onSelect(SelectEvent event) {
          chooser.show();
          view.getSelectionModel().select(0, false);
        }
      });

      vp = new VerticalPanel();
      vp.setSpacing(10);

      vp.add(b);
      
      image = new Image();
      image.getElement().getStyle().setProperty("marginTop", "10px");
      image.setVisible(false);
      vp.add(image);


    }
    return vp;
  }

  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private void onSelectionChange(SelectionChangedEvent<Photo> se) {
    if (se.getSelection().size() > 0) {
      details.setHTML(renderer.render(se.getSelection().get(0)).asString());
      chooser.getButtonById(PredefinedButton.OK.name()).enable();
    } else {
      chooser.getButtonById(PredefinedButton.OK.name()).disable();
      details.setHTML("");
    }
  }

}
