package com.matchmeeat.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    public String toString() {
        return "BaseEntity(id=" + id + ")";
    }

    // Not overrideable, delomboked implementation
    public final boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BaseEntity other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        return Objects.equals(this$id, other$id);
    }

    // Not overrideable, delomboked implementation
    public final int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        return result;
    }

    // Not overrideable, delomboked implementation
    protected final boolean canEqual(Object other) {
        return other instanceof BaseEntity;
    }
}
