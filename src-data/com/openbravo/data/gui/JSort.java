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

package com.openbravo.data.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.LocalRes;

/**
 *
 * @author JG uniCenta
 */
public class JSort extends JDialog {
    
    private ComparatorCreator m_cc;
    private Comparator m_Comparator;
        
    private JSort(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    private JSort(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }
    
    private Comparator init(ComparatorCreator cc) throws BasicException {
        
        initComponents();

        getRootPane().setDefaultButton(jcmdOK);   
        
        m_cc = cc;
        
        String[] sHeaders = m_cc.getHeaders();
        
        m_jSort1.removeAllItems();
        //m_jSort1.addItem("");
        for (int i = 0; i < sHeaders.length; i++) {
            m_jSort1.addItem(sHeaders[i]);
        }
        m_jSort1.setSelectedItem(1);
        
        m_jSort2.removeAllItems();
        m_jSort2.addItem("");
        for (int i = 0; i < sHeaders.length; i++) {
            m_jSort2.addItem(sHeaders[i]);
        }
        m_jSort2.setSelectedItem(0);
        
        m_jSort3.removeAllItems();
        m_jSort3.addItem("");
        for (int i = 0; i < sHeaders.length; i++) {
            m_jSort3.addItem(sHeaders[i]);
        }
        m_jSort3.setSelectedItem(0);
        
        m_Comparator = null;
        setVisible(true);       
        return m_Comparator;
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
       
    /**
     *
     * @param parent
     * @param cc
     * @return
     * @throws BasicException
     */
    public static Comparator showMessage(Component parent, ComparatorCreator cc) throws BasicException {
         
        Window window = getWindow(parent);      
        
        JSort myMsg;
        if (window instanceof Frame) { 
            myMsg = new JSort((Frame) window, true);
        } else {
            myMsg = new JSort((Dialog) window, true);
        }
        return myMsg.init(cc);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        m_jSort1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        m_jSort2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        m_jSort3 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(LocalRes.getIntString("caption.sort")); // NOI18N
        setResizable(false);

        jPanel1.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(LocalRes.getIntString("label.sortby")); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 20, 100, 25);

        m_jSort1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jSort1);
        m_jSort1.setBounds(110, 20, 230, 25);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(LocalRes.getIntString("label.andby")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 50, 100, 25);

        m_jSort2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jSort2);
        m_jSort2.setBounds(110, 50, 230, 25);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(LocalRes.getIntString("label.andby")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 80, 100, 25);

        m_jSort3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jSort3);
        m_jSort3.setBounds(110, 80, 230, 25);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jcmdOK.setText(LocalRes.getIntString("button.ok")); // NOI18N
        jcmdOK.setMaximumSize(new java.awt.Dimension(65, 33));
        jcmdOK.setMinimumSize(new java.awt.Dimension(65, 33));
        jcmdOK.setPreferredSize(new java.awt.Dimension(65, 33));
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);

        jcmdCancel.setText(LocalRes.getIntString("button.cancel")); // NOI18N
        jcmdCancel.setMaximumSize(new java.awt.Dimension(65, 33));
        jcmdCancel.setMinimumSize(new java.awt.Dimension(65, 33));
        jcmdCancel.setPreferredSize(new java.awt.Dimension(65, 33));
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-396)/2, (screenSize.height-199)/2, 396, 199);
    }// </editor-fold>//GEN-END:initComponents

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdCancelActionPerformed
        
        dispose();
        
    }//GEN-LAST:event_jcmdCancelActionPerformed
    
    public Comparator getNameComparator() {
        m_Comparator = m_cc.createComparator(new int[]{0});
        return m_Comparator;
    }
    
    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed

        int iSort1 = m_jSort1.getSelectedIndex()+1;
        int iSort2 = m_jSort2.getSelectedIndex();
        int iSort3 = m_jSort3.getSelectedIndex();
        
        if (iSort1 > 0 && iSort2 == 0 && iSort3 == 0) {
            m_Comparator = m_cc.createComparator(new int[] {iSort1 - 1});
            dispose();
        } else if (iSort1 > 0 && iSort2 > 0 && iSort3 == 0) {
            m_Comparator = m_cc.createComparator(new int[] {iSort1 - 1, iSort2 - 1});
            dispose();
        } else if (iSort1 > 0 && iSort2 > 0 && iSort3 > 0) {
            m_Comparator = m_cc.createComparator(new int[] {iSort1 - 1, iSort2 - 1,  iSort3 - 1});
            dispose();
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, LocalRes.getIntString("message.nosort"));
            msg.show(this);   
        }
  
    }//GEN-LAST:event_jcmdOKActionPerformed
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    private javax.swing.JComboBox m_jSort1;
    private javax.swing.JComboBox m_jSort2;
    private javax.swing.JComboBox m_jSort3;
    // End of variables declaration//GEN-END:variables
    
}
