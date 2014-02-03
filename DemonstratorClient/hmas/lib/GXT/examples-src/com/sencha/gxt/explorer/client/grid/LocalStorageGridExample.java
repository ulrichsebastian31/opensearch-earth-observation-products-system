/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.ScriptTagProxy;
import com.sencha.gxt.data.client.loader.StorageReadProxy;
import com.sencha.gxt.data.client.loader.StorageWriteProxy;
import com.sencha.gxt.data.client.loader.StorageWriteProxy.Entry;
import com.sencha.gxt.data.client.writer.UrlEncodingWriter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.JsonReader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

@Detail(name = "LocalStorage Grid", icon = "localstoragegrid", category = "Grid", classes = {})
public class LocalStorageGridExample implements IsWidget, EntryPoint {
  private FramedPanel fp;

  interface TestAutoBeanFactory extends AutoBeanFactory {
    static TestAutoBeanFactory instance = GWT.create(TestAutoBeanFactory.class);

    AutoBean<ForumCollection> dataCollection();

    AutoBean<ForumListLoadResult> dataLoadResult();

    AutoBean<ForumLoadConfig> loadConfig();
  }

  public interface Forum {
    @PropertyName("topic_title")
    public String getTitle();

    @PropertyName("topic_id")
    public String getTopicId();

    public String getAuthor();

    @PropertyName("forumid")
    public String getForumId();

    @PropertyName("post_text")
    public String getExcerpt();

    @PropertyName("post_id")
    public String getPostId();

    @PropertyName("post_time")
    public Date getDate();

  }

  interface ForumCollection {
    String getTotalCount();

    List<Forum> getTopics();
  }

  interface ForumLoadConfig extends PagingLoadConfig {
    String getQuery();

    void setQuery(String query);

    @Override
    @PropertyName("start")
    public int getOffset();

    @Override
    @PropertyName("start")
    public void setOffset(int offset);
  }

  interface ForumListLoadResult extends PagingLoadResult<Forum> {
    void setData(List<Forum> data);

    @Override
    @PropertyName("start")
    public int getOffset();

    @Override
    @PropertyName("start")
    public void setOffset(int offset);
  }

  interface ForumProperties extends PropertyAccess<Forum> {
    @Path("topicId")
    ModelKeyProvider<Forum> key();


    ValueProvider<Forum, String> title();
    ValueProvider<Forum, String> excerpt();
    ValueProvider<Forum, String> author();
    ValueProvider<Forum, Date> date();

  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  public Widget asWidget() {
    if (fp == null) {

      final Storage storage = Storage.getLocalStorageIfSupported();
      if (storage == null) {
        new MessageBox("Not Supported","Your browser doesn't appear to supprt HTML5 localStorage").show();
        return new HTML("LocalStorage not supported in this browser");
      }

      // Writer to translate load config into string
      UrlEncodingWriter<ForumLoadConfig> writer = new UrlEncodingWriter<ForumLoadConfig>(TestAutoBeanFactory.instance, ForumLoadConfig.class);
      // Reader to translate String results into objects
      JsonReader<ForumListLoadResult, ForumCollection> reader = new JsonReader<ForumListLoadResult, ForumCollection>(
          TestAutoBeanFactory.instance, ForumCollection.class) {
        @Override
        protected ForumListLoadResult createReturnData(Object loadConfig, ForumCollection records) {
          PagingLoadConfig cfg = (PagingLoadConfig) loadConfig;
          ForumListLoadResult res = TestAutoBeanFactory.instance.dataLoadResult().as();
          res.setData(records.getTopics());
          res.setOffset(cfg.getOffset());
          res.setTotalLength(Integer.parseInt(records.getTotalCount()));
          return res;
        }
      };

      // Proxy to load from server
      String url = "http://www.sencha.com/forum/topics-remote.php";
      final ScriptTagProxy<ForumLoadConfig> remoteProxy = new ScriptTagProxy<ForumLoadConfig>(url);
      remoteProxy.setWriter(writer);

      // Proxy to load objects from local storage
      final StorageReadProxy<ForumLoadConfig> localReadProxy = new StorageReadProxy<ForumLoadConfig>(storage);
      localReadProxy.setWriter(writer);

      // Proxy to persist network-loaded objects into local storage
      final StorageWriteProxy<ForumLoadConfig, String> localWriteProxy = new StorageWriteProxy<ForumLoadConfig, String>(storage);
      localWriteProxy.setKeyWriter(writer);

      // Wrapper Proxy to dispatch to either storage or scripttag, and to save results
      DataProxy<ForumLoadConfig, String> proxy = new DataProxy<ForumLoadConfig, String>() {
        @Override
        public void load(final ForumLoadConfig loadConfig, final Callback<String, Throwable> callback) {
          // Storage read is known to be synchronous, so read it first - if null, continue
          localReadProxy.load(loadConfig, new Callback<String, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
              // ignore failure, go remote
              onSuccess(null);
            }
            @Override
            public void onSuccess(String result) {
              if (result != null) {
                callback.onSuccess(result);
              } else {
                //read from remote and save it
                remoteProxy.load(loadConfig, new Callback<JavaScriptObject, Throwable>() {
                  @Override
                  public void onSuccess(JavaScriptObject result) {
                    //write results to local db
                    String json = new JSONObject(result).toString();
                    Entry<ForumLoadConfig, String> data = new Entry<ForumLoadConfig, String>(loadConfig, json);
                    localWriteProxy.load(data, new Callback<Void, Throwable>() {
                      @Override
                      public void onSuccess(Void result) {
                        // ignore response
                      }
                      @Override
                      public void onFailure(Throwable reason) {
                        // ignore response
                      }
                    });

                    callback.onSuccess(json);
                  }

                  @Override
                  public void onFailure(Throwable reason) {
                    callback.onFailure(reason);
                  }
                });
              }
            }
          });
        }
      };


      PagingLoader<ForumLoadConfig, ForumListLoadResult> loader = new PagingLoader<ForumLoadConfig, ForumListLoadResult>(
          proxy, reader);
      loader.useLoadConfig(TestAutoBeanFactory.instance.loadConfig().as());

      ForumProperties props = GWT.create(ForumProperties.class);

      ListStore<Forum> store = new ListStore<Forum>(props.key());
      loader.addLoadHandler(new LoadResultListStoreBinding<ForumLoadConfig, Forum, ForumListLoadResult>(store));

      ColumnConfig<Forum, String> cc1 = new ColumnConfig<Forum, String>(props.title(), 100, "Title");
      cc1.setSortable(false);
      ColumnConfig<Forum, String> cc2 = new ColumnConfig<Forum, String>(props.excerpt(), 165, "Excerpt");
      cc2.setSortable(false);
      ColumnConfig<Forum, Date> cc3 = new ColumnConfig<Forum, Date>(props.date(), 100, "Date");
      cc3.setSortable(false);
      ColumnConfig<Forum, String> cc4 = new ColumnConfig<Forum, String>(props.author(), 50, "Author");
      cc4.setSortable(false);

      List<ColumnConfig<Forum, ?>> l = new ArrayList<ColumnConfig<Forum, ?>>();
      l.add(cc1);
      l.add(cc2);
      l.add(cc3);
      l.add(cc4);
      ColumnModel<Forum> cm = new ColumnModel<Forum>(l);

      Grid<Forum> grid = new Grid<Forum>(store, cm);
      grid.getView().setForceFit(true);
      grid.setLoader(loader);
      grid.setLoadMask(true);
      grid.setBorders(true);

      final PagingToolBar toolBar = new PagingToolBar(50);
      toolBar.getElement().getStyle().setProperty("borderBottom", "none");
      toolBar.add(new TextButton("Clear Cache", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          storage.clear();
        }
      }));
      toolBar.bind(loader);

      fp = new FramedPanel();
      fp.setHeadingText("LocalStorage Grid Example");
      fp.setCollapsible(true);
      fp.setAnimCollapse(true);
      fp.setPixelSize(575, 350);
      fp.addStyleName("margin-10");
      fp.setButtonAlign(BoxLayoutPack.CENTER);

      VerticalLayoutContainer con = new VerticalLayoutContainer();
      con.setBorders(true);
      con.add(grid, new VerticalLayoutData(1, 1));
      con.add(toolBar, new VerticalLayoutData(1, -1));
      fp.setWidget(con);

      loader.load();
    }

    return fp;
  }

}
