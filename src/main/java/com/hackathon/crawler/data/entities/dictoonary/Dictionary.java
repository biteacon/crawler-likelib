package com.hackathon.crawler.data.entities.dictoonary;

import com.hackathon.crawler.data.entities.Transaction;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.Objects;

@MappedSuperclass
public abstract class Dictionary {

    @Id
    @Column(name = "key")
    private Long key;
    @Column(name = "value")
    private String value;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Transaction transaction;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value) &&
                Objects.equals(transaction, that.transaction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, transaction);
    }
}
