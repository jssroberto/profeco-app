package com.itson.profeco.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteStoresResponse {
    private Set<UUID> favoriteStoreIds;
}
