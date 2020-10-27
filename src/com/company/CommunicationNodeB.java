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

  

    public void reciveMessage(String mesaj){
        System.out.println("Nodul B a primit mesajul : "+mesaj+ " si a decriptat : "+decryptECB(mesaj));
    }


}
