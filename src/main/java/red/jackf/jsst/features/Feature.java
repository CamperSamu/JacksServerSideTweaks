package red.jackf.jsst.features;

import blue.endless.jankson.Comment;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import red.jackf.jsst.command.OptionBuilders;
import red.jackf.jsst.config.JSSTConfig;

import static red.jackf.jsst.command.CommandUtils.errorPrefix;
import static red.jackf.jsst.command.CommandUtils.text;

public abstract class Feature<C extends Feature.Config> {
    /**
     * Called to set up hooks and events for this feature.
     */
    public abstract void init();

    /**
     * Identifier for this feature, used in commands. Should be <code>camelCaseOnly</code>.
     * @return Feature Identifier
     */
    public abstract String id();

    public String commandLabel() {
        return id();
    }

    /**
     * Returns this feature's current config from {@link JSSTConfig.Handler#get()}.
     * @return This feature's current config.
     */
    public abstract C getConfig();

    /**
     * Called when this feature is enabled.
     */
    public void onEnabled() {}

    /**
     * Called when this feature is disabled.
     */
    public void onDisabled() {}

    /**
     * Add any nodes to this feature's command here. Helpers for config options are available in {@link OptionBuilders}.
     * @param node this feature's root node.
     */
    public void setupCommand(LiteralArgumentBuilder<CommandSourceStack> node) {}

    /**
     * Utility method for aborting commands if not enabled
     * @throws CommandRuntimeException Thrown if this feature is not enabled.
     */
    public final void assertEnabled() throws CommandRuntimeException {
        if (!getConfig().enabled) throw new CommandRuntimeException(errorPrefix().append(text("Feature " + id() + " not enabled!")));
    }

    public static abstract class Config {
        @Comment("Is this feature enabled? (Default: true, Options: true, false)")
        public boolean enabled = true;
    }
}
