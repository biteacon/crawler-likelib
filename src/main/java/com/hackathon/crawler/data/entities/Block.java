package com.hackathon.crawler.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "blocks")
public class Block {

    @Id
    @Column
    private Long number;

    @Column
    private String hash;

    @Column(name = "hash_in_base64")
    private String hashInBase64;

    @Column
    private LocalDateTime timestamp;

    @Column
    private Integer nonce;

    @Column(name = "prev_block_hash")
    private String prevBlockHash;

    @Column(name = "prev_block_hash_in_base64")
    private String prevBlockHashInBase64;

    @OneToMany(mappedBy = "block", fetch = FetchType.LAZY)
    private List<Transaction> transactionList;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashInBase64() {
        return hashInBase64;
    }

    public void setHashInBase64(String hashInBase64) {
        this.hashInBase64 = hashInBase64;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setPrevBlockHash(String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public String getPrevBlockHashInBase64() {
        return prevBlockHashInBase64;
    }

    public void setPrevBlockHashInBase64(String prevBlockHashInBase64) {
        this.prevBlockHashInBase64 = prevBlockHashInBase64;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(number, block.number) &&
                Objects.equals(hash, block.hash) &&
                Objects.equals(hashInBase64, block.hashInBase64) &&
                Objects.equals(timestamp, block.timestamp) &&
                Objects.equals(nonce, block.nonce) &&
                Objects.equals(prevBlockHash, block.prevBlockHash) &&
                Objects.equals(prevBlockHashInBase64, block.prevBlockHashInBase64) &&
                Objects.equals(transactionList, block.transactionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, hash, hashInBase64, timestamp, nonce, prevBlockHash, prevBlockHashInBase64, transactionList);
    }
}
