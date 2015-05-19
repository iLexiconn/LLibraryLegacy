package net.ilexiconn.llibrary.common.json;

import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHelper
{
    public static JsonTabulaModel parseTabulaModel(InputStream stream)
    {
        return JsonFactory.getGson().fromJson(new InputStreamReader(stream), JsonTabulaModel.class);
    }
}
