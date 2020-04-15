
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PC {

    static ServerSocket server;
    static int clientID = 0;
    static SecretKey key;

    public static void main(String ard[]) {

        try {
            File f = new File("key.txt");
            System.out.println("création d'un fichier et de la clé");
            KeyGenerator generator = KeyGenerator.getInstance("AES"); 
            generator.init(128);
            SecretKey key = generator.generateKey(); //On génère la clé
            String k2w;
            byte[] encoded = key.getEncoded();
            k2w = Base64.encode(encoded);
            FileWriter fw = new FileWriter(f.getAbsoluteFile()); //On met la clé sur le fichier que l'on a créé
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(k2w);
            bw.close();
            fw.close();
            System.out.println("écriture réalisée de la clé : " + k2w); //On affiche la clé que l'on a écrite
            server = new ServerSocket(4444, 5);
            go();
        } catch (Exception e) {
        }
    }

    public static void go() {

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true)//
                    {
                        try {
                            Socket client = server.accept();
                            Thread tAccueil = new Thread(new ChatServer(client, clientID));
                            tAccueil.start();
                            clientID++;
                        } catch (Exception e) {
                        }
                    }
                }
            });
            t.start();

        } catch (Exception i) {
            System.out.println("Impossible d'ecouter sur le port 4444: serait-il occupe?");
            i.printStackTrace();
        }
    }
}
