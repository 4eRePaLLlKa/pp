package com.droids.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.droids.game.MainGame;
import com.droids.game.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Екран для вибору дроїдів перед початком бою
public class TeamSelectionScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;

    private final int MAX_DROIDS_PER_TEAM_CAP = 6;
    private final boolean isOneVsOneMode; // Режим 1 на 1 чи Команда на Команду

    private List<Droid> teamA = new ArrayList<>(); // Дроїди Команди А
    private List<Droid> teamB = new ArrayList<>(); // Дроїди Команди Б

    private boolean selectingTeamA = true; // Прапорець, яку команду обираємо
    private Label selectionStatusLabel; // Напис зі статусом вибору
    private Table teamPreviewTableA; // Панель для відображення Команди А
    private Table teamPreviewTableB; // Панель для відображення Команди Б

    // Конструктор за замовчуванням (Команда на Команду)
    public TeamSelectionScreen(MainGame game) {
        this(game, false);
    }

    // Основний конструктор
    public TeamSelectionScreen(MainGame game, boolean isOneVsOneMode) {
        super(game);
        this.isOneVsOneMode = isOneVsOneMode;
        stage = new Stage(new ScreenViewport());

        setupSkin();
        createUI();
        updateStatusLabel();
    }

    // Налаштування стилів UI
    private void setupSkin() {
        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Стиль кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        // Стиль написів
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);
    }

    // Створення всіх елементів інтерфейсу
    private void createUI() {
        Table rootTable = new Table(skin);
        rootTable.setFillParent(true);
        rootTable.defaults().pad(10);

        // 1. Статус вибору
        selectionStatusLabel = new Label("", skin);
        rootTable.add(selectionStatusLabel).colspan(3).padTop(20).row();

        // 2. Панелі попереднього перегляду команд
        teamPreviewTableA = new Table(skin);
        teamPreviewTableB = new Table(skin);

        rootTable.add(new Label("Team A:", skin)).left().padRight(50);
        rootTable.add(new Label("Team B:", skin)).left().row();

        rootTable.add(teamPreviewTableA).top().width(Gdx.graphics.getWidth() / 2f);
        rootTable.add(teamPreviewTableB).top().width(Gdx.graphics.getWidth() / 2f).row();

        // 3. Роздільник
        rootTable.add(new Label(isOneVsOneMode ? "--- Select 2 Fighters ---" : "--- Select Droids ---", skin)).colspan(3).padTop(40).row();

        // 4. Панель для вибору доступних дроїдів (кнопки)
        Table droidSelectionTable = new Table(skin);

        // Створення кнопок для кожного типу дроїда
        for (Map.Entry<String, Texture> entry : game.droidTextures.entrySet()) {
            final String droidKey = entry.getKey();

            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.imageUp = new TextureRegionDrawable(entry.getValue());
            ImageButton button = new ImageButton(style);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    addDroidToTeam(droidKey); // Обробка натискання
                }
            });
            droidSelectionTable.add(button).size(game.SMALL_DROID_SIZE).pad(10);
            droidSelectionTable.add(new Label(droidKey, skin)).padRight(20);
        }
        rootTable.add(droidSelectionTable).colspan(3).padBottom(40).row();

        // 5. Кнопка Переходу/Завершення
        TextButton nextButton = new TextButton(isOneVsOneMode ? "Start 1 vs 1 Battle" : "Next Team / Finish Selection", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isOneVsOneMode) {
                    // Перехід до бою 1 на 1
                    if (teamA.size() == 1 && teamB.size() == 1) {
                        game.setScreen(new BattleScreen(game, teamA, teamB));
                    } else {
                        selectionStatusLabel.setText("ERROR: Select exactly one droid for each team!");
                        selectionStatusLabel.setColor(Color.RED);
                    }
                } else {
                    switchTeam(); // Перехід до вибору Команди Б або до бою
                }
            }
        });
        rootTable.add(nextButton).width(300).height(60).padBottom(40).colspan(3).row();

        stage.addActor(rootTable);
    }

    // Зміна команди, яку обираємо, або старт бою
    private void switchTeam() {
        if (selectingTeamA) {
            // Перевірка мінімального розміру Команди А
            if (teamA.isEmpty()) {
                selectionStatusLabel.setText("ERROR: Team A must have at least one droid!");
                selectionStatusLabel.setColor(Color.RED);
                return;
            }
            selectingTeamA = false;
            updateStatusLabel();
        } else {
            // Перевірка мінімального розміру Команди Б
            if (teamB.isEmpty()) {
                selectionStatusLabel.setText("ERROR: Team B must have at least one droid!");
                selectionStatusLabel.setColor(Color.RED);
                return;
            }
            // Старт бою (Команда на Команду)
            game.setScreen(new BattleScreen(game, teamA, teamB));
        }
    }

    // Додавання обраного дроїда до поточної команди
    private void addDroidToTeam(String droidKey) {
        int maxLimit = isOneVsOneMode ? 1 : MAX_DROIDS_PER_TEAM_CAP;

        // Перевірка, чи не заповнені обидві команди
        if (teamA.size() >= maxLimit && teamB.size() >= maxLimit) {
            updateStatusLabel();
            return;
        }

        Droid newDroid = createNewDroidInstance(droidKey); // Створення нового екземпляра

        // Додавання до Команди А
        if (selectingTeamA) {
            if (teamA.size() < maxLimit) {
                teamA.add(newDroid);
                if (isOneVsOneMode) switchTeam(); // Для 1 vs 1 одразу переходимо до вибору Команди Б
            } else {
                if (!isOneVsOneMode) selectionStatusLabel.setText("Team A is full (max 6). Click 'Next Team'.");
            }
            // Додавання до Команди Б
        } else {
            if (teamB.size() < maxLimit) {
                teamB.add(newDroid);
            } else {
                selectionStatusLabel.setText(isOneVsOneMode ? "Both teams are ready!" : "Team B is full (max 6). Click 'Finish Selection'.");
            }
        }
        updateStatusLabel();
        updateTeamPreview(); // Оновлення відображення іконок
    }

    // Оновлення відображення іконок обраних дроїдів
    private void updateTeamPreview() {
        teamPreviewTableA.clear();
        teamPreviewTableB.clear();

        for (Droid droid : teamA) {
            String key = droid.getType().toLowerCase().replace("droid", "");
            Texture texture = game.droidTextures.get(key);
            if (texture != null) {
                teamPreviewTableA.add(new Image(texture)).size(game.SMALL_DROID_SIZE / 2f).pad(2);
            }
        }
        for (Droid droid : teamB) {
            String key = droid.getType().toLowerCase().replace("droid", "");
            Texture texture = game.droidTextures.get(key);
            if (texture != null) {
                teamPreviewTableB.add(new Image(texture)).size(game.SMALL_DROID_SIZE / 2f).pad(2);
            }
        }
    }

    // Оновлення тексту та кольору статусу
    private void updateStatusLabel() {
        String status = selectingTeamA ? "SELECTING TEAM A" : "SELECTING TEAM B";
        int max = isOneVsOneMode ? 1 : MAX_DROIDS_PER_TEAM_CAP;
        selectionStatusLabel.setText(status + " | A: " + teamA.size() + "/" + max + " | B: " + teamB.size() + "/" + max);
        selectionStatusLabel.setColor(selectingTeamA ? Color.GREEN : Color.BLUE);
    }

    // Створення нового екземпляра дроїда за ключем
    private Droid createNewDroidInstance(String droidKey) {
        String name = droidKey + (selectingTeamA ? " A" : " B") + (selectingTeamA ? teamA.size() + 1 : teamB.size() + 1);

        switch (droidKey.toLowerCase()) {
            case "knight": return new KnightDroid(name);
            case "assassin": return new AssassinDroid(name);
            case "repair": return new RepairDroid(name);
            case "mage": return new MageDroid(name);
            case "battle": return new BattleDroid(name);
            default: return new BattleDroid("Default Droid");
        }
    }

    @Override public void show() { Gdx.input.setInputProcessor(stage); } // Встановлення обробника вводу
    @Override public void render(float delta) {
        // Малювання фону
        game.batch.begin();
        game.batch.draw(game.arenaBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); // Оновлення UI
        stage.draw(); // Малювання UI
    }
    @Override public void dispose() { stage.dispose(); skin.dispose(); } // Звільнення ресурсів
}
