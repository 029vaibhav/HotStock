package com.niyo.hotstock.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Owner {

    @NotNull
    String userId;
    long qty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Owner owner = (Owner) o;

        return getUserId().equals(owner.getUserId());
    }

    @Override
    public int hashCode() {
        return getUserId().hashCode();
    }
}
