package com.droids.game.screen;

import com.badlogic.gdx.Screen;
import com.droids.game.MainGame;

// Базовий абстрактний клас для всіх екранів гри
public abstract class BaseScreen implements Screen {
    protected final MainGame game; // Посилання на головний клас гри

    // Конструктор
    public BaseScreen(MainGame game) {
        this.game = game;
    }

    // Методи інтерфейсу Screen, які є обов'язковими, але реалізація
    // залишається порожньою для більшості базових функцій.

    @Override
    public void render(float delta) {} // Основний цикл оновлення/малювання

    @Override
    public void resize(int width, int height) {} // Обробка зміни розміру вікна

    @Override
    public void pause() {} // Пауза гри

    @Override
    public void resume() {} // Відновлення гри

    @Override
    public void show() {} // Викликається при встановленні екрана (видалено у вашому коді, але я додав його)

    @Override
    public void hide() {} // Викликається, коли екран приховано

    @Override
    public void dispose() {} // Звільнення ресурсів
}
