//package com.winestory.serverside.framework.security;
//
//
//import org.apache.log4j.Logger;
//
//import java.io.File;
//import java.io.IOException;
//import java.security.*;
//import java.security.cert.CertStoreException;
//import java.security.cert.CertificateException;
//
///**
// */
//public class PKCS7Signer {
//    public Logger logger = Logger.getLogger(PKCS7Signer.class);
//
//    public String enc(String data) {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        String keyPath = "conf/test_certs/app_key.pem";
//        String certPath = "conf/test_certs/app_cert.pem";
//        String paypalCertPath = "conf/test_certs/paypal_cert.pem";
//        String keyPass = "password";
//
//
//        ClientSide client_side = new ClientSide(keyPath, certPath, paypalCertPath, keyPass);
//        String result = null;
//        try {
//            result = client_side.getButtonEncryptionValue(data, keyPath, certPath, paypalCertPath, keyPass);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (UnrecoverableKeyException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (CertStoreException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//}
