package com.hackathon.crawler.data.entities;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Set;

@Entity
@Table(name = "accounts", schema = "likelib")
public class Account {

    @Id
    @Column(unique = true)
    @NonNull
    private String address;

    @Column
    @NonNull
    private Long balance;

    @Column
    @NonNull
    private BigInteger nonce;

    @Column
    @NonNull
    private String type;

    @OneToOne(mappedBy = "coinbase", fetch = FetchType.LAZY)
    private Block block;

    @Transient
    private Set<String> transactionsHashesInString;

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public Long getBalance() {
        return balance;
    }

    public void setBalance(@NonNull Long balance) {
        this.balance = balance;
    }

    @NonNull
    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(@NonNull BigInteger nonce) {
        this.nonce = nonce;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public Set<String> getTransactionsHashesInString() {
        return transactionsHashesInString;
    }

    public void setTransactionsHashesInString(Set<String> transactionsHashesInString) {
        this.transactionsHashesInString = transactionsHashesInString;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
