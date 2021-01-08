package com.github.opticyclic.xml;

import org.jvnet.jaxb2_commons.lang.DefaultToStringStrategy;

/**
 * Clean up the JAXB toString generation by removing hash codes and fully qualified class names
 */
public class SimpleToStringStrategy extends DefaultToStringStrategy {

  @Override
  public boolean isUseIdentityHashCode() {
    return false;
  }

  @Override
  protected void appendClassName(StringBuilder buffer, Object object) {
    if(object != null) {
      buffer.append(getShortClassName(object.getClass()));
    }
  }
}