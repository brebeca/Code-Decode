package com.company;

public class KeyManager {
    private String ECB_key;
    private String OFB_key;
    private String ASE_key;

    KeyManager( String ecb_key, String ofb_key, String aes_key){
        ECB_key=ecb_key;
        OFB_key=ofb_key;
        ASE_key= aes_key;
    }

    /**
     * nodul KM va cripta cheia cerut˘a (K1 sau K2 in funct¸ie de modul de
     * operare ales) ca un singur bloc, utilizˆand criptosistemul AES cu cheia
     * K3 ¸si va trimite mesajul astfel obt¸inut ca r˘aspuns pentru nodurile A ¸si B
     * @param mode
     * @return
     */
    public String getKey(String mode){
        if(mode.contains("ECB"))
            return AES.encrypt(ECB_key,ASE_key);
        if(mode.contains("OFB"))
            return AES.encrypt(OFB_key,ASE_key);
        return null;
    }

    public String getASE_key() {
        return ASE_key;
    }
}
