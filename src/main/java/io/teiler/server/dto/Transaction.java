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
 * Class holding all information about a transaction.
 *
 * @author pbaechli
 */
public abstract class Transaction {

    @Expose
    @SerializedName("id")
    private Integer id;

    @Expose
    @SerializedName("amount")
    private Integer amount;

    @Expose
    @SerializedName("payer")
    private Person payer;

    @Expose(deserialize = false)
    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @Expose(deserialize = false)
    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * @param id Id of the Transaction.
     *
     * @param amount Amount of money involved in the transaction
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Transaction
     * @param createTime {@link LocalDateTime} marking the creation of the Transaction
     */
    public Transaction(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
        LocalDateTime createTime) {
        this.id = id;
        this.amount = amount;
        this.payer = payer;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
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
        return "Transaction [id=" + id + ", amount=" + amount + ", payer=" + payer + ", updateTime="
            + updateTime + ", createTime=" + createTime + "]";
    }

}
