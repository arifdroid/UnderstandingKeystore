package com.example.understandingkeystore;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class Decryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private KeyStore keyStore;

    Decryptor()throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException {

        initKeyStore();

    }

    private void initKeyStore ()throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {

        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);
    }



        //before we decrypt we need the alias, the encrypted word we did, andd... IV??

    String decryptData(final String alias, final byte[] encryptedData,final byte[] encryptionIV)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {


        //cipher is used both encrypt and decrypt

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIV);

        cipher.init(Cipher.DECRYPT_MODE,getSecretKey(alias),spec);

        return new String(cipher.doFinal(encryptedData),"UTF-8");

    }

    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException {
        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();

        //https://medium.com/@josiassena/using-the-android-keystore-system-to-store-sensitive-information-3a56175a454b
    }

}
