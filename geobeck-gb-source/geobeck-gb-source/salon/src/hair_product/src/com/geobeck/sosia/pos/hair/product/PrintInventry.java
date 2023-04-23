/*
 * PrintInventry.java
 *
 * Created on 2008/09/11, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.commodity.MstSuppliers;
import com.geobeck.sosia.pos.master.product.MstPlaces;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import java.sql.SQLException;

/**
 *
 * @author s_matsumura
 */
public class PrintInventry {
    
    /** édì¸êÊ */
    protected	MstSuppliers	supplier		=	null;
    /**  íuÇ´èÍ */
    protected	MstPlaces		place			=	null;
    
    /** Creates a new instance of PrintInventry */
    public PrintInventry() {
        super();
    }
    
    public MstSuppliers getSuppliers() throws SQLException {
        if(supplier == null) {
            supplier	=	new MstSuppliers();
            
            ConnectionWrapper con = SystemInfo.getConnection();
            try {
                supplier.getSupplierList();
                supplier.load(con, true);
            } finally {
                con.close();
            }
        }
        
        return supplier;
    }
    
    public MstPlaces getPlaces() throws SQLException {
        if(place == null) {
            ConnectionWrapper con = SystemInfo.getConnection();
            place	=	new MstPlaces();
            place.getPlacesList();
            place.Placeload(con,true);
        }
        
        return place;
    }
}
