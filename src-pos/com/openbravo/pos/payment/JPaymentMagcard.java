//    uniCenta oPOS  - Touch Friendly Point Of Sale
//    Copyright (c) 2009-2014 uniCenta
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

package com.openbravo.pos.payment;

import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author  adrianromero
 */
public class JPaymentMagcard extends javax.swing.JPanel implements JPaymentInterface {
    
    private PaymentPanel m_cardpanel;
    private final PaymentGateway m_paymentgateway;
    private final JPaymentNotifier m_notifier;
    private String current_reader;
    private String transaction;
    private double total;
    
    /** Creates new form JPaymentMagcard
     * @param app
     * @param notifier */
    public JPaymentMagcard(AppView app, JPaymentNotifier notifier) {
        
        initComponents();   
        
        m_notifier = notifier;
        
        m_paymentgateway = PaymentGatewayFac.getPaymentGateway(app.getProperties());
        current_reader = app.getProperties().getProperty("payment.magcardreader");
        
        if (m_paymentgateway == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));            
        } else {           
            // Se van a poder efectuar pagos con tarjeta
           m_cardpanel = PaymentPanelFac.getPaymentPanel(current_reader, notifier);
            add(m_cardpanel.getComponent(), BorderLayout.CENTER);
            jlblMessage.setText(null);
            // jlblMessage.setText(AppLocal.getIntString("message.nocardreader"));
        }
    }
    
    /**
     *
     * @param customerext
     * @param dTotal
     * @param transID
     */
    @Override
    public void activate(CustomerInfoExt customerext, double dTotal, String transID) {   
        this.transaction = transID;
        this.total = dTotal;

        if (m_cardpanel == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));  
            m_notifier.setStatus(false, false);
        } else {
            jlblMessage.setText(null);
            m_cardpanel.activate(transaction, dTotal); 
            // The cardpanel sets the status
        }
    }

    /**
     *
     * @return
     */
    @Override
    public PaymentInfo executePayment() {
        PaymentInfoMagcard payinfo = m_cardpanel.getPaymentInfoMagcard();
        jlblMessage.setText("Processing Transaction "+payinfo.getTransactionID()+"\nPlease Wait...");
        revalidate();
        m_paymentgateway.execute(payinfo);
        
        if (payinfo.isPaymentOK()) {
            jlblMessage.setText("Transaction ID: "+payinfo.getTransactionID()+ "APPROVED!");
            revalidate();
            return payinfo;
        } else {
            JOptionPane.showMessageDialog(getRootPane(),payinfo.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            jlblMessage.setText(payinfo.getMessage());
            revalidate();
            return null;
        }
    }  

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }
    
    /**
     *
     * @param transid
     */
    public void setTransaction(String transid){
        transaction = transid;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlblMessage = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        manualCard = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(300, 40));
        setPreferredSize(new java.awt.Dimension(300, 40));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(290, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(290, 100));

        jlblMessage.setBackground(new java.awt.Color(224, 223, 227));
        jlblMessage.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jlblMessage.setLineWrap(true);
        jlblMessage.setWrapStyleWord(true);
        jlblMessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jlblMessage.setFocusable(false);
        jlblMessage.setPreferredSize(new java.awt.Dimension(290, 37));
        jlblMessage.setRequestFocusEnabled(false);

        manualCard.setText("Manual Card Entry");
        manualCard.setPreferredSize(new java.awt.Dimension(139, 20));
        manualCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualCardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(manualCard, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(manualCard, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void manualCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualCardActionPerformed
        switchReader(total);
    }//GEN-LAST:event_manualCardActionPerformed
    
    public void switchReader(double total) {
        
       if(manualCard.getText().equals("Manual Card Entry"))
       {
           current_reader = "Keyboard";
           manualCard.setText("Swipe Card Entry");
       }
       else
       {
           current_reader = "Generic";
           manualCard.setText("Manual Card Entry");
       }
       
       remove(m_cardpanel.getComponent());
        
        m_cardpanel = PaymentPanelFac.getPaymentPanel(current_reader, m_notifier);
        add(m_cardpanel.getComponent(), BorderLayout.CENTER);

        jlblMessage.setText(null);
        m_cardpanel.activate(transaction, total);
        revalidate();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextArea jlblMessage;
    private javax.swing.JButton manualCard;
    // End of variables declaration//GEN-END:variables
    
}
