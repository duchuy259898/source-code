/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.mail;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;

/**
 *
 * @author geobeck
 */
public class DBUtil {

    public static boolean isErrorString(String s) {

        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {

            try {
                con.execute("drop table wk_string_check;");
            } catch(Exception e) {
            }

            con.execute("create temporary table wk_string_check(col text);");
            con.executeUpdate("insert into wk_string_check values('" + s + "')");

        } catch(Exception e) {

            // ÉGÉâÅ[
            result = true;

        } finally {
            try {
                con.close();
            } catch (Exception e) {
            }
        }

        return result;
    }
}
