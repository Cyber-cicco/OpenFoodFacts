package fr.diginamic.utils;

import java.util.Scanner;

public class ScannerManager {

    private static Scanner scanner;

    public static Scanner getInstance(){
        if(scanner != null) return scanner;
        scanner = new Scanner(System.in);
        return scanner;
    }
}
