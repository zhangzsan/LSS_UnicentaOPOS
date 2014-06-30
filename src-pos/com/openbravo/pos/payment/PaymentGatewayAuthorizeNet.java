/**
 * <p>Title: AIM Java Version 1.4.1_02-b06</p>
 * <p>Description: Advanced Integration Method</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Authorize.Net</p>
 * @author Authorize.Net
 * @version 3.1
 */

/**
 *  Based on sample code and snipptes provided by:
 *  Patrick Phelan, phelan@choicelogic.com
 *  Roedy Green, Canadian Mind Products
 */

// Modifications by Adrian Romero & Mikel Irurita

package com.openbravo.pos.payment;

import com.openbravo.data.loader.LocalRes;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.util.AltEncrypter;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PaymentGatewayAuthorizeNet implements PaymentGateway {
    
    private static String ENDPOINTADDRESS;
    private static final String OPERATIONVALIDATE = "AUTH_CAPTURE";
    private static final String OPERATIONREFUND = "CREDIT";
    private static final String APPROVED = "1";
    
    private String m_sCommerceID;
    private String m_sCommercePassword;
    private boolean m_bTestMode;

    /** Creates a new instance of PaymentGatewayAuthorizeNet
     * @param props */
    public PaymentGatewayAuthorizeNet(AppProperties props) {
        // Grab some configuration variables
        m_sCommerceID = props.getProperty("payment.commerceid");
        
        AltEncrypter cypher = new AltEncrypter("cypherkey" + props.getProperty("payment.commerceid"));
        this.m_sCommercePassword = cypher.decrypt(props.getProperty("payment.commercepassword").substring(6));

        m_bTestMode = Boolean.valueOf(props.getProperty("payment.testmode"));
        
        ENDPOINTADDRESS = (m_bTestMode) 
                ? "https://test.authorize.net/gateway/transact.dll"
                : "https://cardpresent.authorize.net/gateway/transact.dll";
    }  
    
    public PaymentGatewayAuthorizeNet() {
        
    }

    @Override
    public void execute(PaymentInfoMagcard payinfo) {
// JG 16 May 12 use StringBuilder in place of StringBuilder
        StringBuilder sb = new StringBuilder();
        try {
            //test -> login:44CWBFp7wh9 / pass:43P7s8qb84CVT9Jx
            sb.append("x_cpversion=1.0");
            
            sb.append("&x_market_type=2");
            
//            sb.append("&x_device_type=1");  // JG 1 Oct 13 - changed from 1 unknown to PC based 5
            sb.append("&x_device_type=5"); // PC
           // sb.append("&x_device_type=10");
            
            sb.append("&x_login=");        
            sb.append(URLEncoder.encode(m_sCommerceID, "UTF-8"));

            sb.append("&x_tran_key=");
            sb.append(URLEncoder.encode(m_sCommercePassword, "UTF-8"));
             
            sb.append("&x_amount=");
            NumberFormat formatter = new DecimalFormat("0000.00");
            String amount = formatter.format(Math.abs(payinfo.getTotal()));
            sb.append(URLEncoder.encode(amount.replace(',', '.'), "UTF-8"));
            
            if (payinfo.getTrack1(true) == null) {
                sb.append("&x_card_num=");
                sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));

                sb.append("&x_exp_date=");
                String tmp = payinfo.getExpirationDate();
                sb.append(URLEncoder.encode(tmp, "UTF-8"));

                String[] cc_name = payinfo.getHolderName().split(" ");
                sb.append("&x_first_name=");
                if (cc_name.length > 0) {
                   sb.append(URLEncoder.encode(cc_name[0], "UTF-8"));
                }
                sb.append("&x_last_name=");
                if (cc_name.length > 1) {
                   sb.append(URLEncoder.encode(cc_name[1], "UTF-8"));
                }
            } else {
                // Example Track1
                // %B4111111111111111^PADILLA VISDOMINE/LUIS^0905123000000000000002212322222?5
                sb.append("&x_track1=");
                sb.append(payinfo.getTrack1(false));
                sb.append("&x_track2=");
                sb.append(payinfo.getTrack2(false));
            }
            
            sb.append("&x_method=CC");
            sb.append("&x_version=3.1");
            sb.append("&x_delim_data=TRUE");
            sb.append("&x_delim_char=|");
            sb.append("&x_relay_response=FALSE");
            sb.append("&x_test_request=");
            sb.append(m_bTestMode);
            
            //PAYMENT
            if (payinfo.getTotal() >= 0.0) {
                sb.append("&x_type=");
                sb.append(OPERATIONVALIDATE);
                //sb.append("&x_card_code=340"); //CCV
            }
            //REFUND
            else {
                sb.append("&x_type=");
                sb.append(OPERATIONREFUND);
                sb.append("&x_trans_id=");
                sb.append(payinfo.getTransactionID());
                System.out.println(payinfo.getTransactionID());
            }

            // open secure connection
            URL url = new URL(ENDPOINTADDRESS);

            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            System.out.println(sb.toString());
            // not necessarily required but fixes a bug with some servers
            // JG May 12 added try-with-resources
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                out.write(sb.toString().getBytes());
                out.flush();
            }
            String returned;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                returned = in.readLine();
            }
            String [] responses = returned.split("\\|");
            System.out.println(Arrays.toString(responses));
            AuthorizeNetParser anp = new AuthorizeNetParser(returned);
            Map props = anp.splitXML();
            anp.parseData(responses);
            
            if (anp.getResult().equals(LocalRes.getIntString("button.ok"))) {
                if (APPROVED.equals(props.get("ResponseCode"))) {
                    //Transaction approved
                    payinfo.paymentOK(props.get("AuthCode").toString(), props.get("TransID").toString(), returned);
                } else {                            
                    StringBuilder errorLine = new StringBuilder();
                    errorLine.append(props.get("Description").toString());
                    errorLine.append("\n");
                    errorLine.append(getAVSResponseMessage(props.get("AVSResultCode").toString()));
                    //Transaction declined
                    /*if (anp.getNumErrors()>0) {
                        
                        for (int i=1; i<=anp.getNumErrors(); i++) {
                            errorLine.append(props.get("ErrorCode"+Integer.toString(i)));
                            errorLine.append(": ");
                            errorLine.append(props.get("ErrorText"+Integer.toString(i)));
                            errorLine.append("\n");
                        }
                    }*/
                   // JOptionPane.showMessageDialog(null, responses[4]);
                    
                    payinfo.paymentError(errorLine.toString(), "");
                }
            }
            else {
                //JOptionPane.showMessageDialog(null, responses[4]);
                payinfo.paymentError(returned, "");
            }
           
// JG 16 May 12 use multicatch
        } catch (UnsupportedEncodingException | MalformedURLException eUE) {
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eUE.getMessage());
        } catch(IOException e){
            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), e.getMessage());
        }
    }
    
    public static String getAVSResponseMessage(String msg)
    {
        char c = msg.charAt(0);
        
        switch(c)
        {
            case 'A': return "A Address (Street) matches, ZIP does not";
            case 'B': return "Address information not provided for AVS check";
            case 'E': return "AVS error";
            case 'G': return "Non-U.S. Card Issuing Bank";
            case 'N': return "No Match on Address (Street) or ZIP";
            case 'P': return "AVS not applicable for this transaction";
            case 'R': return "System unavailable or timed out";
            case 'S': return "Service not supported by issuer";
            case 'U': return "Address information is unavailable";
            case 'W': return "9 digit ZIP matches, Address (Street) does not";
            case 'X': return "Address (Street) and 9 digit ZIP match";
            case 'Y': return "Address (Street) and 5 digit ZIP match";
            case 'Z': return "5 digit ZIP matches, Address (Street) does not";
            default: return "Unknown AVS Response Message";
        }
    }
    
    private class AuthorizeNetParser extends DefaultHandler {
    
    private SAXParser m_sp = null;
    final private Map props = new HashMap();
    private String text;
    final private InputStream is;
    private String result;
    private int numMessages = 0;
    private int numErrors = 0;
    
    public AuthorizeNetParser(String input) {
        is = new ByteArrayInputStream(input.getBytes());
    }
    
    public void parseData(String[] in)
    {
        System.out.println(in.length);
        for(int i = 0; i < in.length; i++)
        {
            System.out.println(in[i]);
            switch(i)
            {
            case 0:
                props.put("ResponseCode", in[i]);
                break;
            case 2:
                props.put("Code", in[i]);
                break;
            case 3:
                props.put("Description", in[i]);
                break;
            case 4:
                props.put("AuthCode", in[i]);
                break;
            case 5:
                props.put("AVSResultCode", in[i]);
                break;
            case 6:
                props.put("TransID", in[i]);
                break;
            case 38:
                props.put("CVVResultCode", in[i]);
                break;
            }
        }
    }
    
    public Map splitXML(){
        try {
            if (m_sp == null) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                m_sp = spf.newSAXParser();
            }
            m_sp.parse(is, this);
        } catch (ParserConfigurationException ePC) {
            result = LocalRes.getIntString("exception.parserconfig");
        } catch (SAXException eSAX) {
            result = LocalRes.getIntString("exception.xmlfile");
        } catch (IOException eIO) {
            result = LocalRes.getIntString("exception.iofile");
        }
        result = LocalRes.getIntString("button.ok");
        return props;
    }
      
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
// JG 16 May 12 use switch
            switch (qName) {
                    case "ResponseCode":
                        //props.put("ResponseCode", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "ErrorCode":
                        numErrors++;
                        props.put("ErrorCode"+Integer.toString(numErrors), URLDecoder.decode(text, "UTF-8"));
                        text = "";
                        break;
                    case "ErrorText":
                        props.put("ErrorText"+Integer.toString(numErrors), URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "Code":
                        numMessages++;
                       // props.put("Code"+Integer.toString(numMessages), URLDecoder.decode(text, "UTF-8"));
                        text = "";
                        break;
                    case "Description":
                        //props.put("Description"+Integer.toString(numMessages), URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "AuthCode":
                        //props.put("AuthCode", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "AVSResultCode":
                        //props.put("AVSResultCode", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "CVVResultCode":
                        //props.put("CVVResultCode", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "TransID":
                        //props.put("TransID", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "RefTransID":
                        props.put("RefTransID", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "TransHash":
                        props.put("TransHash", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "TestMode":
                        props.put("TestMode", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                    case "UserRef":
                        props.put("UserRef", URLDecoder.decode(text, "UTF-8"));
                        text="";
                        break;
                }
        }
        catch(UnsupportedEncodingException eUE){
            result = eUE.getMessage();
        }
    }
    
    @Override
    public void startDocument() throws SAXException {
        text = new String();
    }

    @Override
    public void endDocument() throws SAXException {
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (text!=null) {
            text = new String(ch, start, length);
        }
    }
    
    public String getResult(){
        return this.result;
    }
    
    public int getNumErrors(){
        return numErrors;
    }
    
    public int getNumMessages(){
        return numMessages;
    }
    
 }

}