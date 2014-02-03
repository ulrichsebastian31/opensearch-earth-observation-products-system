/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.binding;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.examples.resources.client.model.Kid;
import com.sencha.gxt.examples.resources.client.model.Person;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

@Detail(name="List Property Binding", category="Binding", classes = {PersonEditor.class, Person.class, Kid.class}, icon = "listpropertybinding")
public class ListPropertyBindingExample implements EntryPoint, IsWidget {
  interface Driver extends SimpleBeanEditorDriver<Person, PersonEditor> {}
  
  private Driver driver = GWT.create(Driver.class);
  
  @Override
  public Widget asWidget() {
    FramedPanel panel = new FramedPanel();
    panel.setHeadingText("Model with List Property");
    panel.setBodyBorder(false);
    panel.setWidth(400);
    panel.addStyleName("margin-10");
    
    PersonEditor personEditor = new PersonEditor();
    driver.initialize(personEditor);
    
    panel.setWidget(personEditor);
    panel.addButton(new TextButton("Save", new SelectHandler() {
      @Override
      public void onSelect(SelectEvent event) {
        driver.flush();
      }
    }));
    
    Person person = new Person("Darrell Meyer", "Sencha Inc", "GXT", "Washington, DC", 9.99);

    List<Kid> kids = new ArrayList<Kid>();
    kids.add(new Kid("Alec", 4, new DateWrapper(2004, 1, 1).asDate()));
    kids.add(new Kid("Lia", 2, new DateWrapper(2005, 1, 1).asDate()));
    kids.add(new Kid("Andrew", 1, new DateWrapper(2007, 1, 1).asDate()));
    person.setKids(kids);
    
    driver.edit(person);
    
    return panel;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

}
