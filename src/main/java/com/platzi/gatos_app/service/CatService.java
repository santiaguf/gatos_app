/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app.service;

import com.google.gson.Gson;
import com.platzi.gatos_app.model.Cats;
import com.platzi.gatos_app.model.FavoriteCats;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author santiaguf
 */
public class CatService {

    private static String BASE_URL = "https://api.thecatapi.com/v1/";
    private static String SEARCH_ENDPOINT = BASE_URL+"images/search";
    private static String FAVORITE_ENDPOINT = BASE_URL+"favourites";
    private static String FavoriteMenu = "Opciones: \n"
                            + " 1. ver otra imagen \n"
                            + " 2. Eliminar Favorito \n"
                            + " 3. Volver \n";
    private static String randomCatsMenu = "Opciones: \n"
                    + " 1. ver otra imagen \n"
                    + " 2. Favorito \n"
                    + " 3. Volver \n";

    public static void verGatos() throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SEARCH_ENDPOINT).get().build();
        Response response = client.newCall(request).execute();
        String elJson = response.body().string();

        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1);

        Gson gson = new Gson();
        Cats gatos = gson.fromJson(elJson, Cats.class);

        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);

            if(fondoGato.getIconWidth() > 800){

                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String[] botones = { "ver otra imagen", "favorito", "volver" };
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null, randomCatsMenu, id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones,botones[0]);

            int seleccion = -1;

            for(int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }

            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void favoritoGato(Cats gato) {
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+gato.getId()+"\"\n}");
            Request request = new Request.Builder()
            .url(FAVORITE_ENDPOINT)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", gato.getApikey())
            .build();
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }

    }

    public static void verFavorito(String apikey) throws IOException{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url(FAVORITE_ENDPOINT)
        .get()
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", apikey)
        .build();

        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        if(!response.isSuccessful()) {
            response.body().close();
        }

        Gson gson = new Gson();

        FavoriteCats[] gatosArray = gson.fromJson(elJson,FavoriteCats[].class);
        if(gatosArray.length > 0){
            int min = 1;
            int max  = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            int indice = aleatorio-1;
            FavoriteCats gatofav = gatosArray[indice];


                Image image = null;
                try{
                    URL url = new URL(gatofav.image.getUrl());
                    image = ImageIO.read(url);

                    ImageIcon fondoGato = new ImageIcon(image);

                    if(fondoGato.getIconWidth() > 800){

                        Image fondo = fondoGato.getImage();
                        Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                        fondoGato = new ImageIcon(modificada);
                    }

                    String[] botones = { "ver otra imagen", "eliminar favorito", "volver" };
                    String id_gato = gatofav.getId();
                    String opcion = (String) JOptionPane.showInputDialog(null, FavoriteMenu, id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones,botones[0]);

                    int seleccion = -1;

                    for(int i=0;i<botones.length;i++){
                        if(opcion.equals(botones[i])){
                            seleccion = i;
                        }
                    }

                    switch (seleccion){
                        case 0:
                            verFavorito(apikey);
                            break;
                        case 1:
                            borrarFavorito(gatofav);
                            break;
                        default:
                            break;
                    }

                }catch(IOException e){
                    System.out.println(e);
                }
        }
    }

    public static void borrarFavorito(FavoriteCats gatofav){
        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
            .url(FAVORITE_ENDPOINT+gatofav.getId()+"")
            .delete(null)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", gatofav.getApikey())
            .build();

            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
