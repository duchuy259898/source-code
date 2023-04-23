/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.reservation.api;

import java.util.ArrayList;

/**
 *
 * @author ivs
 */
public class IspotReserverCancelResultWSI {
   private CancelResult results;
    private ArrayList<String> errors;

    public CancelResult getResults() {
        return results;
    }

    public void setResults(CancelResult results) {
        this.results = results;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
    /**
     * 
     * @param errors 
     */
    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
