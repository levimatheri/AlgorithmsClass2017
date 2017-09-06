/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.avianmigration.utilities;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
public class RecieveCookie
{
    private final String ENCRYPTION_ALGORITHM = "AES";
    private final int ENCRYPTION_KEY_SIZE = 256;
    private final int ENCRYPTION_BLOCK_SIZE = 16;
    
    private byte[] decode(String incommingString)
    {
        Base64 t_B64 = new Base64(0, new byte[0], false);
        incommingString = incommingString.replaceAll("-", "+").replaceAll("_", "/");
        
        int mod = incommingString.length() % 4;
        incommingString += "====".substring(0, mod);
        return t_B64.decode(incommingString);
    }
    
    public byte[] decrypt(String incomminString)
    {
        try
        {
            Base64 t_B64 = new Base64(0, new byte[0], false);
            
            // separate encrypted data from initialization vector
            byte[] t_DecodedBytes = decode(incomminString);
            byte[] t_InitializationVector = new byte[ENCRYPTION_BLOCK_SIZE];
            byte[] t_EncryptedBytes = new byte[t_DecodedBytes.length - t_InitializationVector.length];
            
            System.arraycopy(t_DecodedBytes, 0, t_EncryptedBytes, 0, t_EncryptedBytes.length);
            System.arraycopy(t_DecodedBytes, t_EncryptedBytes.length, t_InitializationVector, 0, t_InitializationVector.length);

            // Setup decryption cipher
            SecretKeySpec t_EncryptionSecretKeySpec = new SecretKeySpec(t_B64.decode("+BEDmIyLr8LT8TB/g66bG0Oe5vbNYplZ2DwVb/lvl3E="), ENCRYPTION_ALGORITHM);
            IvParameterSpec t_IvParameterSpec = new IvParameterSpec(t_InitializationVector);
            
            Cipher t_DecryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            t_DecryptionCipher.init(Cipher.DECRYPT_MODE, t_EncryptionSecretKeySpec, t_IvParameterSpec);

            // decrypt
            return t_DecryptionCipher.doFinal(t_EncryptedBytes);
        }
        catch(InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex)
        {
            Logger.getLogger(RecieveCookie.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
