package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class SocketHandler implements Runnable{
    private final Socket s;
    private Biglietti b;
    private static ArrayList<String> user= new ArrayList<>();


    public SocketHandler(Socket so,Biglietti b){
        s=so;
        this.b=b;

    }

    public synchronized boolean login(BufferedReader in, PrintWriter out){
        try{
            String nome;
            boolean var=true;
            while (var) {
                out.println("Inserisci il tuo nome");
                nome=in.readLine();
                if (user.contains(nome)){
                    out.println("ERR DUPLICATENAME --> Nome già in uso");
                }else{
                    user.add(nome);
                    var=false;
                    return true;
                }
            }
        }catch(Exception e){
            System.out.println("Errore");
        }
        return false;
    }

    @Override
    public void run(){
        String scelta;
        String quantita;
        try(
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Socket autoCloseablSocket = s;
        ){
            boolean log=login(in,out);
            if (!log) {
                return; 
            }
            out.println("Welcome");

            while(true){
                scelta=in.readLine();

                switch (scelta) {
                    case "N":
                        String str="";
                            str =  "gold"+": "+b.getGold()+"\n"+"panterre"+": "+b.getPanterre()+"\n"+"pit"+": "+b.getPit()+"\n";
                        
                        out.println(str);
                        str="";
                        break;

                    case "BUY":
                        String inp=in.readLine();
                        String parti[]= inp.split(" ",2);

                            if(parti[0].toLowerCase().equals("gold") || parti[0].toLowerCase().equals("pit") || parti[0].toLowerCase().equals("panterre")){
                                if (parti.length == 1) {
                                    quantita = "1";
                                } else {
                                    quantita = parti[1];
                                }

                                try {
                                    if(b.controllaBiglietti(parti[0], Integer.parseInt(quantita))){
                                        b.acquistaBiglietto(parti[0], Integer.parseInt(quantita));
                                        out.println("Hai acquistato la tipologia: "+parti[0]+" ed hai preso: "+quantita+" biglietti");

                                    }else{
                                        out.println("KO --> Non sono rimasti abbastanza biglietti");
                                    }
                                } catch (Exception e) {
                                    out.println("ERR SYNTAX --> Tentativo di acquisto di quantità non numerica");
                                }
                                
                            }else{
                                out.println("ERR UNKNOWNTYPE --> Tipo non ammesso");
                            }
                        
                        break;

                    case "QUIT":
                        return;
                
                    default:
                        out.println("ERR UNKNOWNCMD --> Comando sconosciuto ");
                        break;
                }
            }
            

        }catch(Exception e){
            System.out.println("Errore");
        }
    }
}
