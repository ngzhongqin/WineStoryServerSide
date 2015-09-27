//package com.winestory.serverside.framework.security;
//
//        import java.io.*;
//        import java.security.*;
//        import java.security.cert.CertStore;
//        import java.security.cert.CertStoreException;
//        import java.security.cert.CertificateException;
//        import java.security.cert.CertificateFactory;
//        import java.security.cert.CollectionCertStoreParameters;
//        import java.security.cert.X509Certificate;
//        import java.util.ArrayList;
//        import java.util.Enumeration;
//
//        import com.sun.xml.internal.messaging.saaj.util.Base64;
//        import org.bouncycastle.cms.CMSEnvelopedData;
//        import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
//        import org.bouncycastle.cms.CMSException;
//        import org.bouncycastle.cms.CMSProcessableByteArray;
//        import org.bouncycastle.cms.CMSSignedData;
//        import org.bouncycastle.cms.CMSSignedDataGenerator;
//        import org.bouncycastle.openssl.PEMReader;
//
//
///**
// */
//public class ClientSide
//{
//    private String  keyPath;
//    private String  certPath;
//    private String  paypalCertPath;
//    private String  keyPass;
//
//    public ClientSide( String keyPath, String certPath, String paypalCertPath, String keyPass )
//    {
//        this.keyPath = keyPath;
//        this.certPath = certPath;
//        this.paypalCertPath = paypalCertPath;
//        this.keyPass = keyPass;
//    }
//
//    public String getButtonEncryptionValue(String _data, String _privateKeyPath, String _certPath, String _payPalCertPath,
//                                           String _keyPass) throws IOException,CertificateException,KeyStoreException,
//            UnrecoverableKeyException,InvalidAlgorithmParameterException,NoSuchAlgorithmException,
//            NoSuchProviderException,CertStoreException {
//        _data = _data.replace(',', '\n');
//        CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");
//
//        // Read the Private Key
////        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
////        ks.load( new FileInputStream(_privateKeyPath), _keyPass.toCharArray() );
////
////        String keyAlias = null;
////        Enumeration aliases = ks.aliases();
////        while (aliases.hasMoreElements()) {
////            keyAlias = (String) aliases.nextElement();
////        }
//
//        //PrivateKey privateKey = (PrivateKey) ks.getKey( keyAlias, _keyPass.toCharArray() );
//
//        KeyPair keyPair = readKeyPair(new File(this.keyPath));
//        PrivateKey privateKey = keyPair.getPrivate();
//
//        // Read the Certificate
//        X509Certificate certificate = (X509Certificate) cf.generateCertificate( new FileInputStream(_certPath) );
//
//        // Read the PayPal Cert
//        X509Certificate payPalCert = (X509Certificate) cf.generateCertificate( new FileInputStream(_payPalCertPath) );
//
//        // Create the Data
//        byte[] data = _data.getBytes();
//
//        // Sign the Data with my signing only key pair
//        CMSSignedDataGenerator signedGenerator = new CMSSignedDataGenerator();
//
//        signedGenerator.addSigner( privateKey, certificate, CMSSignedDataGenerator.DIGEST_SHA1 );
//
//        ArrayList certList = new ArrayList();
//        certList.add(certificate);
//        CertStore certStore = CertStore.getInstance( "Collection", new CollectionCertStoreParameters(certList) );
//        try {
//            signedGenerator.addCertificatesAndCRLs(certStore);
//        } catch (CMSException e) {
//            e.printStackTrace();
//        }
//
//        CMSProcessableByteArray cmsByteArray = new CMSProcessableByteArray(data);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            cmsByteArray.write(baos);
//        } catch (CMSException e) {
//            e.printStackTrace();
//        }
//        System.out.println( "CMSProcessableByteArray contains [" + baos.toString() + "]" );
//
//        CMSSignedData signedData = null;
//        try {
//            signedData = signedGenerator.generate(cmsByteArray, true, "BC");
//        } catch (CMSException e) {
//            e.printStackTrace();
//        }
//
//        byte[] signed = signedData.getEncoded();
//
//        CMSEnvelopedDataGenerator envGenerator = new CMSEnvelopedDataGenerator();
//        envGenerator.addKeyTransRecipient(payPalCert);
//        CMSEnvelopedData envData = null;
//        try {
//            envData = envGenerator.generate( new CMSProcessableByteArray(signed),
//                    CMSEnvelopedDataGenerator.DES_EDE3_CBC, "BC" );
//        } catch (CMSException e) {
//            e.printStackTrace();
//        }
//
//        byte[] pkcs7Bytes = envData.getEncoded();
//
//
//        return new String( DERtoPEM(pkcs7Bytes, "PKCS7") );
//
//    }
//
//    public static byte[] DERtoPEM(byte[] bytes, String headfoot)
//    {
//        ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
//        PrintWriter writer = new PrintWriter(pemStream);
//
//        byte[] stringBytes = Base64.encode(bytes);
//
//        System.out.println("Converting " + stringBytes.length + " bytes");
//
//        String encoded = new String(stringBytes);
//
//        if (headfoot != null) {
//            writer.print("-----BEGIN " + headfoot + "-----\n");
//        }
//
//        // write 64 chars per line till done
//        int i = 0;
//        while ((i + 1) * 64 < encoded.length()) {
//            writer.print(encoded.substring(i * 64, (i + 1) * 64));
//            writer.print("\n");
//            i++;
//        }
//        if (encoded.length() % 64 != 0) {
//            writer.print(encoded.substring(i * 64)); // write remainder
//            writer.print("\n");
//        }
//        if (headfoot != null) {
//            writer.print("-----END " + headfoot + "-----\n");
//        }
//        writer.flush();
//        return pemStream.toByteArray();
//    }
//
//
//    private KeyPair readKeyPair(File privateKey) throws IOException {
//        FileReader fileReader = new FileReader(privateKey);
//        PEMReader r = new PEMReader(fileReader);
//        try {
//            return (KeyPair) r.readObject();
//        } catch (IOException ex) {
//            throw new IOException("The private key could not be decrypted", ex);
//        } finally {
//
//            fileReader.close();
//        }
//    }
//
//}