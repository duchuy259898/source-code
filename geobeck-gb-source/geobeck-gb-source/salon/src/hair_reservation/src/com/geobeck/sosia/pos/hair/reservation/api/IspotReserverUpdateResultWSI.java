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
public class IspotReserverUpdateResultWSI {
    private UpdateResult results;
    private  ArrayList<String> errors;

    public UpdateResult getResults() {
        return results;
    }

    public void setResults(UpdateResult results) {
        this.results = results;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    

  
   
}
