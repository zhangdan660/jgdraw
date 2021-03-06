/*
 * Created on 15.03.2006
 *
 */
package org.jdesktop.swingx.decorator;

import java.util.Comparator;
import java.util.List;

/**
 * A column and how its sorted.
 */
public class SortKey {
    private final SortOrder sortOrder;
    private final int column;
    private final Comparator comparator;
    
    /**
     * @param sortOrder one of {@link SortOrder#ASCENDING},
     *     {@link SortOrder#DESCENDING} or {@link SortOrder#UNSORTED}.
     * @param column a column in terms of <strong>model</strong> index.
     */
    public SortKey(SortOrder sortOrder, int column) {
        this(sortOrder, column, null);
    }

    /**
     * @param sortOrder one of {@link SortOrder#ASCENDING},
     *     {@link SortOrder#DESCENDING} or {@link SortOrder#UNSORTED}.
     * @param column a column in terms of <strong>model</strong> index.
     * @param comparator the comparator to use with this sort.
     */
    public SortKey(SortOrder sortOrder, int column, Comparator comparator) {
        if(sortOrder == null) throw new IllegalArgumentException();
        if(column < 0) throw new IllegalArgumentException();
        this.column = column;
        this.comparator = comparator;
        this.sortOrder = sortOrder;
    }

    /**
     * The sort order, ascending, descending or unsorted.
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    /**
     * The sorting column in terms of <strong>model</strong> index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * The comparator to use, might be null.
     */
    public Comparator getComparator() {
        return comparator;
    }
    /** {@inheritDoc} */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SortKey sortKey = (SortKey) o;

        if (column != sortKey.column) return false;
        if (sortOrder != null ? !sortOrder.equals(sortKey.sortOrder) : sortKey.sortOrder != null) return false;

        return true;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        int result;
        result = (sortOrder != null ? sortOrder.hashCode() : 0);
        result = 29 * result + column;
        return result;
    }
    
//---------------------- static utility methods
    
    public static SortKey getFirstSortingKey(List<? extends SortKey> keys) {
        for (SortKey key : keys) {
            if (key.getSortOrder().isSorted()) {
                return key;
            }
        }
        return null;
    }

    public static SortKey getFirstSortKeyForColumn(List<? extends SortKey> keys, int modelColumn) {
        for (SortKey key : keys) {
            if (key.getColumn() == modelColumn) {
                return key;
            }
        }
        return null;
    }

}
