package org.imsouhay.Grove.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.imsouhay.Grove.util.DexRange;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class DexArgumentType implements ArgumentType<DexRange> {

    @Override
    public DexRange parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.getRemaining(); // Read the entire input
        return parseDexRange(input);
    }

    private DexRange parseDexRange(String input) throws CommandSyntaxException {
        try {
            if (input.startsWith(">")) {
                int minValue = Integer.parseInt(input.substring(1));
                return new DexRange(minValue, DexRange.ABSOLUTE_MAX);
            } else if (input.startsWith("<")) {
                int maxValue = Integer.parseInt(input.substring(1));
                return new DexRange(DexRange.ABSOLUTE_MIN, maxValue);
            } else {
                int exactValue = Integer.parseInt(input);
                return new DexRange(exactValue);
            }
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException(new SimpleCommandExceptionType(
                    new LiteralMessage("Invalid number format for DexRange")),
                    new LiteralMessage("Invalid number format for DexRange. \nError Message: "+e.getMessage())
            );
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        // Provide suggestions if needed (e.g., for autocompletion)
        return ArgumentType.super.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        // Provide example usage for this argument type
        return ArgumentType.super.getExamples();
    }
}