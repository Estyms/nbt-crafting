package de.siphalor.nbtcrafting.dollar.operator;

import java.util.function.Function;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.siphalor.nbtcrafting.api.nbt.NbtUtil;
import de.siphalor.nbtcrafting.dollar.DollarEvaluationException;
import de.siphalor.nbtcrafting.dollar.token.DollarToken;

public class ChildOperator implements BinaryOperator {
	private final int precedence;
	private final DollarToken.Type tokenType;

	public ChildOperator(int precedence, DollarToken.Type tokenType) {
		this.precedence = precedence;
		this.tokenType = tokenType;
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

	@Override
	public DollarToken.@NotNull Type getTokenType() {
		return tokenType;
	}

	@Override
	public @Nullable Object apply(@Nullable Object left, @Nullable Object right, @NotNull Function<String, Object> referenceResolver) throws DollarEvaluationException {
		assertNotNull(left, 0);
		left = tryResolveReference(left, referenceResolver);
		if (left instanceof CompoundTag) {
			String key = assertStringOrLiteral(right, 1);
			return NbtUtil.toDollarValue(((CompoundTag) left).get(key));
		} else if (left instanceof ListTag) {
			Number index = assertParameterType(right, 1, Number.class);
			return NbtUtil.toDollarValue(((ListTag) left).get(index.intValue()));
		}
		exceptParameterType(left, 0, CompoundTag.class, ListTag.class);
		return null;
	}
}
