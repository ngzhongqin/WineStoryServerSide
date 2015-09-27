package com.winestory.serverside.framework.security;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.encoders.HexEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Signer {
    public Logger logger = Logger.getLogger(Signer.class);
    public Signer(){}

    public  String sign(String data){
        Security.addProvider(new BouncyCastleProvider());

        String app_cert_string = "conf/test_certs/app_cert.pem";
        String app_key_string = "conf/test_certs/app_key.pem";

        String message = "hello world";
        File app_cert = new File(app_cert_string);
        File app_key = new File(app_key_string);



        KeyPair keyPair = null;
        try {
            keyPair = readKeyPair(app_key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Signature signature = null;
        try {
            signature = Signature.getInstance("SHA256WithRSAEncryption");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            signature.initSign(keyPair.getPrivate());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            signature.update(message.getBytes());
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        byte [] signatureBytes = new byte[0];
        try {
            signatureBytes = signature.sign();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        logger.info("signed: "+new String(Hex.encode(signatureBytes)));





        Signature verifier = null;
        try {
            verifier = Signature.getInstance("SHA256WithRSAEncryption");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            verifier.initVerify(keyPair.getPublic());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            verifier.update(message.getBytes());
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        try {
            if (verifier.verify(signatureBytes)) {
                logger.info("Signature is valid");
            } else {
                logger.info("Signature is invalid");
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        return new String(Hex.encode(signatureBytes));
    }

    private  KeyPair readKeyPair(File privateKey) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader);
        try {
            return (KeyPair) r.readObject();
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }

    private  PrivateKey readKeyPair(File privateKey, char [] keyPassword) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader, new DefaultPasswordFinder(keyPassword));
        try {
            return (PrivateKey) r.readObject();
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }

    private class DefaultPasswordFinder implements PasswordFinder {

        private final char [] password;

        private DefaultPasswordFinder(char [] password) {
            this.password = password;
        }

        @Override
        public char[] getPassword() {
            return Arrays.copyOf(password, password.length);
        }
    }

    public String encode(String decrypted){
        String encoded_string = null;
        Cipher cipher = null;
        try {
            String paypal_cert_string = "conf/test_certs/paypal_cert.pem";
            File paypal_cert = new File(paypal_cert_string);
            X509Certificate cert = readKeyPair2(paypal_cert);

            logger.info("cert.getPublicKey():"+cert.getPublicKey());

            cipher = Cipher.getInstance("DESede/ECB/Nopadding","BC");
            cipher.init(Cipher.ENCRYPT_MODE,cert);
            byte[] encryptedSignature= cipher.doFinal(decrypted.getBytes());

            HexEncoder hexEncoder =new HexEncoder();
            logger.info("decryptedSignature:"+encryptedSignature);
//            System.out.println("Decrypted signature (hex) = " + hexEncoder.encode(decryptedSignature,));
            System.out.println("Decrypted as String = " + new String(encryptedSignature, encryptedSignature.length-16, 16));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return encoded_string;
    }



    private  X509Certificate readKeyPair2(File privateKey) throws IOException {
        FileReader fileReader = new FileReader(privateKey);
        PEMReader r = new PEMReader(fileReader);
        try {
            return (X509Certificate) r.readObject();
        } catch (IOException ex) {
            throw new IOException("The private key could not be decrypted", ex);
        } finally {
            r.close();
            fileReader.close();
        }
    }
}