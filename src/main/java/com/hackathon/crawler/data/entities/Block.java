package com.hackathon.crawler.data.entities;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "blocks", schema = "likelib")
public class Block {

    @Id
    @Column
    @NonNull
    private Long height;

    @OneToOne
    @JoinColumn(name = "coinbase")
    @NonNull
    private Account coinbase;
    @Transient
    private String coinbaseString;

    @Column
    @NonNull
    private String hash;

    @Column
    @NonNull
    private LocalDateTime timestamp;

    @Column
    @NonNull
    private BigInteger nonce;

    @Column(name = "prev_block_hash")
    @NonNull
    private String prevBlockHash;

    @OneToMany(mappedBy = "block", fetch = FetchType.LAZY)
    private Set<Transaction> transactionList;
    @Transient
    private String transactionsInJSON;

    @NonNull
    public Long getHeight() {
        return height;
    }

    public void setHeight(@NonNull Long height) {
        this.height = height;
    }

    @NonNull
    public Account getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(@NonNull Account coinbase) {
        this.coinbase = coinbase;
    }

    @NonNull
    public String getHash() {
        return hash;
    }

    public void setHash(@NonNull String hash) {
        this.hash = hash;
    }

    @NonNull
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(@NonNull BigInteger nonce) {
        this.nonce = nonce;
    }

    @NonNull
    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setPrevBlockHash(@NonNull String prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public Set<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(Set<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public String getTransactionsInJSON() {
        return transactionsInJSON;
    }

    public void setTransactionsInJSON(String transactionsInJSON) {
        this.transactionsInJSON = transactionsInJSON;
    }

    public String getCoinbaseString() {
        return coinbaseString;
    }

    public void setCoinbaseString(String coinbaseString) {
        this.coinbaseString = coinbaseString;
    }
}
