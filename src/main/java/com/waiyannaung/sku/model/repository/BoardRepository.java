package com.waiyannaung.sku.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.waiyannaung.sku.model.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // List<Article> findAll();
}