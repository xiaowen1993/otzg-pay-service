package com.bcb.pay.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 支付渠道设置
 *
 */
@Entity
@Table(name = "pay_channel")
public class PayChannel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //渠道名称
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    //数据格式
    @Column(name = "data")
    private String data;

    //修改时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false, length = 19)
    private Date updateTime;

    @Column(name = "type", nullable = false,length = 2)
    private int type=0;

    @Column(name = "sort", nullable = false,length = 2)
    private int sort=0;

    @Column(name = "status", nullable = false,length = 2)
    private int status=0;


    public PayChannel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
