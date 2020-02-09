package io.github.rjhaytree.startstopcommands;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Plugin(
        id = "startstopcommands",
        name = "StartStopCommands",
        description = "A simple plugin to run commands on server start and stop.",
        authors = {
                "RyanJH"
        },
        version = "1.0"
)
public class StartStopCommands {
    private ConfigurationNode config;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    private static StartStopCommands instance;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        loadConfig();
        Sponge.getEventManager().registerListeners(this, new EventHandlers(this));
    }

    /**
     * Create or Load configuration.
     */
    private void loadConfig() {
        try {
            if (!Files.exists(defaultConfig.toPath())) {
                Sponge.getAssetManager().getAsset(this, "startstopcommands.conf").get().copyToFile(defaultConfig.toPath());
            }

            config = loader.load();
            logger.info("Configuration loaded successfully.");
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.warn("Configuration file could not be loaded.");
        }
    }

    public ConfigurationNode getConfig() {
        return config;
    }
}
