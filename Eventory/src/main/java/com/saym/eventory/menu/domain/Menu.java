package com.saym.eventory.menu.domain;

import com.saym.eventory.shop.domain.Shop;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "menu_price")
    private Long menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Builder
    public Menu(String menuName, Long menuPrice, Shop shop) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.shop = shop;
    }
    public void assignShop(Shop shop) {
        this.shop = shop;
    }

}




