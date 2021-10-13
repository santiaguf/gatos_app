/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.platzi.gatos_app.service;

import com.google.gson.Gson;
import com.platzi.gatos_app.model.Cats;
import com.platzi.gatos_app.model.FavoriteCat;
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

    public static void markCatAsFavorite(Cats cat) {
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+cat.getId()+"\"\n}");
            Request request = new Request.Builder()
            .url(FAVORITE_ENDPOINT)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", cat.getApikey())
            .build();
            Response response = client.newCall(request).execute();

            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }

    }

    public static void deleteFavorite(FavoriteCat favoriteCat){
        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
            .url(FAVORITE_ENDPOINT+favoriteCat.getId()+"")
            .delete(null)
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", favoriteCat.getApikey())
            .build();

            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()) {
                response.body().close();
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void seeRandomCats() throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SEARCH_ENDPOINT).get().build();
        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();

        jsonData = jsonData.substring(1, jsonData.length());
        jsonData = jsonData.substring(0, jsonData.length()-1);

        Gson gson = new Gson();
        Cats cat = gson.fromJson(jsonData, Cats.class);

        Image image = null;
        try{
            URL url = new URL(cat.getUrl());
            image = ImageIO.read(url);

            ImageIcon catImageIcon = new ImageIcon(image);

            if(catImageIcon.getIconWidth() > 800){

                Image background = catImageIcon.getImage();
                Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                catImageIcon = new ImageIcon(modified);
            }

            String[] buttoms = { "ver otra imagen", "favorito", "volver" };
            String catId = cat.getId();
            String option = (String) JOptionPane.showInputDialog(null, randomCatsMenu, catId, JOptionPane.INFORMATION_MESSAGE, catImageIcon, buttoms,buttoms[0]);

            int selection = -1;

            for(int i=0;i<buttoms.length;i++){
                if(option.equals(buttoms[i])){
                    selection = i;
                }
            }

            switch (selection){
                case 0:
                    seeRandomCats();
                    break;
                case 1:
                    markCatAsFavorite(cat);
                    break;
                default:
                    break;
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }



    public static void seeFavoriteCats(String apikey) throws IOException{

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url(FAVORITE_ENDPOINT)
        .get()
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", apikey)
        .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        if(!response.isSuccessful()) {
            response.body().close();
        }

        Gson gson = new Gson();

        FavoriteCat[] catsArray = gson.fromJson(jsonData,FavoriteCat[].class);
        if(catsArray.length > 0){
            int min = 1;
            int max  = catsArray.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            int indice = aleatorio-1;
            FavoriteCat favoriteCat = catsArray[indice];


                Image image = null;
                try{
                    URL url = new URL(favoriteCat.image.getUrl());
                    image = ImageIO.read(url);

                    ImageIcon catImageIcon = new ImageIcon(image);

                    if(catImageIcon.getIconWidth() > 800){

                        Image background = catImageIcon.getImage();
                        Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                        catImageIcon = new ImageIcon(modified);
                    }

                    String[] buttoms = { "ver otra imagen", "eliminar favorito", "volver" };
                    String catId = favoriteCat.getId();
                    String option = (String) JOptionPane.showInputDialog(null, FavoriteMenu, catId, JOptionPane.INFORMATION_MESSAGE, catImageIcon, buttoms,buttoms[0]);

                    int selection = -1;

                    for(int i=0;i<buttoms.length;i++){
                        if(option.equals(buttoms[i])){
                            selection = i;
                        }
                    }

                    switch (selection){
                        case 0:
                            seeFavoriteCats(apikey);
                            break;
                        case 1:
                            deleteFavorite(favoriteCat);
                            break;
                        default:
                            break;
                    }

                }catch(IOException e){
                    System.out.println(e);
                }
        }
    }


}
