package com.hackathon.crawler.data.repository;

import com.hackathon.crawler.data.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("SELECT max(b.number) FROM Block b")
    Long getMaxValueOfNumber();

}
