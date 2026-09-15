package dev.raphaellee.altechwalletbackend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Player implements Persistable<String> {
    @Id @NonNull
    private String username;

    @Transient
    private boolean isNew = true;

    @Nullable
    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
