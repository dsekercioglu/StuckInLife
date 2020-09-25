package stuckinlife.gamecore.gameutils;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CharFactory {

    public static void createAndAddWithRandomLocation(World world, Class clazz, int teamCode, int number) {
        world.addCharacters(createWithRandomLocation(world, clazz, teamCode, number));
    }

    public static List<Char> createWithRandomLocation(World world, Class clazz, int teamCode, int number) {
        List<Char> characters = new ArrayList<>();
        try {
            for (int i = 0; i < number; i++) {
                double x = world.getMIN_X() + Math.random() * (world.getMAX_X() - world.getMIN_X());
                double y = world.getMIN_Y() + Math.random() * (world.getMAX_Y() - world.getMIN_Y());
                int orientation = (int) (Math.random() * 4);
                characters.add((Char) clazz.getDeclaredConstructor(World.class, double.class, double.class, int.class, int.class).newInstance(world, x, y, orientation, teamCode));
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return characters;
    }

    public static void createAndAdd(World world, Class clazz, int teamCode, double x, double y, int number) {
        world.addCharacters(create(world, clazz, teamCode, x, y, number));
    }

    public static List<Char> create(World world, Class clazz, int teamCode, double x, double y, int number) {
        List<Char> characters = new ArrayList<>();
        try {
            for (int i = 0; i < number; i++) {
                int orientation = (int) (Math.random() * 4);
                characters.add((Char) clazz.getDeclaredConstructor(World.class, double.class, double.class, int.class, int.class).newInstance(world, x, y, orientation, teamCode));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return characters;
    }
}
