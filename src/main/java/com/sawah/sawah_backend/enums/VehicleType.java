package com.sawah.sawah_backend.enums;

import lombok.Getter;

@Getter
public enum VehicleType {
    SEDAN(4),
    SUV(6),
    VAN(8),
    MICROBUS(12);

    private final int capacity;

    VehicleType(int capacity) {
        this.capacity = capacity;
    }

}
