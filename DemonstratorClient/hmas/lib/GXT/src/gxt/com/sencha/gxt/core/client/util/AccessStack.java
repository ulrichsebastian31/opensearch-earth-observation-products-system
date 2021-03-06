/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.core.client.util;

import java.util.Stack;

public class AccessStack<T> {

  private Stack<T> stack = new Stack<T>();

  public void add(T item) {
    if (stack.contains(item)) {
      stack.remove(item);
    }
    stack.add(item);
    if (stack.size() > 10) {
      stack.remove(0);
    }
  }
  
  public void clear() {
    stack.clear();
  }
  
  public T get(int i) {
    return stack.get(i);
  }

  public Stack<T> getStack() {
    return stack;
  }

  public T next() {
    return stack.size() > 0 ? stack.pop() : null;
  }

  public void remove(T item) {
    stack.remove(item);
  }
  
  public int size() {
    return stack.size();
  }
}
