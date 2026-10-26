package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocketHandler implements Runnable{
    private final Socket s;
    Map<String,Integer> disponibilita = new HashMap<>();
    ArrayList<String> user= new ArrayList<>();


    public SocketHandler(Socket so){
        s=so;

        disponibilita.put("Gold", 10);
        disponibilita.put("Pit", 30);
        disponibilita.put("Parterre", 60);
    }

    @Override
    public void run(){
        String nome;
        boolean var=true;
        String scelta;
        try(
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            Socket autoCloseablSocket = s;
        ){
            out.println("Welcome");

            do{
                out.println("Login");
                nome=in.readLine();
                

                if(user.contains(nome)){
                    out.print("USERINUSE");
                }else{
                    out.print("OK");
                    var=false;
                }

            }while(var);
            user.add(nome);

            while(true){
                scelta=in.readLine();

                switch (scelta) {
                    case "N":
                        String str="";
                        for(String s : disponibilita.keySet()){
                            str += s +": "+disponibilita.get(s)+"\n";
                        }
                        out.println(str);
                        str="";
                        break;

                    case "BUY":
                        String inp=in.readLine();
                        String parti[]= inp.split(" ",2);

                        if(parti[0].toLowerCase()=="gold" && parti[0].toLowerCase()=="pit" && parti[0].toLowerCase()=="parterre"){
                            if(parti.length== 1)
                                parti[1]="1";

                            try {
                                if(disponibilita.get(parti[0])>=Integer.parseInt(parti[1])){
                                    disponibilita.put(parti[0],(disponibilita.get(parti[0])-Integer.parseInt(parti[1])));
                                    out.println("Hai acquistato la tipologia: "+parti[0]+" ed hai preso: "+parti[1]+" biglietti");

                                }else{
                                    out.println("KO --> Non sono rimasti abbastanza biglietti");
                                }
                            } catch (Exception e) {
                                out.println("ERR SYNTAX --> Tentativo di acquisto di quantità non numerica");
                            }
                            
                        }else{
                            out.println("ERR UNKNOWNTYPE --> Tipo non ammesso");
                        }

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
