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
public class IspotReserverConfirmResultWSI {
    private ConfirmResult results;
    private ArrayList<String> errors;

    public ConfirmResult getResults() {
        return results;
    }

    public void setResults(ConfirmResult results) {
        this.results = results;
    }
    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
