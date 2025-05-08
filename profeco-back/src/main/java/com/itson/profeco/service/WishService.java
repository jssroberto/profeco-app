package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Wish;
import com.itson.profeco.repository.WishRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;

    public List<Wish> getAllWishes() {
        return wishRepository.findAll();
    }

    public Wish getWishById(UUID id) {
        return wishRepository.findById(id).orElse(null);
    }

    public Wish saveWish(Wish wish) {
        return wishRepository.save(wish);
    }

    public Wish updateWish(UUID id, Wish updatedWish) {
        if (!wishRepository.existsById(id)) {
            return null;
        }
        updatedWish.setId(id);
        return wishRepository.save(updatedWish);
    }

    public void deleteWish(UUID id) {
        wishRepository.deleteById(id);
    }
}
