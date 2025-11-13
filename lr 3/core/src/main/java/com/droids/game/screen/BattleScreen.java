package com.droids.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.droids.game.MainGame;
import com.droids.game.battle.TeamBattle;
import com.droids.game.model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Екран для візуалізації командного бою (покроковий режим)
public class BattleScreen extends BaseScreen {

    private final List<Droid> teamA;
    private final List<Droid> teamB;
    private final boolean isOneVsOneMode; // Режим 1 на 1

    // --- Логіка Бою ---
    private final TeamBattle teamBattle;
    private float timeSinceLastTurn = 0f;
    private final float TURN_DELAY = 2.0f; // Затримка між ходами (2 секунди)
    private boolean battleRunning = true; // Стан бою
    private int lastLogIndex = 0;

    // --- Логіка завершення ---
    private float timeAfterBattleEnd = 0f;
    private final float EXIT_DELAY = 3.0f; // Затримка перед виходом до меню

    // --- Візуалізація ---
    private ShapeRenderer shapeRenderer; // Для малювання HP-барів
    private final Map<Droid, Integer> maxHealthMap; // Зберігає початкове HP для розрахунку бару

    // Параметри розміщення
    private final float DROID_DRAW_SIZE = 80f; // Розмір спрайта дроїда
    private final float DROID_PADDING_Y = 20f;
    private final float DROID_PADDING_X = 50f;
    private final float GRID_OFFSET_X_TEAM_A = 150f;
    private final float GRID_OFFSET_X_TEAM_B = 150f;
    private final float GRID_CENTER_OFFSET_Y;
    private final float ONE_VS_ONE_CENTER_OFFSET = 150f;

    // Конструктор
    public BattleScreen(MainGame game, List<Droid> teamA, List<Droid> teamB) {
        super(game);
        this.teamA = teamA;
        this.teamB = teamB;
        this.maxHealthMap = new HashMap<>();
        this.isOneVsOneMode = (teamA.size() == 1 && teamB.size() == 1);

        // Обчислення центрального зміщення Y
        float totalTeamHeight = (DROID_DRAW_SIZE * 3) + (DROID_PADDING_Y * 2);
        GRID_CENTER_OFFSET_Y = (Gdx.graphics.getHeight() - totalTeamHeight) / 2f;

        initializeMaxHealths(teamA); // Ініціалізація максимального HP
        initializeMaxHealths(teamB);

        this.teamBattle = new TeamBattle(teamA, teamB); // Ініціалізація логіки бою

        // Логування початкового статусу
        List<String> initialLog = teamBattle.getBattleLog();
        for (String line : initialLog) {
            Gdx.app.log("Battle Setup", line);
        }
        lastLogIndex = initialLog.size();
    }

    // Допоміжний метод для визначення максимального HP дроїда
    private void initializeMaxHealths(List<Droid> team) {
        for (Droid droid : team) {
            Droid tempDroid;
            String className = droid.getClass().getSimpleName();

            // Використовуємо тимчасовий екземпляр для отримання maxHealth
            switch (className) {
                case "KnightDroid": tempDroid = new KnightDroid("temp"); break;
                case "AssassinDroid": tempDroid = new AssassinDroid("temp"); break;
                case "RepairDroid": tempDroid = new RepairDroid("temp"); break;
                case "MageDroid": tempDroid = new MageDroid("temp"); break;
                case "BattleDroid": tempDroid = new BattleDroid("temp"); break;
                default: tempDroid = new Droid("temp", 100, 10); break;
            }
            maxHealthMap.put(droid, tempDroid.getHealth());
        }
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
    }

    // Основний цикл рендерингу (малювання та оновлення)
    @Override
    public void render(float delta) {
        if (battleRunning) {
            timeSinceLastTurn += delta;
            // Перевіряємо, чи настав час для наступного ходу
            if (timeSinceLastTurn >= TURN_DELAY) {
                battleRunning = teamBattle.takeTurn(); // Виконуємо хід

                // Вивід нового логу в консоль
                List<String> currentLog = teamBattle.getBattleLog();
                for (int i = lastLogIndex; i < currentLog.size(); i++) {
                    Gdx.app.log("Battle Log", currentLog.get(i));
                }
                lastLogIndex = currentLog.size();

                timeSinceLastTurn = 0f;
            }
        } else {
            // Логіка затримки після закінчення бою
            timeAfterBattleEnd += delta;
            if (timeAfterBattleEnd >= EXIT_DELAY) {
                Gdx.app.log("BATTLE END", "Delay finished. Returning to Menu.");
                game.setScreen(new MenuScreen(game)); // Повернення до меню (потрібен MenuScreen)
                return;
            }
        }

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1); // Очищення екрана

        game.batch.begin();
        game.batch.draw(game.arenaBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Малювання фону

        // Малювання дроїдів
        if (isOneVsOneMode) {
            drawOneVsOne();
        } else {
            drawTeam(teamA, true); // Малюємо Команду А
            drawTeam(teamB, false); // Малюємо Команду Б (віддзеркалену)
        }

        game.batch.end();

        drawHealthBars(); // Малювання HP-барів (ShapeRenderer)
    }

    // Малювання в режимі 1 на 1
    private void drawOneVsOne() {
        if (teamA.isEmpty() || teamB.isEmpty()) return;

        float center_X = Gdx.graphics.getWidth() / 2f;
        float center_Y = Gdx.graphics.getHeight() / 2f;

        // Малюємо дроїдів зі зміщенням
        drawSingleDroid(teamA.get(0), center_X - ONE_VS_ONE_CENTER_OFFSET, center_Y, true);
        drawSingleDroid(teamB.get(0), center_X + ONE_VS_ONE_CENTER_OFFSET, center_Y, false);
    }

    // Малювання одного дроїда (використовується в 1vs1)
    private void drawSingleDroid(Droid droid, float x, float y, boolean facingRight) {
        SpriteBatch batch = game.batch;
        String droidKey = getCorrectedDroidTextureKey(droid);
        Texture texture = game.droidTextures.get(droidKey);

        // Не малюємо мертвих дроїдів після закінчення бою
        if (!droid.isAlive() && !battleRunning) {
            return;
        }

        float size = DROID_DRAW_SIZE;

        // Зміна кольору для мертвих/пошкоджених
        if (!droid.isAlive()) { batch.setColor(0.5f, 0.5f, 0.5f, 0.5f); }
        else { batch.setColor(Color.WHITE); }

        // Малювання спрайта (віддзеркалення, якщо потрібно)
        if (facingRight) {
            batch.draw(texture, x, y, size, size);
        } else {
            batch.draw(texture, x + size, y, -size, size);
        }
        batch.setColor(Color.WHITE);
    }

    // Малювання цілої команди (в режимі сітки)
    private void drawTeam(List<Droid> team, boolean facingRight) {
        SpriteBatch batch = game.batch;
        float screenWidth = Gdx.graphics.getWidth();
        float startX;
        float teamWidth = DROID_DRAW_SIZE + DROID_PADDING_X;

        // Визначення стартової позиції X
        if (facingRight) {
            startX = GRID_OFFSET_X_TEAM_A;
        } else {
            startX = screenWidth - GRID_OFFSET_X_TEAM_B - teamWidth;
        }

        float startY = GRID_CENTER_OFFSET_Y;
        int rowCount = 3;

        for (int i = 0; i < team.size(); i++) {
            Droid droid = team.get(i);

            String droidKey = getCorrectedDroidTextureKey(droid);
            Texture texture = game.droidTextures.get(droidKey);
            if (texture == null) continue;

            // Розрахунок позиції в сітці
            int columnIndex = i / rowCount;
            int rowIndex = i % rowCount;

            float drawX = startX + columnIndex * (DROID_DRAW_SIZE + DROID_PADDING_X);
            float drawY = startY + rowIndex * (DROID_DRAW_SIZE + DROID_PADDING_Y);

            // Налаштування кольору для мертвих дроїдів
            if (!droid.isAlive()) {
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            } else {
                batch.setColor(Color.WHITE);
            }

            // Малювання спрайта
            if (facingRight) {
                batch.draw(texture, drawX, drawY, DROID_DRAW_SIZE, DROID_DRAW_SIZE);
            } else {
                batch.draw(texture, drawX + DROID_DRAW_SIZE, drawY, -DROID_DRAW_SIZE, DROID_DRAW_SIZE);
            }

            batch.setColor(Color.WHITE);
        }
    }

    // Отримання ключа текстури з назви класу (наприклад, AssassinDroid -> assassin)
    private String getCorrectedDroidTextureKey(Droid droid) {
        return droid.getClass().getSimpleName().toLowerCase().replace("droid", "");
    }

    // Головний метод для малювання HP-барів
    private void drawHealthBars() {
        drawHealthBarsForTeam(teamA, true);
        drawHealthBarsForTeam(teamB, false);
    }

    // Малювання HP-барів для однієї команди
    private void drawHealthBarsForTeam(List<Droid> team, boolean isTeamA) {
        if (shapeRenderer == null) return;

        // **ВИПРАВЛЕННЯ:** Оголошення локальної змінної screenWidth
        float screenWidth = Gdx.graphics.getWidth();

        shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barWidth = DROID_DRAW_SIZE;
        float barHeight = 8f;

        if (isOneVsOneMode) {
            // Логіка малювання HP-барів 1 на 1
            if (team.isEmpty()) {
                shapeRenderer.end();
                return;
            }
            Droid droid = team.get(0);

            if (!droid.isAlive() && !battleRunning) { // Не малюємо бар, якщо дроїд мертвий і бій закінчено
                shapeRenderer.end();
                return;
            }

            float center_X = screenWidth / 2f;
            float center_Y = Gdx.graphics.getHeight() / 2f;

            // Розрахунок позиції бару
            float droidX = isTeamA ? center_X - ONE_VS_ONE_CENTER_OFFSET : center_X + ONE_VS_ONE_CENTER_OFFSET;
            float hpBarX = droidX;
            if (!isTeamA) {
                hpBarX = droidX + DROID_DRAW_SIZE - barWidth;
            }

            float hpBarY = center_Y + DROID_DRAW_SIZE + 5f;

            int maxHealth = maxHealthMap.getOrDefault(droid, 100);
            float healthRatio = (float) droid.getHealth() / maxHealth;

            shapeRenderer.setColor(Color.RED); // Фон (залишок HP)
            shapeRenderer.rect(hpBarX, hpBarY, barWidth, barHeight);
            shapeRenderer.setColor(Color.GREEN); // Поточне HP
            shapeRenderer.rect(hpBarX, hpBarY, barWidth * healthRatio, barHeight);

        } else {
            // Логіка малювання HP-барів командного бою
            float startY = GRID_CENTER_OFFSET_Y;
            int rowCount = 3;

            // Визначення стартової позиції X
            float startX;
            if (isTeamA) {
                startX = GRID_OFFSET_X_TEAM_A;
            } else {
                float teamWidth = DROID_DRAW_SIZE + DROID_PADDING_X;
                // Ось тут була помилка
                startX = screenWidth - GRID_OFFSET_X_TEAM_B - teamWidth;
            }

            for (int i = 0; i < team.size(); i++) {
                Droid droid = team.get(i);
                if (!maxHealthMap.containsKey(droid) || droid.getHealth() <= 0) continue; // Пропуск мертвих

                int columnIndex = i / rowCount;
                int rowIndex = i % rowCount;

                // Розрахунок позиції бару над дроїдом
                float drawX = startX + columnIndex * (DROID_DRAW_SIZE + DROID_PADDING_X);
                float drawY = startY + rowIndex * (DROID_DRAW_SIZE + DROID_PADDING_Y) + DROID_DRAW_SIZE + 5f;

                int maxHealth = maxHealthMap.get(droid);
                float healthRatio = (float) droid.getHealth() / maxHealth;

                shapeRenderer.setColor(Color.RED); // Фон
                shapeRenderer.rect(drawX, drawY, barWidth, barHeight);
                shapeRenderer.setColor(Color.GREEN); // Поточне HP
                shapeRenderer.rect(drawX, drawY, barWidth * healthRatio, barHeight);
            }
        }

        shapeRenderer.end();
    }


    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
