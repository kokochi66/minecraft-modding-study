package net.kokochi.tutorialmod.thirst;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerThirstProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    /*
    * Capability 시스템 사용 방법:
        Capability 정의: 게임 오브젝트에 부여할 추가적인 데이터나 기능을 정의합니다. 여기서는 PlayerThirst 클래스가 이 역할을 합니다.
        Capability 제공자: PlayerThirstProvider는 ICapabilityProvider 인터페이스를 구현하여, 플레이어 엔티티가 PlayerThirst Capability를 가질 수 있도록 합니다.
        데이터 저장 및 로드: INBTSerializable<CompoundTag> 인터페이스를 구현하여, 플레이어의 목마름 상태를 NBT 데이터로 저장하고 로드할 수 있습니다. 이를 통해 게임을 저장하고 로드할 때 플레이어의 목마름 상태를 유지할 수 있습니다.

        NBT 사용 방법:
        NBT 저장: 게임 오브젝트의 상태를 CompoundTag 객체에 저장합니다. 이 객체는 다양한 타입의 데이터(예: 숫자, 문자열, 리스트, 다른 컴파운드 태그 등)를 저장할 수 있는 구조입니다.
        NBT 로드: 저장된 CompoundTag로부터 데이터를 읽어와 게임 오브젝트의 상태를 복원합니다.
    * */


    // PLAYER_THIRST는 플레이어의 목마름 상태를 나타내는 Capability입니다.
    public static Capability<PlayerThirst> PLAYER_THIRST = CapabilityManager.get(new CapabilityToken<PlayerThirst>() {});

    // 플레이어의 목마름 상태를 저장하는 필드입니다.
    private PlayerThirst thirst = null;
    // Capability 인스턴스를 지연 초기화하기 위한 LazyOptional입니다. 실제 사용 시점에 createPlayerThirst 메소드를 호출하여 초기화합니다.
    private final LazyOptional<PlayerThirst> optional = LazyOptional.of(this::createPlayerThirst);

    // 실제 PlayerThirst 인스턴스를 생성하는 메소드입니다. 이미 생성되었다면 기존 인스턴스를 반환합니다.
    private PlayerThirst createPlayerThirst() {
        if (this.thirst == null) {
            this.thirst = new PlayerThirst();
        }
        return this.thirst;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // 요청된 Capability가 PLAYER_THIRST와 일치하면, 해당 Capability의 LazyOptional 인스턴스를 반환합니다.
        if (cap == PLAYER_THIRST) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        // 플레이어의 목마름 상태를 NBT 데이터로 직렬화하는 메소드입니다.
        CompoundTag nbt = new CompoundTag();
        createPlayerThirst().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // NBT 데이터로부터 플레이어의 목마름 상태를 역직렬화하여 로드하는 메소드입니다.
        createPlayerThirst().loadNBTData(nbt);
    }
}

