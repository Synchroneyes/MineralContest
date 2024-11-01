package fr.synchroneyes.custom_events;

import fr.synchroneyes.mineral.Utils.MassBlockSpawner;
import lombok.Getter;
import org.checkerframework.checker.units.qual.Mass;

public class MCMassBlockSpawnEndedEvent extends MCEvent{

    @Getter
    private MassBlockSpawner spawner;

    public MCMassBlockSpawnEndedEvent(MassBlockSpawner spawner) {
        this.spawner = spawner;
    }
}
