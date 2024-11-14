package com.visualmetronome;

import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.awt.*;

public class FullResizableVisualMetronomeOverlay extends Overlay
{
    private final Client client;
    private final VisualMetronomeConfig config;
    private final VisualMetronomePlugin plugin;

    private static int TITLE_PADDING = 10;
    private static final int MINIMUM_SIZE = 16;
    private static final int OFFSET_Y = 20;
    private static final int MOUSE_OFFSET_X = 1100;
    private static final int MOUSE_OFFSET_Y = 110;

    @Inject
    public FullResizableVisualMetronomeOverlay(Client client, VisualMetronomeConfig config, VisualMetronomePlugin plugin)
    {
        super(plugin);
        this.client = client;
        this.config = config;
        this.plugin = plugin;

        setMinimumSize(MINIMUM_SIZE);
        setResizable(true);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Dimension preferredSize = getPreferredSize();

        if (preferredSize == null)
        {
            preferredSize = plugin.DEFAULT_SIZE;
            setPreferredSize(preferredSize);
        }

        if (config.enableMetronome())
        {
            java.awt.Point location;
            if (config.renderToMouse())
            {
                setPosition(OverlayPosition.DYNAMIC);
                setLayer(OverlayLayer.ALWAYS_ON_TOP);
                setPriority(OverlayPriority.HIGH);

                java.awt.Point mouseCanvasPosition = client.getMouseCanvasPosition();
                if (mouseCanvasPosition.getX() < 0 || mouseCanvasPosition.getY() < 0)
                {
                    location = new java.awt.Point(preferredSize.width / 2, preferredSize.height / 2);
                }
                else
                {
                    location = convertToAwtPoint(mouseCanvasPosition);
                    location.x -= MOUSE_OFFSET_X;
                    location.y -= MOUSE_OFFSET_Y + OFFSET_Y;
                }
            }
            else
            {
                setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
                setLayer(OverlayLayer.UNDER_WIDGETS);
                setPriority(OverlayPriority.NONE);

                renderBoxAndText(graphics, preferredSize, 0, 0);
                return preferredSize;
            }

            renderBoxAndText(graphics, preferredSize, location.x - preferredSize.width / 2, location.y - preferredSize.height / 2);
        }

        return preferredSize;
    }

    private void renderBoxAndText(Graphics2D graphics, Dimension preferredSize, int x, int y)
    {
        graphics.setColor(plugin.currentColor);
        graphics.fillRect(x, y, preferredSize.width, preferredSize.height);
        TITLE_PADDING = (Math.min(preferredSize.width, preferredSize.height) / 2 - 4);

        if (config.showTick())
        {
            int textX = x + preferredSize.width / 3;
            int textY = y + preferredSize.height;
            graphics.setColor(config.NumberColor());

            if (config.disableFontScaling())
            {
                if (config.tickCount() == 1)
                {
                    graphics.drawString(String.valueOf(plugin.currentColorIndex), x + TITLE_PADDING, y + preferredSize.height - TITLE_PADDING);
                }
                else
                {
                    graphics.drawString(String.valueOf(plugin.tickCounter), x + TITLE_PADDING, y + preferredSize.height - TITLE_PADDING);
                }
            }
            else
            {
                if (config.fontType() == FontTypes.REGULAR)
                {
                    graphics.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.PLAIN, Math.min(preferredSize.width, preferredSize.height)));
                }
                else
                {
                    graphics.setFont(new Font(config.fontType().toString(), Font.PLAIN, Math.min(preferredSize.width, Math.min(preferredSize.width, preferredSize.height))));
                }

                if (config.tickCount() == 1)
                {
                    OverlayUtil.renderTextLocation(graphics, new java.awt.Point(textX, textY), String.valueOf(plugin.currentColorIndex), config.NumberColor());
                }
                else
                {
                    OverlayUtil.renderTextLocation(graphics, new java.awt.Point(textX, textY), String.valueOf(plugin.tickCounter), config.NumberColor());
                }
            }
        }
    }

    private java.awt.Point convertToAwtPoint(java.awt.Point point)
    {
        return new java.awt.Point(point.getX(), point.getY());
    }

    private java.awt.Point convertToRuneLitePoint(java.awt.Point point)
    {
        return new Point(point.x, point.y);
    }
}
