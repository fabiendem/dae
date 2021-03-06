package com.fablap.defib.models.defibrillator.json;

import android.util.Log;

import com.fablap.defib.models.EnvironmentEnum;
import com.fablap.defib.models.defibrillator.DefibrillatorModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collection;

/**
 * Created by Fabien on 20/02/15.
 */
public class DefibrillatorJsonConvertor {

    public static DefibrillatorModel ConvertJsonStingToDefibrillator(String json) {
        Log.d("Utils", "jsonToDefibrillator");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DefibrillatorModel.class,
                new DefibrillatorModelDeserializer());
        Gson gson = gsonBuilder.create();
        // Parse JSON to Java
        return gson.fromJson(json, DefibrillatorModel.class);
    }

    public static Collection<DefibrillatorModel> ConvertJsonStringToDefibrillatorsCollection(String jsonArrayString) {
        Log.d("Utils", "ConvertJsonStringToDefibrillatorsCollection");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Collection.class,
                new DefibrillatorModelsCollectionDeserializer());
        Gson gson = gsonBuilder.create();

        // Parse JSON to Java
        return gson.fromJson(jsonArrayString, Collection.class);
    }

    protected static DefibrillatorModel ConvertJsonElementToDefibrillatorModel(JsonElement jsonElement) {
        DefibrillatorModel.Builder defibrillatorModelBuilder = new DefibrillatorModel.Builder();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        defibrillatorModelBuilder.setId(jsonObject.get("OBJECTID").getAsInt());
        defibrillatorModelBuilder.setLocationDescription(jsonObject.get("EMPLACEMEN").getAsString());
        defibrillatorModelBuilder.setCity(jsonObject.get("COMMUNE_DA").getAsString());

        JsonElement interieurJsonElement = jsonObject.get("INTERIEUR_");
        String interieur = null;
        if(! interieurJsonElement.isJsonNull()) {
            interieur = interieurJsonElement.getAsString();
        }
        if (interieur != null && interieur.equals("X")) {
            defibrillatorModelBuilder.setEnvironment(EnvironmentEnum.INDOORS);
        }

        JsonElement exterieurJsonElement = jsonObject.get("EXTERIEUR_");
        String exterieur = null;
        if(! exterieurJsonElement.isJsonNull()) {
            exterieur = exterieurJsonElement.getAsString();
        }
        if (exterieur != null &&
                (exterieur.equals("X") || exterieur.equals("24h/24")) ) {
            defibrillatorModelBuilder.setEnvironment(EnvironmentEnum.OUTDOORS);
        }

        JsonObject locationJson = jsonObject.get("geometry").getAsJsonObject();
        defibrillatorModelBuilder.setLongitude(locationJson.get("x").getAsDouble());
        defibrillatorModelBuilder.setLatitude(locationJson.get("y").getAsDouble());

        return defibrillatorModelBuilder.build();
    }
}
