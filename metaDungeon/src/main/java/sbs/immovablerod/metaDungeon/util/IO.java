package sbs.immovablerod.metaDungeon.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IO {
    //private final String dataFolder = getDataFolder().getAbsolutePath();
    public static void DumpMapToJson(String file_name, Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(map);
        try {
            objectMapper.writeValue(new File ("plugins" + File.separator + "skillfulhacks" + File.separator + file_name), map);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String MapToJson(Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println(map);
        try {
            return objectMapper.writeValueAsString(map);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static HashMap<String,Object> LoadMapFromJson(String file_name) {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File("plugins" + File.separator + "skillfulhacks" + File.separator + file_name);
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
