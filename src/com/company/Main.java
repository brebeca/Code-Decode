package com.company;

import java.util.Random;

public class Main {
    public static String initVector(int blockSize){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(blockSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
     public static void exempulCuOFB(String mesaj){
         String iV=initVector(15);
         KeyManager keyManager= new KeyManager("k1","k2","aesKEy");
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
         /**
          *nodul A cripteaz˘a cont¸inutul unui fi¸sier text utilizˆand AES, cheia primit˘a
          * de la KM ¸si modul de operare ales. A va transmite nodului B blocurile
          * de criptotext obt¸inute iar nodul B va decripta blocurile primite ¸si va afi¸sa rezultatul obt¸inut.
          */
         nodeA.sendMessageToNodeB(mesaj);
     }

    public static void exempulCuECB(String mesaj){
        String iV=initVector(15);
        KeyManager keyManager= new KeyManager("k1","k2","aesKEy");
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
        /**
         *nodul A cripteaz˘a cont¸inutul unui fi¸sier text utilizˆand AES, cheia primit˘a
         * de la KM ¸si modul de operare ales. A va transmite nodului B blocurile
         * de criptotext obt¸inute iar nodul B va decripta blocurile primite ¸si va afi¸sa rezultatul obt¸inut.
         */
        nodeA.sendMessageToNodeB(mesaj);
    }

    public static void main(String[] args) {
       exempulCuECB("um mesaj de pentru care folosim ECB");
       System.out.println();
       exempulCuOFB("un mesaj pentru care folosim OFB");
    }
}
