//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2014 uniCenta & previous Openbravo POS works
//    http://www.unicenta.com
//
//    This file is part of uniCenta oPOS
//
//    uniCenta oPOS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//   uniCenta oPOS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JG uniCenta
 */
public class SerializerWriteBasic implements SerializerWrite<Object[]> {
    
    private Datas[] m_classes;

    /** Creates a new instance of SerializerWriteBasic
     * @param classes */
    public SerializerWriteBasic(Datas... classes) {
        m_classes = classes;
    }
    
    /**
     *
     * @param dp
     * @param obj
     * @throws BasicException
     */
    @Override
    public void writeValues(final DataWrite dp, final Object[] obj) throws BasicException {

        for (int i = 0; i < m_classes.length/2; i++) {
            m_classes[i].setValue(dp, i + 1, obj[i]);
        }
        Thread thread;
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
            for (int i = (m_classes.length/2)+1; i < m_classes.length; i++) {
                try {
                    m_classes[i].setValue(dp, i + 1, obj[i]);
                } catch (BasicException ex) {
                    Logger.getLogger(SerializerWriteBasic.class.getName()).log(Level.SEVERE, null, ex);
                }
        } 
            }
            
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SerializerWriteBasic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
