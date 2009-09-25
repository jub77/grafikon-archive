package net.parostroj.timetable.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

/**
 * Implementation of splash screen info for splash screen from java 1.6.
 * 
 * @author jub
 */
public class SplashScreenInfoOrig implements SplashScreenInfo {

    private int x,  y;
    private SplashScreen splash;

    public SplashScreenInfoOrig(SplashScreen splash, int x, int y) {
        this.splash = splash;
        this.x = x;
        this.y = y;
    }

    @Override
    public void setText(String text) {
        Graphics2D g = (Graphics2D) splash.createGraphics();
        Dimension size = splash.getSize();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, size.width, size.height);
        g.setPaintMode();
        g.setFont(g.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));
        g.setColor(Color.BLACK);
        g.drawString(text, x, y);
        splash.update();
    }

    @Override
    public void setProgress(int progress) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
