package com.itson.profeco.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteStoresRequest {
    @NotNull(message = "Favorite store IDs list cannot be null, but can be empty.")
    private Set<UUID> favoriteStoreIds;
}
