package me.eliq.troot.block;

import me.eliq.troot.Trooot;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class BuzzerBlock extends Block {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final int REPEAT_TICKS = 30;

    public BuzzerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        boolean flag = state.getValue(POWERED);
        if (flag != level.hasNeighborSignal(pos)) {
            if (!flag) {
                playSound(level, pos);
                level.scheduleTick(pos, this, REPEAT_TICKS);
            }
            level.setBlock(pos, state.cycle(POWERED), 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            playSound(level, pos);
            level.scheduleTick(pos, this, REPEAT_TICKS);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    void playSound(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }
        level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(),
        (SoundEvent) Trooot.INDUSTRIAL_BUZZER_SOUND.get(), SoundSource.BLOCKS, 6.0F, 1.0F, 0L);
    }
}
