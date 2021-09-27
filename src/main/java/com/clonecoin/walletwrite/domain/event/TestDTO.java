package com.clonecoin.walletwrite.domain.event;

import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Getter
public class TestDTO {
    private Long userId;

    public TestDTO() {
    }

    public TestDTO(Long userId){
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDTO that = (TestDTO) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
