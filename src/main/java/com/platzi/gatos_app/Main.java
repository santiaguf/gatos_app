/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app;

import java.io.IOException;
import javax.swing.JOptionPane;

import com.platzi.gatos_app.model.Cats;
import com.platzi.gatos_app.service.CatService;

/**
 *
 * @author santiaguf
 */
public class Main {

    public static void main(String[] args) throws IOException{
        int menuOption = -1;
        String[] buttoms = {" 1. ver gatos", "2. ver favoritos", "3. salir"};

        do{
            String option = (String) JOptionPane.showInputDialog(null, "Gatitos java", "Menu principal", JOptionPane.INFORMATION_MESSAGE, null, buttoms,buttoms[0]);

            for(int i=0;i<buttoms.length;i++){
                if(option.equals(buttoms[i])){
                    menuOption = i;
                }
            }

            switch(menuOption){
                case 0:
                    CatService.seeRandomCats();
                    break;
                case 1:
                    Cats cat = new Cats();
                    CatService.seeFavoriteCats(cat.getApikey());
                default:
                    break;
            }
        }while(menuOption != 2);
    }
}
