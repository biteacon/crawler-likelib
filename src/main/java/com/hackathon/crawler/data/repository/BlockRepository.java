package com.hackathon.crawler.data.repository;

import com.hackathon.crawler.data.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, String> {

    @Query("SELECT max(b.height) FROM Block b")
    Long getTopBlockNumber();

    Boolean existsByHeight(Long height);


}
