/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.util;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.postnet.POSTNETBean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

/**
 *
 * @author adrian
 */
public class BarcodeImage {
    
    
//    * org.krysalis.barcode4j.impl.codabar.CodabarBean
//    * org.krysalis.barcode4j.impl.code128.Code128Bean
//    * org.krysalis.barcode4j.impl.code39.Code39Bean
//    * org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean
//    * org.krysalis.barcode4j.impl.postnet.POSTNETBean
//    * org.krysalis.barcode4j.impl.upcean.EAN13Bean
//    * org.krysalis.barcode4j.impl.upcean.EAN8Bean
//    * org.krysalis.barcode4j.impl.upcean.UPCABean
//    * org.krysalis.barcode4j.impl.upcean.UPCEBean
    
    /**
     *
     * @param value
     * @return
     */
        
    public static Image getBarcodeCodabar(String value) {
        AbstractBarcodeBean barcode = new CodabarBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
        /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeEAN128(String value) {
        AbstractBarcodeBean barcode = new EAN128Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    

    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeCode39(String value) {
        AbstractBarcodeBean barcode = new Code39Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeInterleaved2Of5(String value) {
        AbstractBarcodeBean barcode = new Interleaved2Of5Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodePOSTNET(String value) {
        AbstractBarcodeBean barcode = new POSTNETBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeUPCA(String value) {
        AbstractBarcodeBean barcode = new UPCABean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeUPCE(String value) {
        AbstractBarcodeBean barcode = new UPCEBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeEAN13(String value) {
        AbstractBarcodeBean barcode = new EAN13Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcodeEAN8(String value) {
        AbstractBarcodeBean barcode = new EAN8Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

        /**
     *
     * @param value
     * @return
     */
    public static Image getBarcode39(String value) {
        value = "*"+value+"*";
        AbstractBarcodeBean barcode = new Code39Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode); 
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static Image getBarcode128(String value) {
        AbstractBarcodeBean barcode = new Code128Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        return getBarcode(value, barcode); 
    }
    
    private static Image getBarcode(String value, AbstractBarcodeBean barcode) {
        
        barcode.setModuleWidth(1.0); 
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);                
        BarcodeDimension dim = barcode.calcDimensions(value);
        int width = (int) dim.getWidth(0) + 20;
        int height = (int) dim.getHeight(0);  
        double scale = (120d / 72d);
        
        BufferedImage imgtext = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
        BitmapCanvasProvider provider =    new BitmapCanvasProvider(     601,     BufferedImage.TYPE_BYTE_BINARY,     true,     0);
        Graphics2D g2d = imgtext.createGraphics();
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
       // g2d.scale(scale, scale);
      //  g2d.drawRenderedImage(imgtext, null);
       // g2d.setTransform(g2d.getTransform());
        try {
            barcode.generateBarcode(provider, value);
        } catch (IllegalArgumentException e) {
            g2d.drawRect(0, 0, width - 1, height - 1);
            g2d.drawString(value, 2, height - 3);
        }
        //g2d.scale(scale, scale);
       // g2d.drawRenderedImage(imgtext, null);
       // g2d.setTransform(g2d.getTransform());
        g2d.dispose();
        
        return provider.getBufferedImage();  
    }
}
