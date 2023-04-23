/*
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.mail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author lvut
 */
public interface SelectImagesOpener
{
	public void addSelectedImage(String ImageCode, final HashMap<Object, ImageIcon> icons, final HashMap<Object, String> iconPaths, final LinkedHashMap<String,DataImage> imgMap);
}
