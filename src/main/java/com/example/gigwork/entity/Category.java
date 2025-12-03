package com.example.gigwork.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 업직종 카테고리 엔티티
 * DB 테이블: category
 * 연관 테이블: 없음 (독립 테이블)
 */
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "cd", length = 20, nullable = false)
    private String cd;

    @Column(name = "nm", nullable = false)
    private String nm;

    @Column(name = "kind", length = 10, nullable = false)
    private String kind;

    @Column(name = "depth", nullable = false)
    private Integer depth;

    @Column(name = "seq", nullable = true)
    private String seq;

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}


