package red.jackf.jsst.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import red.jackf.jsst.features.Feature;

import java.util.Arrays;
import java.util.stream.Collectors;

import static net.minecraft.commands.Commands.literal;

public class JSSTCommand {
    public static void register(Feature<?>[] features) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            var root = literal(("jsst"));

            for (Feature<?> feature : features) {
                var node = literal(feature.id());

                OptionBuilders.addEnabled(node, feature);

                feature.setupCommand(node);

                root.then(node);
            }

            root.executes(ctx -> {
                var map = Arrays.stream(features).collect(Collectors.partitioningBy(Feature::isEnabled));
                var enabled = map.get(true);
                var disabled = map.get(false);
                if (enabled.size() > 0) {
                    var str = Component.literal("[+] ").withStyle(ChatFormatting.DARK_GREEN);
                    for (int i = 0; i < enabled.size(); i++) {
                        if (i > 0) str.append(Component.literal(", ").withStyle(ChatFormatting.GREEN));
                        str.append(Component.literal(enabled.get(i).id()).withStyle(ChatFormatting.WHITE));
                    }
                    ctx.getSource().sendSuccess(str, false);
                }
                if (disabled.size() > 0) {
                    var str = Component.literal("[x] ").withStyle(ChatFormatting.DARK_RED);
                    for (int i = 0; i < disabled.size(); i++) {
                        if (i > 0) str.append(Component.literal(", ").withStyle(ChatFormatting.RED));
                        str.append(Component.literal(disabled.get(i).id()).withStyle(ChatFormatting.WHITE));
                    }
                    ctx.getSource().sendSuccess(str, false);
                }
                return 1;
            });

            dispatcher.register(root);
        });
    }
}
