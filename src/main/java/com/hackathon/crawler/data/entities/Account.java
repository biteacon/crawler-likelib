package com.hackathon.crawler.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "address")
    private String address;

    @Column(name = "address_in_base58")
    private String addressInBase58;

    @Column
    private Long balance;

    @Column
    private Integer nonce;


    private String type;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressInBase58() {
        return addressInBase58;
    }

    public void setAddressInBase58(String addressInBase58) {
        this.addressInBase58 = addressInBase58;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(address, account.address) &&
                Objects.equals(addressInBase58, account.addressInBase58) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(nonce, account.nonce) &&
                Objects.equals(type, account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, addressInBase58, balance, nonce, type);
    }
}
