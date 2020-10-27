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

    public static void main(String[] args) {
        String iV=initVector(15);
        KeyManager keyManager= new KeyManager("k1","k2","aesKEy");
        CommunicationNodeB nodeB= new CommunicationNodeB(keyManager,iV);
        CommunicationNodeA nodeA= new CommunicationNodeA("OFB",keyManager,nodeB,iV);
         /* nodeA.askForCommunicaionKey();
          nodeA.communicateOperationMode();
          nodeA.sendMessageToNodeB("un mesaj oarecare");
          */
        //nodeA.encryptOFB("qwertyuiopasdfghjkl");
        //nodeB.decryptOFB(nodeA.encryptOFB("12345678901234"));
        nodeA.encryptOFB("qwertyuiopasdfghjkllll");
    }
}
