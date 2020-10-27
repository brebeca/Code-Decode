package com.company;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class CommunicationNodeA {
    private final String communicationMode;
    private final String AesKey;
    private String communicationKey;
    private final KeyManager keyManager;
    private final CommunicationNodeB nodeB;
    private final int blockSize;
    private final String iV;

    CommunicationNodeA(String communicationMode, KeyManager keyManager, CommunicationNodeB nodeB, String iV){
        this.communicationMode=communicationMode;
        this.keyManager=keyManager;
        this.AesKey=keyManager.getASE_key();
        this.nodeB = nodeB;
        this.blockSize=15;
        this.iV=iV;
    }

    /**
     * nodul transmite un mesaj nodului KM prin
     * care cere cheia corespunzatoare si o decripteaza
     */
    public void askForCommunicaionKey(){
        communicationKey=AES.decrypt(keyManager.getKey(communicationMode), AesKey);
        System.out.println("Nodul A a decriptat keya "+ communicationKey);
    }

    /**
     * nodul A comunica nodului B modul de operare
     */
    public void communicateOperationMode(){
        nodeB.pairWithNodeForCommunication(this);
        nodeB.setCommunicationMode(communicationMode);
    }

    public void communicationStartNotify(){
        System.out.println("Nodul B a notificat nodul A ca comunicarea poate incape");
    }

    public void sendMessageToNodeB(String mesaj){
        String criptat=encryptECB(mesaj);
        System.out.println("Nodul A cripteaza mesajul : "+mesaj +" si trimte mesajul: "+criptat );
        nodeB.reciveMessage(criptat);
    }


    /**
     * se imparte in blocuri de lungime egala
     * fiecare bloc este criptat apoi adaugat la mesajul returnat
     * se verifica daca a mai ramas o bucata de mesaj necitita( pt ca avea lungimea < blockSize) care se padeaza si se cripteaza
     * @param mesaj mesajul ce va fi criptat
     * @return mesajul criptat
     */
    public String encryptECB(String mesaj ){
        try {
            StringBuilder block;
            StringBuilder crypted= new StringBuilder();
            int i=0;

            //impartim in blocuri de lungime fixa si criptam
            while(mesaj.length()>=i+blockSize){
                block = new StringBuilder(mesaj.substring(i, i + blockSize));
                crypted.append(AES.encrypt(block.toString(), AesKey));
                i=i+blockSize;
            }

            // verificam daca mai exista un block neciti in mesaj ; il padam si il criptam
            if(mesaj.length()>i) {
                block = new StringBuilder(mesaj.substring(i));
                while (block.length()!=blockSize)
                    block.append(" ");
                crypted.append(AES.encrypt(block.toString(), AesKey));

            }
            return crypted.toString();

        }catch (Exception e)
        {
            System.out.println("Eroare "+e.toString());
        }
        return null;
    }

    public String encryptOFB(String mesaj){
        String encryptedIV= iV;                        //vector de initializare
        char[] encrypt= new char[mesaj.length()];

        for(int i=0;i< mesaj.length(); i+=blockSize) {
            encryptedIV = AES.encrypt(encryptedIV, AesKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k = 0;
            for (int j = i; j < i+ blockSize && j< mesaj.length(); j++) {
                assert encryptedIV != null;
                encrypt[j] = (char) (mesaj.charAt(j) ^
                        encryptedIV.charAt(k));
                k++;
            }
        }

        System.out.println(encrypt);

        encryptedIV= iV;
        char[] decrypt= new char[mesaj.length()];

        for(int i=0;i< mesaj.length(); i+=blockSize){
            encryptedIV= AES.encrypt(encryptedIV,AesKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k=0;
            for (int j = i; j < i+blockSize && j< mesaj.length() ; j++) {
                assert encryptedIV != null;
                decrypt[j] = (char) (encrypt[j] ^ encryptedIV.charAt(k));
                k++;
            }
        }
        System.out.println(decrypt);


        return null;
    }

    /*
    public String encryptOFB(String mesaj){
        String encryptedIV= iV;                        //vector de initializare
        char[] encrypt= new char[mesaj.length()];

        for(int i=0;i< mesaj.length(); i+=blockSize){
            encryptedIV= AES.encrypt(encryptedIV,AesKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k=0;
            for (int j = i; j < mesaj.length(); j++) {
                assert encryptedIV != null;
                encrypt[j] = (char) (mesaj.charAt(j) ^ encryptedIV.charAt(k));
                k++;
            }
        }
        System.out.println("Encrypted : "+ Arrays.toString(encrypt));

        encryptedIV= iV;
        char[] decrypt= new char[mesaj.length()];
        for(int i=0;i< mesaj.length(); i+=blockSize){
            encryptedIV= AES.encrypt(encryptedIV,AesKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k=0;
            for (int j = i; j < mesaj.length(); j++) {
                assert encryptedIV != null;
                decrypt[j] = (char) (encrypt[i] ^ encryptedIV.charAt(k));
                k++;
            }
        }
        System.out.println(decrypt);


        return null;
    }
    */


}
