package sbs.immovablerod.metaDungeon.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Experimental;
import sbs.immovablerod.metaDungeon.MetaDungeon;

import java.io.File;
import java.io.IOException;

public class JsonLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public final File dataPath;
    public final File mapDataPath;
    public final File compliedJsonDataPath;

    public final JsonNode itemsV2;
    public final JsonNode entityTemplates;
    public final JsonNode eventTemplates;
    public JsonNode gameplay;
    public final JsonNode messages;
    public JsonNode mapTemplates;

    public JsonLoader(File dataPath) throws IOException {

        this.dataPath = dataPath;
        this.dataPath.mkdirs();
        //plugin.getLogger().info("Data path loaded at: " + this.dataPath);

        this.mapDataPath = new File(this.dataPath + File.separator + "mapData");
        this.mapDataPath.mkdirs();
        //plugin.getLogger().info("Map data path loaded at: " + this.mapDataPath);

        this.compliedJsonDataPath = new File(this.dataPath + File.separator + "compiledJson");
        this.compliedJsonDataPath.mkdirs();
        //plugin.getLogger().info("Compiled json data path loaded at: " + this.compliedJsonDataPath);



        this.itemsV2 = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "items.json"));
        this.entityTemplates = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "monsters.json"));
        this.eventTemplates = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "events.json"));
        this.gameplay = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "gameplay.json"));
        this.messages = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "messages.json"));
        this.mapTemplates = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "maps.json"));
    }

    public void reload() throws IOException {
        this.gameplay = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "gameplay.json"));
        this.mapTemplates = this.objectMapper.readTree(new File(this.compliedJsonDataPath + File.separator + "maps.json"));
    }
}
