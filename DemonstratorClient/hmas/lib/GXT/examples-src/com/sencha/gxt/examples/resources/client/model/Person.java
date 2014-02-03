/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.examples.resources.client.model;

import java.util.ArrayList;
import java.util.List;

public class Person {
  private String name;
  private String company;
  private String product;
  private String location;
  private double income;
  private List<Kid> kids = new ArrayList<Kid>();
  
  public List<Kid> getKids() {
    return kids;
  }

  public void setKids(List<Kid> kids) {
    this.kids = kids;
  }

  public Person(String name, String company, String product, String location, double income) {
    setName(name);
    setCompany(company);
    setProduct(product);
    setLocation(location);
    setIncome(income);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public double getIncome() {
    return income;
  }

  public void setIncome(double income) {
    this.income = income;
  }

}