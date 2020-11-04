package com.company;

import java.util.Random;

public class Main {
    public static String randomString(int blockSize){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(blockSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

     public static void exempulCuOFB(){
         String iV= randomString(16);
         String K1=randomString(16);
         String K2=randomString(16);
         String K3=randomString(16);
         KeyManager keyManager= new KeyManager(K1,K2,K3);
         CommunicationNodeB nodeB= new CommunicationNodeB(keyManager,iV);
         CommunicationNodeA nodeA= new CommunicationNodeA("OFB",keyManager,nodeB,iV);

         /**
          *  nodul A transmite un mesaj nodului KM prin
          * care cere cheia corespunzatoare
          */
         nodeA.askForCommunicaionKey();
         /**
          * nodul A trimite un
          * mesaj c˘atre nodul B ˆın care comunic˘a modul de operare
          */
         nodeA.communicateOperationMode();

     }

    public static void exempulCuECB(){
        String iV= randomString(16);
        String K1=randomString(16);
        String K2=randomString(16);
        String K3=randomString(16);
        KeyManager keyManager= new KeyManager(K1,K2,K3);
        CommunicationNodeB nodeB= new CommunicationNodeB(keyManager,iV);
        CommunicationNodeA nodeA= new CommunicationNodeA("ECB",keyManager,nodeB,iV);

        /**
         *  nodul A transmite un mesaj nodului KM prin
         * care cere cheia corespunzatoare
         */
        nodeA.askForCommunicaionKey();
        /**
         * nodul A trimite un
         * mesaj c˘atre nodul B ˆın care comunic˘a modul de operare
         */
        nodeA.communicateOperationMode();

    }

    public static void main(String[] args) {
       exempulCuECB();
       System.out.println();
       exempulCuOFB();
    }
}
