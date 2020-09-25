package stuckinlife.gamecore.level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import stuckinlife.gamecore.level.charcreator.CharCreator;
import stuckinlife.gamecore.level.charcreator.FixedCharCreator;
import stuckinlife.gamecore.level.charcreator.LoopingCharCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static stuckinlife.StuckInLife.MAIN_PATH;
import static stuckinlife.StuckInLife.SEPARATOR;

public class LevelSerializer {

    private static final int LEVEL = 2;

    public static void main(String[] args) {
        /*
        FixedCharCreator lcc0 = new FixedCharCreator(1,
                0,
                2000,
                5,
                1,
                1,
                "Mummy");
        List<CharCreator> charCreators = new ArrayList<>();
        charCreators.add(lcc0);
        Level level1 = new Level(charCreators);
        serialize(level1, LEVEL);
        Level l = deserialize(LEVEL);

         */

        /*
        LoopingCharCreator lcc0 = new LoopingCharCreator(1,
                0,
                1000,
                5,
                1,
                0,
                1,
                1,
                "Mummy");
        List<CharCreator> charCreators = new ArrayList<>();
        charCreators.add(lcc0);
        Level level1 = new Level(charCreators);
        serialize(level1, LEVEL);
        Level l = deserialize(LEVEL);
         */

        /*
        List<CharCreator> charCreators = new ArrayList<>();
        for (int y = 0; y < 5; y++) {
            FixedCharCreator lcc = new FixedCharCreator(1,
                    0,
                    1000,
                    5,
                    1,
                    y / 4D,
                    "Mummy");
            charCreators.add(lcc);
        }
        Level level1 = new Level(charCreators);
        serialize(level1, LEVEL);

         */
        /*
        List<CharCreator> charCreators = new ArrayList<>();
        LoopingCharCreator lcc = new LoopingCharCreator(0,
                0,
                10000,
                20,
                0,
                0,
                0,
                1,
                "Golemite");
        LoopingCharCreator lcc1 = new LoopingCharCreator(1,
                0,
                10000,
                20,
                1,
                0,
                1,
                1,
                "Dragon");
        charCreators.add(lcc);
        charCreators.add(lcc1);
        Level level1 = new Level(charCreators);
        serialize(level1, LEVEL);

         */

        //Level l = deserialize(LEVEL);

        List<CharCreator> charCreators = new ArrayList<>();
        LoopingCharCreator lcc = new LoopingCharCreator(1,
                0,
                1000,
                12,
                1,
                0.99,
                1,
                0.01,
                "Reaper");
        charCreators.add(lcc);
        Level level1 = new Level(charCreators);
        serialize(level1, LEVEL);

    }

    public static void serialize(Level level, int levelNum) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setPrettyPrinting();


        RuntimeTypeAdapterFactory<CharCreator> typeFactory = RuntimeTypeAdapterFactory.of(CharCreator.class);
        typeFactory.registerSubtype(FixedCharCreator.class);
        typeFactory.registerSubtype(LoopingCharCreator.class);

        gsonBuilder.registerTypeAdapterFactory(typeFactory);


        Gson gson = gsonBuilder.create();
        String string = gson.toJson(level);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("levels/level" + levelNum + ".json"))) {
            bufferedWriter.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Level deserialize(int levelNum) {
        Level level = null;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setPrettyPrinting();

        RuntimeTypeAdapterFactory<CharCreator> typeFactory = RuntimeTypeAdapterFactory.of(CharCreator.class);
        typeFactory.registerSubtype(FixedCharCreator.class);
        typeFactory.registerSubtype(LoopingCharCreator.class);

        gsonBuilder.registerTypeAdapterFactory(typeFactory);

        Gson gson = gsonBuilder.create();
        try (JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(MAIN_PATH + "levels" + SEPARATOR + "level" + levelNum + ".json")))) {
            level = gson.fromJson(jsonReader, Level.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return level;
    }
}
