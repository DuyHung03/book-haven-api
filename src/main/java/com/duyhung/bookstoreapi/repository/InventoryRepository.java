package com.duyhung.bookstoreapi.repository;

import com.duyhung.bookstoreapi.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Optional<Inventory> findByBookBookId(String bookId);

}
