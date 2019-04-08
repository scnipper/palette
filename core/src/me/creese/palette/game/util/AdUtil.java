package me.creese.palette.game.util;

/**
 * Интерфейс для доступа к функциям платформы
 */
public interface AdUtil {
    /**
     * Загрузка картинки пользователем
     *
     * @param getImage Принимает файл картинки
     */
    void requestImagePath(GetImage getImage);

    /**
     * Показывет сообщение
     * @param text
     */
    void showToast(String text);

    /**
     * Диалог выхода в меню
     * @param afterOk
     */
    void showDialogExit(Runnable afterOk);
}
