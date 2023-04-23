/*
 * LimitByteFilter.java
 *
 * Created on 2008/09/14, 20:27
 *
 * ���̓o�C�g���ł̃t�B���^�[
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import javax.swing.text.*;

/**
 *
 * @author takeda
 */
public class LimitByteFilter extends DocumentFilter {
    
    int limit;
    LimitByteFilter( int limit ){
        this.limit = limit; 
    }
    
    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) 
                    throws BadLocationException {
        if( string != null ){
            Document	doc			=	fb.getDocument();
            String      docText     = doc.getText( 0, doc.getLength() );
            String      delText     = doc.getText( offset, length );
            byte[]      b           = docText.getBytes();
            byte[]      delb        = delText.getBytes();
            byte[]      inb         = string.getBytes();
            int         afterLen    = b.length - delb.length + inb.length;
            
            if( afterLen  > limit ){
                // ���͉\�o�C�g���ɋ�؂�
                int oklen = inb.length - (afterLen - limit);
                if(oklen > 0){
                    while(true){
                        string = string.substring(0, string.length()-1);
                        if( string.getBytes().length <= oklen ){
                            break;
                        }
                    }
                }else{
                    string = null;
                }
            }
        }
        super.replace( fb, offset, length, string, attrs );
    }
}
