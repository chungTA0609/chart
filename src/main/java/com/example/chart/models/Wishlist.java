package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(indexes = {
        @Index(name = "idx_wishlist_user_id", columnList = "user_id")
})
@Data
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> items;

    @Version
    private Long version;
}
