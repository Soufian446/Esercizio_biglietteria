package com.example;

public class Biglietti {
    private int gold;
    private int panterre;
    private int pit;


    public Biglietti(int gold, int panterre, int pit) {
        this.gold = gold;
        this.panterre = panterre;
        this.pit = pit;
    }

    public synchronized int getGold() {
        return gold;
    }

    public synchronized int getPanterre() {
        return panterre;
    }

    public synchronized int getPit() {
        return pit;
    }

    public synchronized boolean acquistaBiglietto(String tipo, int quantita) {
        switch (tipo.toLowerCase()) {
            case "gold":
                if (quantita <= gold) {
                    gold -= quantita;
                    return true;
                }
                break;
            case "panterre":
                if (quantita <= panterre) {
                    panterre -= quantita;
                    return true;
                }
                break;
            case "pit":
                if (quantita <= pit) {
                    pit -= quantita;
                    return true;
                }
                break;
        }
        return false;
    }

    public synchronized boolean controllaBiglietti(String tipo, int quantita) {
        switch (tipo.toLowerCase()) {
            case "gold":
                if (quantita <= gold) {
                    return true;
                } else {
                    return false;
                }

            case "panterre":
                if (quantita <= panterre) {
                    return true;
                } else {
                    return false;
                }

            case "pit":
                if (quantita <= pit) {
                    return true;
                } else {
                    return false;
                }
        }
        return false; 
    }

}
