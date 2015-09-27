package com.winestory.serverside.framework.helper;

import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.security.*;
import com.winestory.serverside.framework.security.Signer;
import org.apache.log4j.Logger;



import java.io.IOException;
import java.security.*;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;

/**
 * Created by ngzhongqin on 8/9/15.
 */
public class PayPalHelper {
    private Logger logger = Logger.getLogger(PayPalHelper.class);

    public PayPalHelper(){}

    public String getPayPalNonEncryptButtonValue(OrderVO orderVO){
        String returnString = "{:business=>\"test-orders@winestory.sg\", :cmd=>\"_cart\", :upload=>1, :currency_code=>\"SGD\", " +
                ":return=>\"http://localhost:3000/store\", :invoice=>5, " +
                ":notify_url=>\"http://localhost:3000/payment_notifications?secret=foobar\", :cert_id=>\"SVZPZ7EKAFGVU\", \"amount_1\"=>#<BigDecimal:7ff9a54f2120,'0.72E2',9(36)>, " +
                "\"item_name_1\"=>\"La Petite Ferme Cabernet Sauvignon\", \"item_number_1\"=>1, \"quantity_1\"=>2}";

        return returnString;
    }



    public String getPayPalEncryptButtonValue2(OrderVO orderVO){
        String returnString = "-----BEGIN PKCS7-----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-----END PKCS7-----";
        return returnString;
    }

    public String getPayPalEncryptButtonValue(OrderVO orderVO){

        String business_email = "test-orders@winestory.sg";
        String cmd = "_cart";
        String upload = "1";
        String currency_code = "SGD";
        String return_url = "http://localhost:3000/store";
        String invoice = "5";
        String notify_url = "http://localhost:3000/payment_notifications?secret=foobar";
        String cert_id = "SVZPZ7EKAFGVU";
        String amount_1 = "2";
        String item_name_1 = "La Petite Ferme Cabernet Sauvignon";
        String item_number_1 = "1";
        String quantity_1 = "2";

        String returnString = "{" +
                ":business=>\"" +business_email+ "\", " +
                ":cmd=>\"" +cmd+ "\", "+
                ":upload=>\"" + upload +"\", " +
                ":currency_code=>\""+ currency_code +"\", " +
                ":return=>\"" +return_url+ "\", " +
                ":invoice=>\"" + invoice + "\", " +
                ":notify_url=>\""+notify_url +"\", " +
                ":cert_id=>\""+cert_id+"\", "+
                ":amount_1=>\""+amount_1+"\", "+
                ":item_name_1=>\""+item_name_1+"\", "+
                ":item_number_1=>\""+item_number_1+"\", "+
                ":quantity_1=>\""+quantity_1+"\""+
                                "}";


        logger.info("unencrypted String is:"+returnString);


        Signer signer = new Signer();
        String signed = signer.sign(returnString);

       signed = signer.encode(signed);

//        PKCS7Signer pkcs7Signer = new PKCS7Signer();
//        String enc = pkcs7Signer.enc(returnString);

        return signed;
    }

}


