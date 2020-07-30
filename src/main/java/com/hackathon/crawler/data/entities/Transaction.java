package com.hackathon.crawler.data.entities;

import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions", schema = "likelib")
public class Transaction implements Serializable {

    @Id
    @Column
    @NonNull
    private String hash;

    @Column
    @NonNull
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to")
    @NonNull
    private Account accountTo;
    @Transient
    private String accountToString;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from")
    @NonNull
    private Account accountFrom;
    @Transient
    private String accountFromString;

    @Column
    private String data;

    @Column
    @NonNull
    private Long amount;

    @Column
    @NonNull
    private String sign;

    @Column
    @NonNull
    private Long fee;

    @ManyToOne
    @JoinColumn(name = "block_height")
    @NonNull
    private Block block;

    @Column
    @NonNull
    private String type;

    @Column
    @NonNull
    private String status;

    @Column
    private String message;

    @Column(name = "fee_left")
    private Long feeLeft;

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
    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(@NonNull Account accountTo) {
        this.accountTo = accountTo;
    }

    @NonNull
    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(@NonNull Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @NonNull
    public Long getAmount() {
        return amount;
    }

    public void setAmount(@NonNull Long amount) {
        this.amount = amount;
    }

    @NonNull
    public String getSign() {
        return sign;
    }

    public void setSign(@NonNull String sign) {
        this.sign = sign;
    }

    @NonNull
    public Long getFee() {
        return fee;
    }

    public void setFee(@NonNull Long fee) {
        this.fee = fee;
    }

    @NonNull
    public Block getBlock() {
        return block;
    }

    public void setBlock(@NonNull Block block) {
        this.block = block;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccountToString() {
        return accountToString;
    }

    public void setAccountToString(String accountToString) {
        this.accountToString = accountToString;
    }

    public String getAccountFromString() {
        return accountFromString;
    }

    public void setAccountFromString(String accountFromString) {
        this.accountFromString = accountFromString;
    }

    public Long getFeeLeft() {
        return feeLeft;
    }

    public void setFeeLeft(Long feeLeft) {
        this.feeLeft = feeLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return timestamp.equals(that.timestamp) &&
                Objects.equals(accountToString, that.accountToString) &&
                Objects.equals(accountFromString, that.accountFromString) &&
                amount.equals(that.amount) &&
                sign.equals(that.sign) &&
                fee.equals(that.fee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, accountToString, accountFromString, amount, sign, fee);
    }
}
