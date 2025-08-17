package com.saym.eventory.store.domain;

import com.saym.eventory.store.domain.Store;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String menuName;

    private int price;

    private boolean isSignature; // 시그니처 메뉴 여부

    @Builder
    public Menu(Store store, String menuName, int price, boolean isSignature) {
        this.store = store;
        this.menuName = menuName;
        this.price = price;
        this.isSignature = isSignature;
    }

    public void updateMenu(String menuName, int price, boolean isSignature) {
        this.menuName = menuName;
        this.price = price;
        this.isSignature = isSignature;
    }
}