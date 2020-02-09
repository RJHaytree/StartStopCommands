package io.github.rjhaytree.startstopcommands;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventHandlers {
    private StartStopCommands instance;
    private List<String> commandsOnServerStarted;
    private List<String> commandsOnServerStopping;

    public EventHandlers(StartStopCommands instance) {
        this.instance = instance;

        try {
            commandsOnServerStarted = instance.getConfig().getNode("onServerStarted").getList(TypeToken.of(String.class));
            commandsOnServerStopping = instance.getConfig().getNode("onServerStopping").getList(TypeToken.of(String.class));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onServerStarted(GameStartedServerEvent event) {
        Sponge.getScheduler().createTaskBuilder()
                .execute(this::sendCommandsOnTimer)
                .delay(20, TimeUnit.MILLISECONDS)
                .submit(instance);
    }

    /**
     * Execute start commands 20 milliseconds after the server is loaded
     */
    private void sendCommandsOnTimer() {
        for (String cmd: commandsOnServerStarted) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd);
        }
    }

    @Listener
    public void onServerStopping(GameStoppingEvent event) {
        for (String cmd: commandsOnServerStopping) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd);
        }
    }
}
