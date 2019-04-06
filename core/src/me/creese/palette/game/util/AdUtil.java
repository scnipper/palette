package me.creese.palette.game.util;


public interface AdUtil {
    void requestImagePath(GetImage getImage);
    void showToast(String text);
    void showDialogExit(Runnable afterOk);
}
