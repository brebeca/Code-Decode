package com.company;

import java.util.Arrays;

public class CommunicationNodeB {
    private String communicationMode;
    private final String AesKey;
    private String communicationKey;
    private final KeyManager keyManager;
    private CommunicationNodeA communicationNodeA;
    private final int blockSizeECB;
    private final int blockSizeOFB;
    private final String iV;


    CommunicationNodeB( KeyManager keyManager, String iV){
        this.keyManager=keyManager;
        this.AesKey=keyManager.getASE_key();
        this.blockSizeECB=24;
        this.blockSizeOFB=15;
        this.iV=iV;
    }

    /**
     * nodul transmite un mesaj nodului KM prin
     * care cere cheia corespunzatoare si o decripteaza
     */
    public void askForCommunicaionKey(){
        communicationKey=AES.decrypt(keyManager.getKey(communicationMode), AesKey);
        System.out.println("nodul B a decriptat "+ communicationKey);
        communicationNodeA.communicationStartNotify();
    }

    public void pairWithNodeForCommunication(CommunicationNodeA communicationNodeA){
        this.communicationNodeA=communicationNodeA;
    }

    /**
     *• Nodul B, la primirea mesajului de la nodul A, cere nodului KM cheia
     * @param communicationMode
     */
    public void setCommunicationMode(String communicationMode){ this.communicationMode=communicationMode; askForCommunicaionKey();}

    /**
     * se parcurge mesajul primit si se imparte in blocuri de lungime fixa
     * se decripteaza fiecare bloc si se adauga la mesajul final
     * @param mesaj
     */
    public String decryptECB(String mesaj) {
        try {

            StringBuilder decrypted = new StringBuilder();
            int i = 0;
            StringBuilder block;

            while (mesaj.length() >= i + blockSizeECB) {
                block = new StringBuilder(mesaj.substring(i, i + blockSizeECB));
                decrypted.append(AES.decrypt(block.toString(), AesKey));
                i = i + blockSizeECB;
            }
            return decrypted.toString();

        } catch (Exception e) {
            return "Eroare la decriptare "+e.toString();
        }
    }

    /**
     * se itereaza prin mesaj si se selecteaza blocuri de lungimi egale
     * la fiecare pas se cripteaza vectorul de initializare encryptedIV din nou
     * se itereaza pe fiecare bloc si se face XOR caracter cu caracter intre bloc si vectorul de initializare encryptedIV
     * si se memoreaza rezultatul caracter cu caracter in array-ul decrypt
     * @param mesaj mesajul ce va fi decriptat
     * @return mesajul decriptat
     */
    public String decryptOFB(char[] mesaj){
        String encryptedIV= iV;
        char[] decrypt= new char[mesaj.length];

        for(int i=0;i< mesaj.length; i+=blockSizeOFB){
            encryptedIV= AES.encrypt(encryptedIV,AesKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k=0;
            for (int j = i; j < i+blockSizeOFB && j< mesaj.length ; j++) {
                assert encryptedIV != null;
                decrypt[j] = (char) (mesaj[j] ^ encryptedIV.charAt(k));
                k++;
            }
        }
        return String.valueOf(decrypt);
    }

    /**
     * se apeleaza functia de decriptare specifaca modului
     * se afiseaza rezultatul
     * @param mesaj mesajul criptat
     */
    public void reciveMessage(String mesaj){
        String decrypted="";
        if(communicationMode.equals("ECB")) {
            decrypted=decryptECB(mesaj);
          }
        if(communicationMode.equals("OFB")) {
            decrypted=decryptOFB(convertToArrayOfChar(mesaj));
           }
        System.out.println("Nodul B a primit mesajul : " +"\u001B[34m"+mesaj+"\u001B[0m");
        System.out.println("  A decriptat : "+"\u001B[31m"+decrypted +"\u001B[0m");
        System.out.println("  Folosit  modul :"+"\u001B[33m"+communicationMode+"\u001B[0m");
    }

    public char[] convertToArrayOfChar(String mesaj){
        char[] converted= new char[mesaj.length()];
        for(int i=0; i<mesaj.length();i++){
            converted[i]=mesaj.charAt(i);
        }
        return converted;
    }


}


