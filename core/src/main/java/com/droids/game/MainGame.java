// com.droids.game.MainGame (ОНОВЛЕНО)

package com.droids.game;

import com.badlogic.gdx.Game; // Основний клас LibGDX для керування екранами
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.droids.game.screen.MenuScreen;
import java.util.HashMap;
import java.util.Map;


public class MainGame extends Game { // Спадкування від Game дозволяє керувати екранами

    // Зроблено публічними, щоб екрани мали прямий доступ до ресурсів
    public SpriteBatch batch; // Об'єкт для малювання 2D графіки
    public Map<String, Texture> droidTextures; // Карта для зберігання текстур дроїдів
    public Texture arenaBackground; // Текстура фону

    // Константи для розмірів спрайтів
    public final float DROID_SIZE = 200f;
    public final float SMALL_DROID_SIZE = 150f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        droidTextures = new HashMap<>();

        // --- 1. Завантаження всіх ресурсів ---
        arenaBackground = new Texture("arena.png");
        // Завантаження текстур дроїдів за ключем (типом)
        droidTextures.put("assassin", new Texture("assassin_droid.png"));
        droidTextures.put("battle", new Texture("battle_droid.png"));
        droidTextures.put("repair", new Texture("repair_droid.png"));
        droidTextures.put("knight", new Texture("knight_droid.png"));
        droidTextures.put("mage", new Texture("mage_droid.png"));

        // --- 2. Встановлюємо перший екран ---
        setScreen(new MenuScreen(this)); // Запускаємо гру з MenuScreen
    }

    @Override
    public void render() {
        // Делегує малювання активному екрану
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose(); // Звільнення ресурсів SpriteBatch
        arenaBackground.dispose(); // Звільнення текстури фону

        // Звільнення всіх завантажених текстур дроїдів
        for (Texture texture : droidTextures.values()) {
            texture.dispose();
        }
    }
}
