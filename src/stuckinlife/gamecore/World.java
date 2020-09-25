package stuckinlife.gamecore;

import stuckinlife.gamecore.animation.DamageAnimation;
import stuckinlife.gamecore.animation.AnimationRenderer;
import stuckinlife.gamecore.animation.Particle;
import stuckinlife.gamecore.animation.Trail;
import stuckinlife.gamecore.characters.*;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.gameutils.Animation;
import stuckinlife.gamecore.level.Level;
import stuckinlife.gamecore.projectile.Projectile;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final int WORLD_WIDTH;
    private final int WORLD_HEIGHT;

    private final Level LEVEL;

    private final double WALL_MARGIN = 30;
    private final double MAX_X;
    private final double MAX_Y;
    private final double MIN_X;
    private final double MIN_Y;

    private List<Char> characters = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<AnimationRenderer> animationRenderers = new ArrayList<>();
    private List<DamageAnimation> damageAnimations = new ArrayList<>();
    private List<Trail> trails = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();

    private List<Char> charactersToAdd = new ArrayList<>();
    private List<Projectile> projectilesToAdd = new ArrayList<>();
    private List<Trail> trailsToAdd = new ArrayList<>();
    private List<Particle> particlesToAdd = new ArrayList<>();

    private List<Char> charactersToRemove = new ArrayList<>();
    private List<Projectile> projectilesToRemove = new ArrayList<>();
    private List<DamageAnimation> damageAnimationsToRemove = new ArrayList<>();
    private List<Trail> trailsToRemove = new ArrayList<>();
    private List<Particle> particlesToRemove = new ArrayList<>();

    Player PLAYER;

    public World(int width, int height, Level level, List<AnimationRenderer> renderers) {
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;

        LEVEL = level;
        LEVEL.init();

        MAX_X = WORLD_WIDTH - WALL_MARGIN;
        MAX_Y = WORLD_HEIGHT - WALL_MARGIN;
        MIN_X = WALL_MARGIN;
        MIN_Y = WALL_MARGIN;

        animationRenderers.addAll(renderers);
    }

    public void addPlayer(Player player) {
        PLAYER = player;
        addCharacter(PLAYER);
    }

    public void addCharacter(Char character) {
        charactersToAdd.add(character);
    }

    public void addCharacters(List<Char> characters) {
        charactersToAdd.addAll(characters);
    }

    public void addProjectile(Projectile projectile) {
        projectilesToAdd.add(projectile);
    }

    public void addTrail(Trail trail) {
        trailsToAdd.add(trail);
    }

    public void addParticle(Particle particle) {
        particlesToAdd.add(particle);
    }

    public void update(int milliseconds) {
        updateDamageAnimations(milliseconds);
        updateDeathAnimations(milliseconds);
        updateProjectiles(milliseconds);
        updateCharacters(milliseconds);
        updateTrails(milliseconds);
        updateParticles(milliseconds);

        LEVEL.update(this, milliseconds);
    }

    public void updateCharacters(int milliseconds) {

        for (Char character : charactersToRemove) {
            animationRenderers.add(character.getDeathAnimation());
        }

        characters.removeAll(charactersToRemove);
        charactersToRemove.clear();

        for (Char character : characters) {
            character.update(milliseconds);
        }

        characters.addAll(charactersToAdd);
        charactersToAdd.clear();

    }

    public void updateProjectiles(int milliseconds) {
        projectiles.removeAll(projectilesToRemove);
        projectilesToRemove.clear();


        for (Projectile projectile : projectiles) {
            for (Char character : characters) {
                if (projectile.getTEAM_CODE() != character.getTEAM_CODE()) {
                    if (projectile.hit(character.getX(), character.getY())) {
                        double damage = projectile.getDamage(character);
                        boolean critical = false;
                        if (Math.random() < projectile.getCRITICAL_CHANCE()) {
                            damage *= 2;
                            critical = true;
                        }
                        double damageTaken = character.attacked(projectile.getOWNER(), damage);
                        projectile.getOWNER().hit(character, damageTaken);
                        if (character.getHealth() <= 0) {
                            character.onDeath(projectile.getOWNER());
                            projectile.getOWNER().onKill(character);
                            charactersToRemove.add(character);
                        }
                        for (int i = 0; i < 8; i++) {
                            Particle damageParticle = new Particle(
                                    character.getX(),
                                    character.getY(),
                                    Math.random() * 3 + 2,
                                    Math.random() * Math.PI * 2,
                                    0.92,
                                    400,
                                    new Animation(0, 3, 30, true, false, "DamageParticle.gif"));
                            addParticle(damageParticle);
                        }
                        DamageAnimation damageAnimation = new DamageAnimation(character.getX(), character.getY(), damageTaken, critical);
                        damageAnimations.add(damageAnimation);
                    }
                }
            }
            if (projectile.destroy() || projectile.getX() < 0 || projectile.getX() > WORLD_WIDTH || projectile.getY() < 0 || projectile.getY() >= WORLD_HEIGHT) {
                projectilesToRemove.add(projectile);
            }
            projectile.update(this, milliseconds);
        }

        projectiles.addAll(projectilesToAdd);
        projectilesToAdd.clear();
    }

    public void updateDeathAnimations(int milliseconds) {
        for (AnimationRenderer animationRenderer : animationRenderers) {
            if (!animationRenderer.isComplete()) {
                animationRenderer.update(milliseconds);
            }
        }
    }

    public void updateDamageAnimations(int milliseconds) {

        damageAnimations.removeAll(damageAnimationsToRemove);
        damageAnimationsToRemove.clear();

        for (DamageAnimation damageAnimation : damageAnimations) {
            damageAnimation.update(milliseconds);
            if (damageAnimation.isComplete()) {
                damageAnimationsToRemove.add(damageAnimation);
            }
        }
    }

    public void updateTrails(int milliseconds) {
        trails.removeAll(trailsToRemove);
        trailsToRemove.clear();
        for (Trail trail : trails) {
            trail.update(milliseconds);
            if (trail.isComplete()) {
                trailsToRemove.add(trail);
            }
        }
        trails.addAll(trailsToAdd);
        trailsToAdd.clear();
    }

    public void updateParticles(int milliseconds) {
        particles.removeAll(particlesToRemove);
        particlesToRemove.clear();
        for (Particle particle : particles) {
            particle.update(milliseconds);
            if (particle.isComplete()) {
                particlesToRemove.add(particle);
            }
        }
        particles.addAll(particlesToAdd);
        particlesToAdd.clear();
    }

    public int getWORLD_WIDTH() {
        return WORLD_WIDTH;
    }

    public int getWORLD_HEIGHT() {
        return WORLD_HEIGHT;
    }

    public double getMAX_X() {
        return MAX_X;
    }

    public double getMAX_Y() {
        return MAX_Y;
    }

    public double getMIN_X() {
        return MIN_X;
    }

    public double getMIN_Y() {
        return MIN_Y;
    }

    public List<Char> getCharacters() {
        return characters;
    }

    public List<Trail> getTrails() {
        return trails;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public List<AnimationRenderer> getAnimationRenderers() {
        return animationRenderers;
    }

    public List<DamageAnimation> getDamageAnimations() {
        return damageAnimations;
    }

    public List<Particle> getParticles() {
        return particles;
    }


}
