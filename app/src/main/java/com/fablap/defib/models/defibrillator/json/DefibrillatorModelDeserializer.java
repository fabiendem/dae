package com.fablap.defib.models.defibrillator.json;

import com.fablap.defib.models.defibrillator.DefibrillatorModel;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Fabien on 20/02/15.
 */
public class DefibrillatorModelDeserializer implements JsonDeserializer<DefibrillatorModel> {
    @Override
    public DefibrillatorModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return DefibrillatorJsonConvertor.ConvertJsonElementToDefibrillatorModel(json);
    }
}
