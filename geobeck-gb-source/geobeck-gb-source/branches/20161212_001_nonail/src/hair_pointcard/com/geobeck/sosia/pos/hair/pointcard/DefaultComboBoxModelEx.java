/*
 * DefaultComboBoxModelEx.java
 *
 * Created on 2008/09/12, 14:12
 *
 * 同一表示内容を別のアイテムと登録する事が出来るコンボボックスモデル
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author trino
 */
public class DefaultComboBoxModelEx extends AbstractListModel implements MutableComboBoxModel, Serializable {
    ArrayList<ComboBoxItem> objects;
    Object selectedObject;
    
    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public DefaultComboBoxModelEx() {
        objects = new ArrayList<ComboBoxItem>();
        
        System.out.println( "Creating DefaultComboBoxModelEx" );
        
    }
    
    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an array of objects.
     *
     * @param items  an array of Object objects
     */
    public DefaultComboBoxModelEx(final Object items[]) {
        objects = new ArrayList<ComboBoxItem>();
        objects.ensureCapacity( items.length );
        
        int i,c;
        for ( i=0,c=items.length;i<c;i++ ) {
            objects.add( new ComboBoxItem( (String)items[i], i ) );
        }
        
        if ( getSize() > 0 ) {
            selectedObject = (ComboBoxItem)getElementAt( 0 );
        }
    }
    
    // implements javax.swing.ComboBoxModel
    /**
     * Set the value of the selected item. The selected item may be null.
     * <p>
     * @param anObject The combo box value or null for no selection.
     */
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
            selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }
    
    // implements javax.swing.ComboBoxModel
    public Object getSelectedItem() {
        return selectedObject;
    }
    
    // implements javax.swing.ListModel
    public int getSize() {
        return objects.size();
    }
    
    // implements javax.swing.ListModel
    public Object getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
            return objects.get(index);
        else
            return null;
    }
    
    /**
     * Returns the index-position of the specified object in the list.
     *
     * @param anObject
     * @return an int representing the index position, where 0 is
     *         the first position
     */
    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }
    
    // implements javax.swing.MutableComboBoxModel
    public void addElement(Object anObject) {
        objects.add( (ComboBoxItem)anObject);
        fireIntervalAdded(this,objects.size()-1, objects.size()-1);
        if ( objects.size() == 1 && selectedObject == null && anObject != null ) {
            setSelectedItem( anObject );
        }
    }
    
    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(Object anObject,int index) {
        objects.add( index, (ComboBoxItem)anObject);
        fireIntervalAdded(this, index, index);
    }
    
    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            } else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }
        
        objects.remove(index);
        
        fireIntervalRemoved(this, index, index);
    }
    
    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }
    
    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if ( objects.size() > 0 ) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            objects.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }
    
    private class ComboBoxItem {
        private String title;
        private int index;
        
        /** Creates a new instance of ComboBoxItem */
        public ComboBoxItem( String title, int index ) {
            this.setTitle(title);
            this.setIndex(index);
        }
        
        public String toString() {
            return getTitle();
        }
        
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public int getIndex() {
            return index;
        }
        
        public void setIndex(int index) {
            this.index = index;
        }
        
    }
}
