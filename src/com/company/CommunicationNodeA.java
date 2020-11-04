package com.company;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        this.blockSize=16;
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
        sendMessageToNodeB();
    }

    public void sendMessageToNodeB(){
        try {
            StringBuilder mesaj= new StringBuilder();
            File myObj = new File("src"+File.separator+"com"+File.separator+"company"+File.separator+"de_aici_citeste_nodulA");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                mesaj.append(myReader.nextLine());
            }
            myReader.close();
            if(communicationMode.equals("ECB")) {
                String criptat = encryptECB(mesaj.toString());
                System.out.println("Nodul A cripteaza mesajul : "+mesaj +" si trimte mesajul: "+criptat );
                nodeB.reciveMessage(criptat);
            }
            if(communicationMode.equals("OFB")) {
                char[] criptat = encryptOFB(mesaj.toString());
                System.out.println("Nodul A cripteaza mesajul : "+mesaj +" si trimte mesajul: "+ new String(criptat));
                nodeB.reciveMessage(new String(criptat));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
                crypted.append(AES.encrypt(block.toString(),communicationKey ));
                i=i+blockSize;
            }

            // verificam daca mai exista un block neciti in mesaj ; il padam si il criptam
            if(mesaj.length()>i) {
                block = new StringBuilder(mesaj.substring(i));
                while (block.length()!=blockSize)
                    block.append(" ");
                crypted.append(AES.encrypt(block.toString(), communicationKey));

            }
            return crypted.toString();

        }catch (Exception e)
        {
            System.out.println("Eroare "+e.toString());
        }
        return null;
    }

    /**
     * se parcurge mesajul si se imparte in blocuri de marimi egale
     * la fiacare bloc de reinitializeaza vectorul initializare
     * pt fiecare bloc se face XOR intre caracterul din mesaj si caracterul din vector
     * si rezultatul se memoreaza in vectorul encrypt
     * @param mesaj
     * @return
     */
    public char[] encryptOFB(String mesaj){
        String encryptedIV= iV;                        //vector de initializare
        char[] encrypt= new char[mesaj.length()];

        for(int i=0;i< mesaj.length(); i+=blockSize) {
            encryptedIV = AES.encrypt(encryptedIV, communicationKey);//se cripteaza vectorul de initializare precedent cu cheia AES
            int k = 0;
            for (int j = i; j < i+ blockSize && j< mesaj.length(); j++) {
                assert encryptedIV != null;
                encrypt[j] = (char) (mesaj.charAt(j) ^
                        encryptedIV.charAt(k));
                k++;
            }
        }

        return encrypt;
    }



}
