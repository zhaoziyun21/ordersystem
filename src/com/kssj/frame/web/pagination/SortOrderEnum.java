package com.kssj.frame.web.pagination;

import java.io.Serializable;
import java.util.Iterator;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class SortOrderEnum implements Serializable{
  private static final long serialVersionUID = 42L;
  public static final SortOrderEnum DESCENDING = new SortOrderEnum(1, "descending");

  public static final SortOrderEnum ASCENDING = new SortOrderEnum(2, "ascending");

  static final SortOrderEnum[] ALL = { DESCENDING, ASCENDING };
  private final int enumCode;
  private final String enumName;

  private SortOrderEnum(int code, String name)
  {
    this.enumCode = code;
    this.enumName = name;
  }

  public int getCode()
  {
    return this.enumCode;
  }

  public String getName()
  {
    return this.enumName;
  }

  public static SortOrderEnum fromCode(int key)
  {
    for (int i = 0; i < ALL.length; i++)
    {
      if (key == ALL[i].getCode())
      {
        return ALL[i];
      }
    }

    return null;
  }

  public static SortOrderEnum fromCode(Integer key)
  {
    if (key == null)
    {
      return null;
    }

    return fromCode(key.intValue());
  }

  /** @deprecated */
  public static SortOrderEnum fromIntegerCode(Integer key)
  {
    return fromCode(key);
  }

  public static SortOrderEnum fromName(String code)
  {
    for (int i = 0; i < ALL.length; i++)
    {
      if (ALL[i].getName().equals(code))
      {
        return ALL[i];
      }
    }

    return null;
  }

  @SuppressWarnings("rawtypes")
public static Iterator iterator()
  {
    return new ArrayIterator(ALL);
  }

  public String toString()
  {
    return getName();
  }

  public boolean equals(Object o)
  {
    return this == o;
  }

  public int hashCode()
  {
    return new HashCodeBuilder(1123997057, -1289836553).append(this.enumCode).toHashCode();
  }
}
