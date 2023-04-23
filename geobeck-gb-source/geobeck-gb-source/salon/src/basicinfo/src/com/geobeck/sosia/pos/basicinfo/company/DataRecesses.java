/*
 * DataRecesses.java
 *
 * Created on 2009/04/30, 14:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sql.ConnectionWrapper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author takeda
 */
public class DataRecesses extends ArrayList<DataRecess>{
    
    /** Creates a new instance of DataRecesses */
    public DataRecesses() {
    }
    
    // �ǂݍ���ł���x�e�f�[�^���w��̋x�eID�̃f�[�^�����o��
    public DataRecess getRecess(Integer recessId){
        for( DataRecess recess : this ){
            if( recess.getRecessId().equals(recessId) ){
                return recess;
            }
        }
        return null;
    }    

    // �w�肵�����Ԃ��A�x�e���ԓ����𒲍�
    public boolean inRange(String time)
    {
        for( DataRecess recess : this ){
            if( recess.inRange(time) ) return true;
        }
        return false;
    }

    public List inRangeEx(String time)
    {
        List result = new ArrayList();
        result.add(false);
        result.add(null);

        for( DataRecess recess : this ){
            if ( recess.inRange(time) ) {
                result.set(0, true);
                result.set(1, recess);
            }
        }
        return result;
    }
    
    // �w�肵�����Ԕ͈͓��ɋx�e���Ԃ��܂܂�邩�𒲍�
    public boolean inRange(String startTime, String endTime) {
        for( DataRecess recess : this ){
            if( recess.inRange(startTime, endTime) ) return true;
        }
        return false;
    }
    
    // �o�^
    void regist(ConnectionWrapper con) throws SQLException {
        for( DataRecess recess : this ){
            recess.regist(con);
        }
    }

}
