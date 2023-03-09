package com.visualmetronome;

import com.google.inject.Provides;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;

@PluginDescriptor(
        name = "Visual Metronome",
        description = "Shows a visual cue on an overlay every game tick to help timing based activities",
        tags = {"timers", "overlays", "tick", "skilling"}
)
public class VisualMetronomePlugin extends Plugin implements KeyListener
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private VisualMetronomeTileOverlay tileOverlay;

    @Inject
    private FullResizableVisualMetronomeOverlay overlay;

    @Inject
    private VisualMetronomeConfig config;

    @Inject
    private KeyManager keyManager;

    private int currentColorIndex = 0;
    protected int tickCounter = 0;
    protected Color currentColor = Color.WHITE;

    protected Dimension DEFAULT_SIZE = new Dimension(25, 25);

    @Provides
    VisualMetronomeConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(VisualMetronomeConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (config.tickCount() != 1)
        {
            if (tickCounter % config.tickCount() == 0)
            {
                tickCounter = 0;
                if (currentColorIndex == config.colorCycle())
                {
                    currentColorIndex = 0;
                }
                switch (++currentColorIndex)
                {
                    case 1:
                        currentColor = config.getTickColor();
                        break;
                    case 2:
                        currentColor = config.getTockColor();
                        break;
                    case 3:
                        currentColor = config.getTick3Color();
                        break;
                    case 4:
                        currentColor = config.getTick4Color();
                        break;
                    case 5:
                        currentColor = config.getTick5Color();
                        break;
                    case 6:
                        currentColor = config.getTick6Color();
                        break;
                    case 7:
                        currentColor = config.getTick7Color();
                        break;
                    case 8:
                        currentColor = config.getTick8Color();
                        break;
                    case 9:
                        currentColor = config.getTick9Color();
                        break;
                    case 10:
                        currentColor = config.getTick10Color();
                }
            }
            tickCounter++;
            return;
        }
        // changes color every tick
        if (tickCounter == config.colorCycle())
        {
            tickCounter = 0;
        }
        switch (++tickCounter)
        {
            case 1:
                currentColor = config.getTickColor();
                break;
            case 2:
                currentColor = config.getTockColor();
                break;
            case 3:
                currentColor = config.getTick3Color();
                break;
            case 4:
                currentColor = config.getTick4Color();
                break;
            case 5:
                currentColor = config.getTick5Color();
                break;
            case 6:
                currentColor = config.getTick6Color();
                break;
            case 7:
                currentColor = config.getTick7Color();
                break;
            case 8:
                currentColor = config.getTick8Color();
                break;
            case 9:
                currentColor = config.getTick9Color();
                break;
            case 10:
                currentColor = config.getTick10Color();
        }

    }
    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (!event.getGroup().equals("visualmetronome"))
        {
            return;
        }

        if (config.tickCount() == 1)
        {
            if (tickCounter > config.colorCycle())
            {
                tickCounter = 0;
            }
        }
        else
        {
            if (currentColorIndex > config.colorCycle())
            {
                currentColorIndex = 0;
            }
            if (tickCounter > config.tickCount())
            {
                tickCounter = 0;
            }
        }
        DEFAULT_SIZE = new Dimension(config.boxWidth(), config.boxWidth());
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
        overlay.setPreferredSize(DEFAULT_SIZE);
        overlayManager.add(tileOverlay);
        keyManager.registerKeyListener(this);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        overlayManager.remove(tileOverlay);
        tickCounter = 0;
        currentColorIndex = 0;
        currentColor = config.getTickColor();
        keyManager.unregisterKeyListener(this);
    }

    //hotkey settings
    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (config.tickResetHotkey().matches(e))
        {
            tickCounter = 0;
            currentColorIndex = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }
}
