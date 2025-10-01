package com.waiyannaung.sku.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.waiyannaung.sku.model.domain.Article;

@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {
}