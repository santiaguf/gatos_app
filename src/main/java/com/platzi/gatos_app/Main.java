/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app;

import java.io.IOException;
import javax.swing.JOptionPane;

import com.platzi.gatos_app.model.Gatos;
import com.platzi.gatos_app.service.GatosService;

/**
 *
 * @author santiaguf
 */
public class Main {

    public static void main(String[] args) throws IOException{
        int opcion_menu = -1;
        String[] botones = {" 1. ver gatos", "2. ver favoritos", "3. salir"};

        do{

            //menu principal
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos java", "Menu principal", JOptionPane.INFORMATION_MESSAGE,
                    null, botones,botones[0]);

            //validamos que opcion selecciona el usuario
            for(int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    opcion_menu = i;
                }
            }

            switch(opcion_menu){
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavorito(gato.getApikey());
                default:
                    break;
            }
        }while(opcion_menu != 2);
    }
}
