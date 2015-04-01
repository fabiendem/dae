package com.fablap.defib.models.defibrillator.json;

import com.fablap.defib.models.defibrillator.DefibrillatorModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Fabien on 20/02/15.
 */
public class DefibrillatorModelsCollectionDeserializer implements JsonDeserializer<Collection<DefibrillatorModel>> {

    @Override
    public Collection<DefibrillatorModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<DefibrillatorModel> newArray = new ArrayList<>();

        JsonArray array= json.getAsJsonArray();

        for (JsonElement json2 : array) {
            DefibrillatorModel object = DefibrillatorJsonConvertor.ConvertJsonElementToDefibrillatorModel(json2);
            newArray.add(object);
        }

        return newArray;
    }
}
