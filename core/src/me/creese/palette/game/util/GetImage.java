package me.creese.palette.game.util;


import java.io.File;

public interface GetImage {
    /**
     * Вызывается когда получен файл изображения от пользователя
     * @param path Файл с изображением. Может быть null
     */
    void loadImage(File path);
}
