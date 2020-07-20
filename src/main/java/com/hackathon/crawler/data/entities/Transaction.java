package com.hackathon.crawler.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column
    private String hash;

    @Column(name = "hash_in_base64")
    private String hashInBase64;

    @Column
    private LocalDateTime timestamp;

    @Column(name = "address_to")
    private String addressTo;

    @Column(name = "address_to_in_base58")
    private String addressToInBase58;

    @Column(name = "address_from")
    private String addressFrom;

    @Column(name = "address_from_in_base58")
    private String addressFromBase58;

    @Column
    private byte[] data;

    @Column
    private Integer amount;

    @OneToOne
    @JoinColumn(name = "status")
    private Transaction status;

    @Column
    private String sign;

    @Column(name = "sign_in_base64")
    private String signInBase64;

    @Column
    private Long fee;

    @ManyToOne
    @JoinColumn(name = "block_height")
    private Block block;

    @OneToOne
    @JoinColumn(name = "type")
    private Transaction transaction;

    @Column
    private String message;

    @Column(name = "message_in_base64")
    private String messageInBase64;

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

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getAddressToInBase58() {
        return addressToInBase58;
    }

    public void setAddressToInBase58(String addressToInBase58) {
        this.addressToInBase58 = addressToInBase58;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getAddressFromBase58() {
        return addressFromBase58;
    }

    public void setAddressFromBase58(String addressFromBase58) {
        this.addressFromBase58 = addressFromBase58;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Transaction getStatus() {
        return status;
    }

    public void setStatus(Transaction status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignInBase64() {
        return signInBase64;
    }

    public void setSignInBase64(String signInBase64) {
        this.signInBase64 = signInBase64;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageInBase64() {
        return messageInBase64;
    }

    public void setMessageInBase64(String messageInBase64) {
        this.messageInBase64 = messageInBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(hash, that.hash) &&
                Objects.equals(hashInBase64, that.hashInBase64) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(addressTo, that.addressTo) &&
                Objects.equals(addressToInBase58, that.addressToInBase58) &&
                Objects.equals(addressFrom, that.addressFrom) &&
                Objects.equals(addressFromBase58, that.addressFromBase58) &&
                Arrays.equals(data, that.data) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(status, that.status) &&
                Objects.equals(sign, that.sign) &&
                Objects.equals(signInBase64, that.signInBase64) &&
                Objects.equals(fee, that.fee) &&
                Objects.equals(block, that.block) &&
                Objects.equals(transaction, that.transaction) &&
                Objects.equals(message, that.message) &&
                Objects.equals(messageInBase64, that.messageInBase64);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(hash, hashInBase64, timestamp, addressTo, addressToInBase58, addressFrom, addressFromBase58, amount, status, sign, signInBase64, fee, block, transaction, message, messageInBase64);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
