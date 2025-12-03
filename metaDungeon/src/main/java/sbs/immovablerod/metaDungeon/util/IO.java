package sbs.immovablerod.metaDungeon.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IO {
    public static HashMap<String,Object> LoadMapFromJson(String file_name) {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File("plugins" + File.separator + "metaDungeon" + File.separator + file_name);
        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        HashMap<String,Object> o = null;
        try {
            o = mapper.readValue(from, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(o);

        return o;
    }



}
