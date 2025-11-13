package com.droids.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.droids.game.MainGame;

// Екран головного меню
public class MenuScreen extends BaseScreen {

    private Stage stage; // Сцена для керування елементами UI (кнопки, написи)
    private Skin skin; // Скін для визначення стилів елементів UI

    // Конструктор
    public MenuScreen(MainGame game) {
        super(game);
        stage = new Stage(new ScreenViewport());

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font); // Додавання стандартного шрифту

        // Створення стилю для кнопок
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        // Створення стилю для написів (Label)
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", labelStyle);

        createTable(); // Створення та додавання елементів до таблиці
    }

    // Створює таблицю, яка містить усі елементи меню
    private void createTable() {
        Table table = new Table();
        table.setFillParent(true); // Заповнити всю сцену

        // 1. Заголовок
        Label title = new Label("DROID BATTLE ARENA", skin);
        table.add(title).padBottom(50).row();

        // 2. Кнопка "Бій 1 на 1"
        TextButton oneVsOneButton = new TextButton("1 vs 1 Battle", skin);
        oneVsOneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Перехід до екрана вибору команди, встановлюємо режим 1 на 1
                game.setScreen(new TeamSelectionScreen(game, true));
            }
        });
        table.add(oneVsOneButton).width(300).height(50).pad(10).row();

        // 3. Кнопка "Команда на команду"
        TextButton teamButton = new TextButton("Team vs Team Battle", skin);
        teamButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Перехід до екрана вибору команди, режим Команда на Команду
                game.setScreen(new TeamSelectionScreen(game, false));
            }
        });
        table.add(teamButton).width(300).height(50).pad(10).row();

        // 4. Кнопка "Вихід"
        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Вихід з програми
            }
        });
        table.add(exitButton).width(300).height(50).pad(10).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Обробка вводу сценою
    }

    @Override
    public void render(float delta) {
        // Малювання фону
        game.batch.begin();
        game.batch.draw(game.arenaBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(Gdx.graphics.getDeltaTime()); // Оновлення сцени (кнопки, тощо)
        stage.draw(); // Малювання сцени
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Звільнення ресурсів
    }
}
