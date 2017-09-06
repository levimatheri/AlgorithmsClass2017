/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.utilities;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.net.util.Base64;
//import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author cjedwards1
 */
public class SendCookie
{
    private final String ENCRYPTION_ALGORITHM = "AES";
    private final int ENCRYPTION_KEY_SIZE = 256;
    private final int ENCRYPTION_BLOCK_SIZE = 16;
    
    private byte[] encrypt(byte[] bytes)
    {
        try
        {
//            System.out.println(Cipher.getMaxAllowedKeyLength("AES")); //To test if host can use 256 bit encryption.
            Base64 t_B64 = new Base64(0, new byte[0], false);
            
            // Setup encryption cipher
            SecretKeySpec t_EncryptionSecretKeySpec = new SecretKeySpec(t_B64.decode("+BEDmIyLr8LT8TB/g66bG0Oe5vbNYplZ2DwVb/lvl3E="), ENCRYPTION_ALGORITHM);
            SecureRandom t_SecureRandom = new SecureRandom();
            byte[] t_InitializationVector = new byte[ENCRYPTION_BLOCK_SIZE];
            t_SecureRandom.nextBytes(t_InitializationVector);
            IvParameterSpec t_IvParameterSpec = new IvParameterSpec(t_InitializationVector);
            
            Cipher t_EncryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            t_EncryptionCipher.init(Cipher.ENCRYPT_MODE, t_EncryptionSecretKeySpec, t_IvParameterSpec);
            
            // encrypt
            byte[] t_EncryptedBytes = t_EncryptionCipher.doFinal(bytes);
            byte[] t_EncryptedBytesIV = new byte[t_InitializationVector.length + t_EncryptedBytes.length];
            
            System.arraycopy(t_EncryptedBytes, 0, t_EncryptedBytesIV, 0, t_EncryptedBytes.length);
            System.arraycopy(t_InitializationVector, 0, t_EncryptedBytesIV, t_EncryptedBytes.length, t_InitializationVector.length);
            
            return t_EncryptedBytesIV;
        }
        catch(InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex)
        {
            Logger.getLogger(SendCookie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public String encode(byte[] bytes)
    {
        byte[] encryptedBytes = encrypt(bytes);
        
        if(encryptedBytes == null)
            return null;
        else
            return Base64.encodeBase64URLSafeString(encryptedBytes);
    }
}
