package com.visualmetronome;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Font;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPriority;

public class VisualMetronomeNumberOverlay extends Overlay
{
    private final Client client;
    private final VisualMetronomeConfig config;
    private final VisualMetronomePlugin plugin;

    @Inject
    public VisualMetronomeNumberOverlay(Client client, VisualMetronomeConfig config, VisualMetronomePlugin plugin)
    {
        super(plugin);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPriority(OverlayPriority.MED);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (config.showPlayerTick())
        {
            if (config.fontType() == FontTypes.REGULAR)
            {
                graphics.setFont(new Font(FontManager.getRunescapeFont().getName(), Font.PLAIN, config.fontSize()));
            }
            else
            {
                graphics.setFont(new Font(config.fontType().toString(), Font.PLAIN, config.fontSize()));
            }

            final int height = client.getLocalPlayer().getLogicalHeight() + 20;
            final LocalPoint localLocation = client.getLocalPlayer().getLocalLocation();
            final Point playerPoint = Perspective.localToCanvas(client, localLocation, client.getPlane(), height);

            if (config.tickCount() == 1)
            {
                OverlayUtil.renderTextLocation(graphics, playerPoint, String.valueOf(plugin.currentColorIndex), config.NumberColor());
            }
            else
            {
                OverlayUtil.renderTextLocation(graphics, playerPoint, String.valueOf(plugin.tickCounter), config.NumberColor());
            }
        }

        return null;
    }

    private java.awt.Point convertToAwtPoint(net.runelite.api.Point point)
    {
        return new java.awt.Point(point.getX(), point.getY());
    }

    private net.runelite.api.Point convertToRuneLitePoint(java.awt.Point point)
    {
        return new net.runelite.api.Point(point.x, point.y);
    }
}
