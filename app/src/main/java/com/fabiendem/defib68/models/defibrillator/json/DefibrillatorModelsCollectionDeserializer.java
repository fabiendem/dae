package com.fabiendem.defib68.models.defibrillator.json;

import com.fabiendem.defib68.models.defibrillator.DefibrillatorModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Fabien on 20/02/15.
 */
public class DefibrillatorModelsCollectionDeserializer implements JsonDeserializer<Collection<DefibrillatorModel>> {

    @Override
    public Collection<DefibrillatorModel> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<DefibrillatorModel> newArray = new ArrayList<DefibrillatorModel>();

        JsonArray array= json.getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();

        while(iterator.hasNext()){
            JsonElement json2 = (JsonElement)iterator.next();
            DefibrillatorModel object = DefibrillatorJsonConvertor.ConvertJsonElementToDefibrillatorModel(json2);
            newArray.add(object);
        }

        return newArray;
    }
}
