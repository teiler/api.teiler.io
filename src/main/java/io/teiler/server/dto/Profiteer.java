/**
 * MIT License
 *
 * Copyright (c) 2017 L. Röllin, P. Bächli, K. Thurairatnam & D. Thoma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.teiler.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

/**
 * Represents the share a Person takes in a Transaction.
 *
 * @author pbaechli
 */
public class Profiteer {

    private Integer transactionId;

    @Expose
    @SerializedName("person")
    private Person person;

    @Expose
    @SerializedName("share")
    private Integer share;

    @Expose(deserialize = false)
    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @Expose(deserialize = false)
    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * Usual constructor.
     *
     * @param transactionId Id of the Transaction in which share is being taken
     * @param person Person taking share in the Transaction
     * @param share Amount of money by which share is being taken
     */
    public Profiteer(Integer transactionId, Person person, Integer share) {
        this.transactionId = transactionId;
        this.person = person;
        this.share = share;
    }

    /**
     * Constructor with all fields (useful for converting).
     *
     * @param transactionId Id of the Transaction in which share is being taken
     * @param person Person taking share in the Transaction
     * @param share Amount of money by which share is being taken
     * @param updateTime {@link LocalDateTime} marking the last update of the Share
     * @param createTime {@link LocalDateTime} marking the creation of the Share
     */
    public Profiteer(Integer transactionId, Person person, Integer share, LocalDateTime updateTime,
        LocalDateTime createTime) {
        this.transactionId = transactionId;
        this.person = person;
        this.share = share;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Profiteer [person=" + person + ", share=" + share + ", updateTime=" + updateTime
            + ", createTime=" + createTime + ", transactionId(transient)=" + transactionId + "]";
    }

}
