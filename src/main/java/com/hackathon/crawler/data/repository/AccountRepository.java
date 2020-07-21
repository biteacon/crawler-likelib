package com.hackathon.crawler.data.repository;

import com.hackathon.crawler.data.entities.Account;
import com.hackathon.crawler.data.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Boolean existsByAddress(String address);

}
