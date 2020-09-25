package stuckinlife;

import gifAnimation.Gif;
import processing.core.*;
import processing.event.KeyEvent;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PJOGL;
import processing.opengl.PShader;
import stuckinlife.gamecore.World;
import stuckinlife.gamecore.animation.DamageAnimation;
import stuckinlife.gamecore.animation.AnimationRenderer;
import stuckinlife.gamecore.animation.Particle;
import stuckinlife.gamecore.animation.Trail;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.Player;
import stuckinlife.gamecore.gameutils.Animation;
import stuckinlife.gamecore.gameutils.AnimationLoader;
import stuckinlife.gamecore.gameutils.HealthBarLoader;
import stuckinlife.gamecore.gameutils.ShadowLoader;
import stuckinlife.gamecore.level.LevelSerializer;
import stuckinlife.gamecore.projectile.Projectile;
import utils.Pair;

import java.io.File;
import java.util.*;

public class StuckInLife extends PApplet {


    public static final String SEPARATOR = File.separator;
    public static final String MAIN_PATH = "";
    public static final int NUM_TEXT_FONTS = 20;

    private static final int FRAME_RATE = 1000;

    private int WIDTH;
    private int HEIGHT;

    private final static int WORLD_WIDTH = 3360;
    private final static int WORLD_HEIGHT = 2100;

    private final static int DRAW_LIMIT = 100;

    private final static int X_FOV = 560;
    private final static int Y_FOV = 350;

    private final static int HALF_X_FOV = X_FOV / 2;
    private final static int HALF_Y_FOV = Y_FOV / 2;

    private final static float VIEW_RATIO = Math.max(WORLD_WIDTH / X_FOV, WORLD_HEIGHT / Y_FOV);

    private final static float WORLD_IMG_SIZE = 2.5F;
    private final static float WORLD_PROJECTILE_SIZE = 1.25F;
    private final static float WORLD_PARTICLE_SIZE = 1.5F;

    private float DRAW_RATIO;

    private World world;

    private final Map<String, PImage[]> frameMap = new HashMap<>();
    private final Map<String, PImage[]> filterMap = new HashMap<>();
    private final Map<String, Integer> sizeMap = new HashMap<>();

    private final Map<String, int[][]> shadowLocationMap = new HashMap<>();
    private final Map<String, float[]> healthBarYMap = new HashMap<>();

    private final PFont[] fonts = new PFont[NUM_TEXT_FONTS];

    private final boolean[] KEYS = new boolean[6];

    private long time = 0;

    private Player PLAYER;

    private PShader rgbSplitFilter;

    private PShader outlineFilter;
    private PShader dilateEdgeFilter;

    private PShader bloomBrightnessFilter;
    private PShader bloomBlurHFilter;
    private PShader bloomBlurVFilter;

    private PGraphics processedScreen;
    private PGraphics filteredScreen;
    private PGraphics worldDraw;
    private PGraphics filterDraw;

    private PGraphics brightPass;
    private PGraphics horizontalBlurPass;
    private PGraphics verticalBlurPass;

    private PGraphics outlinePass;
    private PGraphics dilateEdgePass;

    private PGraphics rgbSplitPass;

    private PGraphics finalFilter;

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[]{StuckInLife.class.getName()};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }


    @Override
    public void setup() {
        frameRate(FRAME_RATE);

        PJOGL pgl = (PJOGL) beginPGL();
        pgl.gl.setSwapInterval(1);
        endPGL();

        ((PGraphicsOpenGL) g).textureSampling(2);
        for (int i = 0; i < NUM_TEXT_FONTS; i++) {
            PFont font = createFont("font" + SEPARATOR + "04B_11__.TTF", (10 + i));
            fonts[i] = font;
        }

        loadEnvironment(MAIN_PATH);
        loadChars(MAIN_PATH);
        loadFilters(MAIN_PATH);
        loadProjectiles(MAIN_PATH);
        loadMarkers(MAIN_PATH);
        loadParticles(MAIN_PATH);

        rgbSplitFilter = loadShader("shaders" + SEPARATOR + "rgbSplit.glsl");

        bloomBrightnessFilter = loadShader("shaders" + SEPARATOR + "bloom" + SEPARATOR + "filter.glsl");
        bloomBlurHFilter = loadShader("shaders" + SEPARATOR + "bloom" + SEPARATOR + "blur9h.glsl");
        bloomBlurVFilter = loadShader("shaders" + SEPARATOR + "bloom" + SEPARATOR + "blur9v.glsl");

        outlineFilter = loadShader("shaders" + SEPARATOR + "outline" + SEPARATOR + "edge.glsl");
        dilateEdgeFilter = loadShader("shaders" + SEPARATOR + "outline" + SEPARATOR + "dilate.glsl");

        processedScreen = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) processedScreen).textureSampling(2);

        filteredScreen = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) filteredScreen).textureSampling(2);

        worldDraw = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) worldDraw).textureSampling(2);

        brightPass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) brightPass).textureSampling(2);
        brightPass.shader(bloomBrightnessFilter);

        horizontalBlurPass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) horizontalBlurPass).textureSampling(2);
        horizontalBlurPass.shader(bloomBlurHFilter);

        verticalBlurPass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) verticalBlurPass).textureSampling(2);
        verticalBlurPass.shader(bloomBlurVFilter);

        outlinePass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) outlinePass).textureSampling(2);
        outlinePass.shader(outlineFilter);

        dilateEdgePass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) dilateEdgePass).textureSampling(2);
        dilateEdgePass.shader(dilateEdgeFilter);

        rgbSplitPass = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) rgbSplitPass).textureSampling(2);
        rgbSplitPass.shader(rgbSplitFilter);

        finalFilter = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) finalFilter).textureSampling(2);

        filterDraw = createGraphics(displayWidth, displayHeight, P3D);
        ((PGraphicsOpenGL) filterDraw).textureSampling(2);

        world = new World(WORLD_WIDTH, WORLD_HEIGHT, LevelSerializer.deserialize(2), generateRandomEnvironment());
        PLAYER = new Player(world, WORLD_WIDTH / 2F, WORLD_HEIGHT / 2F, 3, 0, KEYS);
        world.addPlayer(PLAYER);
    }

    public void settings() {
        fullScreen(P3D);
        WIDTH = displayWidth;
        HEIGHT = displayHeight;
        DRAW_RATIO = Math.min(WIDTH * 1F / WORLD_WIDTH, HEIGHT * 1F / WORLD_HEIGHT);
    }


    public void draw() {
        int deltaTime = (int) (System.currentTimeMillis() - time);
        world.update(time == 0 ? 0 : deltaTime);
        time = System.currentTimeMillis();
        clear();

        worldDraw.beginDraw();
        worldDraw.clear();
        worldDraw.fill(0, 90, 0);
        worldDraw.rect(0, 0, displayWidth, displayHeight);

        filterDraw.beginDraw();
        filterDraw.clear();

        drawOnCanvas(worldDraw, filterDraw);

        filterDraw.endDraw();
        worldDraw.endDraw();


        filteredScreen.beginDraw();
        filteredScreen.clear();
        filteredScreen.blendMode(REPLACE);
        filteredScreen.image(worldDraw, 0, 0);
        filteredScreen.blendMode(MULTIPLY);
        filteredScreen.image(filterDraw, 0, 0);
        filteredScreen.endDraw();


        processedScreen.beginDraw();
        processedScreen.clear();
        processedScreen.blendMode(REPLACE);
        processedScreen.image(worldDraw, 0, 0);
        PGraphics bloom = blur(filteredScreen);
        processedScreen.blendMode(ADD);
        processedScreen.image(bloom, 0, 0);
        processedScreen.endDraw();

        //applyMatrix(-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

        image(processedScreen, 0, 0);
    }

    public void drawBackground(PGraphics canvas) {
        PImage img = frameMap.get("Ground")[0];
        canvas.beginShape();
        canvas.textureWrap(REPEAT);
        canvas.texture(img);
        canvas.textureMode(IMAGE);
        canvas.vertex(-(float) PLAYER.getX(), -(float) PLAYER.getY(), 0, 0);
        canvas.vertex(WORLD_WIDTH - (float) PLAYER.getX(), -(float) PLAYER.getY(), 16, 0);
        canvas.vertex(WORLD_WIDTH - (float) PLAYER.getX(), WORLD_HEIGHT - (float) PLAYER.getY(), 16, 16);
        canvas.vertex(-(float) PLAYER.getX(), WORLD_HEIGHT - (float) PLAYER.getY(), 0, 16);
        canvas.endShape();
    }

    public void drawOnCanvas(PGraphics canvas, PGraphics filter) {
        float scale = VIEW_RATIO * DRAW_RATIO;
        float xTranslate = (float) (HALF_X_FOV - PLAYER.getX());
        float yTranslate = (float) (HALF_Y_FOV - PLAYER.getY());
        canvas.scale(scale);
        canvas.translate(xTranslate, yTranslate);
        filter.scale(scale);
        filter.translate(xTranslate, yTranslate);
        drawWorld(canvas, filter, world);
    }

    public PGraphics filter(PGraphics canvas, float threshold) {
        brightPass.beginDraw();
        brightPass.clear();
        brightPass.imageMode(CORNER);
        bloomBrightnessFilter.set("threshold", threshold);
        brightPass.image(canvas, 0, 0);
        brightPass.endDraw();
        return brightPass;
    }

    public PGraphics hBlur(PGraphics canvas, float filter) {
        horizontalBlurPass.beginDraw();
        horizontalBlurPass.clear();
        horizontalBlurPass.imageMode(CORNER);
        bloomBlurHFilter.set("filterSize", filter);
        horizontalBlurPass.image(canvas, 0, 0);
        horizontalBlurPass.endDraw();
        return horizontalBlurPass;
    }

    public PGraphics vBlur(PGraphics canvas, float filter) {
        verticalBlurPass.beginDraw();
        verticalBlurPass.clear();
        verticalBlurPass.imageMode(CORNER);
        bloomBlurVFilter.set("filterSize", filter);
        verticalBlurPass.image(canvas, 0, 0);
        verticalBlurPass.endDraw();
        return verticalBlurPass;
    }

    public PGraphics outline(PGraphics canvas, float threshold) {
        outlinePass.beginDraw();
        outlinePass.clear();
        outlinePass.imageMode(CORNER);
        outlineFilter.set("threshold", threshold);
        outlinePass.image(canvas, 0, 0);
        outlinePass.endDraw();
        return outlinePass;
    }

    public PGraphics dilateEdges(PGraphics canvas) {
        dilateEdgePass.beginDraw();
        dilateEdgePass.clear();
        dilateEdgePass.imageMode(CORNER);
        dilateEdgePass.image(canvas, 0, 0);
        dilateEdgePass.endDraw();
        return dilateEdgePass;
    }


    public PGraphics outlineEdges(PGraphics canvas, float threshold) {
        return dilateEdges(outline(canvas, threshold));
    }

    public PGraphics rgbSplit(PGraphics canvas, float alter) {
        rgbSplitPass.beginDraw();
        rgbSplitPass.clear();
        rgbSplitFilter.set("alter", alter);
        rgbSplitPass.imageMode(CORNER);
        rgbSplitPass.image(canvas, 0, 0);
        rgbSplitPass.endDraw();
        return rgbSplitPass;
    }

    public PGraphics bloom(PGraphics canvas, float threshold) {
        canvas.imageMode(CORNER);
        PGraphics filtered = filter(canvas, threshold);
        PGraphics hBlurred = hBlur(filtered, 3F);
        PGraphics vBlurred = vBlur(hBlurred, 3F);
        finalFilter.beginDraw();
        finalFilter.clear();
        finalFilter.image(vBlurred, 0, 0);
        finalFilter.blendMode(SCREEN);
        float filterSize = 3.4F;
        for (int i = 0; i < 2; i++) {
            filterSize += 0.8F;
            finalFilter.image(vBlur(hBlur(vBlurred, filterSize), filterSize), 0, 0);
        }
        finalFilter.endDraw();
        return finalFilter;
    }


    public PGraphics blur(PGraphics canvas) {
        float filterSize = 3.4F;
        PGraphics hBlurred = hBlur(canvas, 3F);
        PGraphics vBlurred = vBlur(hBlurred, 3F);
        finalFilter.beginDraw();
        finalFilter.clear();
        finalFilter.image(vBlurred, 0, 0);
        finalFilter.blendMode(SCREEN);
        for (int i = 0; i < 2; i++) {
            filterSize += 0.8F;
            finalFilter.image(vBlur(hBlur(vBlurred, filterSize), filterSize), 0, 0);
        }
        finalFilter.endDraw();
        return finalFilter;
    }


    public static List<AnimationRenderer> generateRandomEnvironment() {
        List<AnimationRenderer> animationRenderers = new ArrayList<>();

        int xDim = (int) Math.ceil(1F * WORLD_WIDTH / X_FOV) * 2;
        int yDim = (int) Math.ceil(1F * WORLD_HEIGHT / Y_FOV) * 2;
        for (int i = 0; i < xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                for (int k = 0; k < 1; k++) {
                    double x = (X_FOV * i + (Math.random() * X_FOV)) * 0.5;
                    double y = (Y_FOV * j + (Math.random() * Y_FOV)) * 0.5;
                    Animation anim = new Animation(0, 0, 500, false, false, "Rock0");
                    animationRenderers.add(new AnimationRenderer(x, y, "Rock0", anim));
                }
            }
        }
        return animationRenderers;
    }

    public void loadEnvironment(String path) {
        String directory = path + "img" + SEPARATOR + "environment" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (String gifLink : contents) {
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String objectName = gifLink.split("[.]")[0];
                frameMap.put(objectName, pGraphics);
                sizeMap.put(objectName, pGraphics[0].width);
            }
        }
    }


    public void loadFilters(String path) {
        String directory = path + "img" + SEPARATOR + "bloomfilters" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (String gifLink : contents) {
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String characterName = gifLink.split("[.]")[0];
                filterMap.put(characterName, pGraphics);
            }
        }
    }

    public void loadChars(String path) {
        String directory = path + "img" + SEPARATOR + "chars" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (String gifLink : contents) {
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String characterName = gifLink.split("[.]")[0];
                frameMap.put(characterName, pGraphics);
                sizeMap.put(characterName, pGraphics[0].width);
                Animation[] animations = AnimationLoader.loadCharAnimations(characterName);
                int[][] shadowLocations = ShadowLoader.getShadowLocations(gif, animations);
                shadowLocationMap.put(characterName, shadowLocations);
                float[] healthBarYLocations = HealthBarLoader.getHealthBarYLocations(gif, animations);
                healthBarYMap.put(characterName, healthBarYLocations);
            }
        }
    }

    public void loadProjectiles(String path) {
        String directory = path + "img" + SEPARATOR + "projectiles" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String gifLink = contents[i];
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String projectileName = gifLink;
                frameMap.put(projectileName, pGraphics);
                sizeMap.put(projectileName, frames[0].width);
            }
        }
    }

    public void loadMarkers(String path) {
        String directory = path + "img" + SEPARATOR + "markers" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String gifLink = contents[i];
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String projectileName = gifLink;
                frameMap.put(projectileName, pGraphics);
                sizeMap.put(projectileName, frames[0].width);
            }
        }
    }

    public void loadParticles(String path) {
        String directory = path + "img" + SEPARATOR + "particles" + SEPARATOR;
        File directoryPath = new File(directory);
        String[] contents = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            String gifLink = contents[i];
            if (gifLink.endsWith(".gif")) {
                Gif gif = new Gif(this, directory + gifLink);
                PImage[] frames = gif.getPImages();
                PGraphics[] pGraphics = new PGraphics[frames.length];
                for (int j = 0; j < frames.length; j++) {
                    pGraphics[j] = createGraphics(frames[j].width, frames[j].height);
                    pGraphics[j].beginDraw();
                    pGraphics[j].image(frames[j], 0, 0);
                    pGraphics[j].endDraw();
                }
                String particleName = gifLink;
                frameMap.put(particleName, pGraphics);
                sizeMap.put(particleName, frames[0].width);
            }
        }
    }

    public void drawWorld(PGraphics canvas, PGraphics filter, World world) {
        List<Char> characters = getSortedCharacters(world);
        List<Trail> trails = world.getTrails();
        List<Projectile> projectiles = world.getProjectiles();
        List<AnimationRenderer> animationRenderers = world.getAnimationRenderers();
        List<DamageAnimation> damageAnimations = world.getDamageAnimations();
        List<Particle> particles = world.getParticles();

        canvas.tint(255F, 127);
        for (Char character : characters) {
            PImage[] frames = frameMap.get("Shadow.gif");
            PImage currentImage = frames[0];
            int[][] shadowLocations = shadowLocationMap.get(character.getCHARACTER_NAME());
            int[] xyLength = shadowLocations[character.getAnimationIndex()];
            drawShadow(canvas,
                    currentImage,
                    (float) (character.getX() + xyLength[0] * WORLD_IMG_SIZE),
                    (float) (character.getY() + (xyLength[1] + 0.5) * WORLD_IMG_SIZE),
                    xyLength[2]);
        }
        canvas.noTint();

        drawRenderers(canvas, filter, animationRenderers);

        for (Trail trail : trails) {
            String animation = trail.getIMAGE();
            PImage currentImage = frameMap.get(animation)[trail.getORIENTATION()];
            drawCostume(canvas, currentImage, (float) trail.getX(), (float) trail.getY(), currentImage.width, WORLD_PROJECTILE_SIZE);
        }

        for (Projectile projectile : projectiles) {
            String animation = projectile.getIMAGE();
            PImage[] projectileFrames = frameMap.get(animation);
            PImage currentImage = projectileFrames[projectile.getORIENTATION()];
            drawCostume(canvas, currentImage, (float) projectile.getX(), (float) projectile.getY(), currentImage.width, WORLD_PROJECTILE_SIZE);
        }

        drawChars(canvas, filter, characters);
        drawBars(canvas, characters);

        for (Particle particle : particles) {
            String animation = particle.getIMAGE();
            PImage currentImage = frameMap.get(animation)[particle.getAnimationIndex()];
            drawCostume(canvas, currentImage, (float) particle.getX(), (float) particle.getY(), currentImage.width, WORLD_PARTICLE_SIZE);
        }

        for (DamageAnimation damageAnimation : damageAnimations) {
            canvas.fill(damageAnimation.getCOLOR().getRGB());
            canvas.textFont(fonts[damageAnimation.getSIZE_INDEX()]);
            canvas.textAlign(CENTER, CENTER);
            canvas.text(damageAnimation.getTEXT(), (float) (damageAnimation.getX()), (float) (damageAnimation.getY()));
        }

    }

    public List<Char> getSortedCharacters(World world) {
        List<Char> charactersUnsorted = world.getCharacters();
        List<Pair<Char>> characterPairs = new ArrayList<>();
        List<Char> characters = new ArrayList<>();
        for (Char character : charactersUnsorted) {
            characterPairs.add(new Pair(character, character.getY()));
        }
        Collections.sort(characterPairs);
        for (Pair<Char> pair : characterPairs) {
            characters.add(pair.getObject());
        }
        return characters;
    }

    public boolean doesRender(float x, float y) {
        return Math.abs(x - PLAYER.getX()) < X_FOV + DRAW_LIMIT && Math.abs(y - PLAYER.getY()) < Y_FOV + DRAW_LIMIT;
    }

    public void drawChars(PGraphics canvas, PGraphics filter, List<Char> characters) {
        for (Char character : characters) {
            String animation = character.getCHARACTER_NAME();
            PImage[] characterFrames = frameMap.get(animation);
            PImage currentImage = characterFrames[character.getAnimationIndex()];
            drawCostume(canvas, currentImage, (float) character.getX(), (float) character.getY(), currentImage.width, WORLD_IMG_SIZE);

            PImage[] filterFrames = filterMap.get(animation);
            if (filterFrames != null) {
                PImage filterImage = filterFrames[character.getAnimationIndex()];
                drawCostume(filter, filterImage, (float) character.getX(), (float) character.getY(), filterImage.width, WORLD_IMG_SIZE);
            }
        }
    }

    public void drawRenderers(PGraphics canvas, PGraphics filter, List<AnimationRenderer> animationRenderers) {
        for (AnimationRenderer animationRenderer : animationRenderers) {
            String animation = animationRenderer.getNAME();
            PImage currentImage = frameMap.get(animation)[animationRenderer.getAnimationIndex()];
            drawCostume(canvas, currentImage, (float) animationRenderer.getX(), (float) animationRenderer.getY(), currentImage.width, WORLD_IMG_SIZE);
            PImage[] filterFrames = filterMap.get(animation);
            if (filterFrames != null) {
                PImage filterImage = filterFrames[animationRenderer.getAnimationIndex()];
                drawCostume(filter, filterImage, (float) animationRenderer.getX(), (float) animationRenderer.getY(), filterImage.width, WORLD_IMG_SIZE);
            }
        }
    }

    public void drawBars(PGraphics canvas, List<Char> characters) {
        for (Char character : characters) {
            float healthBarY = healthBarYMap.get(character.getCHARACTER_NAME())[character.getAnimationIndex()];
            float y = (float) (character.getY() + healthBarY * WORLD_IMG_SIZE);
            drawHealthBar(canvas, (float) character.getX(), y, (float) (character.getHealth() / character.getMAX_HEALTH()));
            drawDeflectBar(canvas, (float) character.getX(), y, (float) (character.getDeflectionChance()));
            drawStaminaBar(canvas, (float) character.getX(), y, (float) (character.getStamina() / character.getSTAMINA()));
        }
    }

    public void drawShadow(PGraphics canvas, PImage costume, float centerX, float centerY, float size) {
        if (doesRender(centerX, centerY)) {
            float newWidth = size * WORLD_IMG_SIZE;
            float newHeight = (float) Math.sqrt(size) * 4 * WORLD_IMG_SIZE;
            canvas.imageMode(CENTER);
            canvas.pushMatrix();
            canvas.translate(centerX, centerY);
            canvas.image(costume, 0, 0, newWidth, newHeight);
            canvas.popMatrix();
        }
    }

    public void drawCostume(PGraphics canvas, PImage costume, float centerX, float centerY, float size, float imgSize) {
        if (doesRender(centerX, centerY)) {
            float newSize = size * imgSize;
            canvas.imageMode(CENTER);
            canvas.pushMatrix();
            canvas.translate(centerX, centerY);
            canvas.image(costume, 0, 0, newSize, newSize);
            canvas.popMatrix();
        }
    }

    public void drawHealthBar(PGraphics canvas, float centerX, float centerY, float percentage) {
        if (doesRender(centerX, centerY)) {
            final float LENGTH = 10;
            final float HEIGHT_TO_WIDTH_RATIO = 0.2F;
            float healthBarWidth = LENGTH * WORLD_IMG_SIZE;
            float healthBarHeight = healthBarWidth * HEIGHT_TO_WIDTH_RATIO;
            float healthBarX = centerX - healthBarWidth / 2;
            float healthBarY = centerY - healthBarHeight;

            int red = (int) Math.min((1 - percentage) * 510, 255);
            int green = (int) Math.min(percentage * 510, 255);

            canvas.fill(0);
            canvas.stroke(0);
            canvas.pushMatrix();
            canvas.translate(healthBarX, healthBarY);
            canvas.rect(0, 0, healthBarWidth, healthBarHeight);
            canvas.fill(red, green, 0);
            canvas.stroke(0F);
            canvas.rect(0, 0, healthBarWidth * percentage, healthBarHeight);
            canvas.popMatrix();
        }
    }


    public void drawDeflectBar(PGraphics canvas, float centerX, float centerY, float percentage) {
        if (doesRender(centerX, centerY)) {
            final float LENGTH = 10;
            final float HEIGHT_TO_WIDTH_RATIO = 0.2F;
            float deflectBarWidth = LENGTH * WORLD_IMG_SIZE;
            float deflectBarHeight = deflectBarWidth * HEIGHT_TO_WIDTH_RATIO;
            deflectBarWidth *= percentage;
            float deflectBarX = centerX - deflectBarWidth / 2;
            float deflectBarY = centerY - deflectBarHeight * 2 - 2;

            canvas.pushMatrix();
            canvas.translate(deflectBarX, deflectBarY);
            canvas.fill(255, 255, 0);
            canvas.stroke(0F);
            canvas.rect(0, 0, deflectBarWidth, deflectBarHeight);
            canvas.popMatrix();
        }
    }

    public void drawStaminaBar(PGraphics canvas, float centerX, float centerY, float percentage) {
        if (doesRender(centerX, centerY)) {
            final float LENGTH = 10;
            final float HEIGHT_TO_WIDTH_RATIO = 0.2F;
            float deflectBarWidth = LENGTH * WORLD_IMG_SIZE;
            float deflectBarHeight = deflectBarWidth * HEIGHT_TO_WIDTH_RATIO;
            deflectBarWidth *= percentage;
            float deflectBarX = centerX - deflectBarWidth / 2;
            float deflectBarY = centerY - deflectBarHeight * 3 - 4;

            canvas.pushMatrix();
            canvas.translate(deflectBarX, deflectBarY);
            canvas.fill(255, 0, 255);
            canvas.stroke(0F);
            canvas.rect(0, 0, deflectBarWidth, deflectBarHeight);
            canvas.popMatrix();
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        char key = keyEvent.getKey();
        int keyCode = keyEvent.getKeyCode();
        if (key == 'd' || keyCode == RIGHT) {
            KEYS[0] = true;
        } else if (key == 's' || keyCode == DOWN) {
            KEYS[1] = true;
        } else if (key == 'a' || keyCode == LEFT) {
            KEYS[2] = true;
        } else if (key == 'w' || keyCode == UP) {
            KEYS[3] = true;
        } else if (key == ' ') {
            KEYS[4] = true;
        } else if (key == 'f') {
            KEYS[5] = true;
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        char key = keyEvent.getKey();
        int keyCode = keyEvent.getKeyCode();
        if (key == 'd' || keyCode == RIGHT) {
            KEYS[0] = false;
        } else if (key == 's' || keyCode == DOWN) {
            KEYS[1] = false;
        } else if (key == 'a' || keyCode == LEFT) {
            KEYS[2] = false;
        } else if (key == 'w' || keyCode == UP) {
            KEYS[3] = false;
        } else if (key == ' ') {
            KEYS[4] = false;
        } else if (key == 'f') {
            KEYS[5] = false;
        }
    }
}
