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
import java.util.List;

/**
 * Class holding all information about an expense.
 *
 * @author pbaechli
 */
public class Expense extends Transaction {

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("profiteers")
    private List<Profiteer> profiteers;

    /**
     * Usual constructor.
     *
     * @param id Id of the Expense
     * @param amount Amount of money involved in the Expense
     * @param payer Person who spent the money
     * @param title Title of the Expense
     * @param profiteers {@link List} of {@link Profiteer} related to the Expense
     */
    public Expense(Integer id, Integer amount, Person payer, String title,
        List<Profiteer> profiteers) {
        super(id, amount, payer, null, null);
        this.title = title;
        this.profiteers = profiteers;
    }

    /**
     * Constructor with all fields (useful for converting).
     *
     * @param id Id of the Expense
     * @param amount Amount of money involved in the Expense
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Expense
     * @param createTime {@link LocalDateTime} marking the creation of the Expense
     * @param title Title of the Expense
     * @param profiteers {@link List} of {@link Profiteer} related to the Expense
     */
    public Expense(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
        LocalDateTime createTime, String title, List<Profiteer> profiteers) {
        super(id, amount, payer, updateTime, createTime);
        this.title = title;
        this.profiteers = profiteers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Profiteer> getProfiteers() {
        return profiteers;
    }

    public void setProfiteers(List<Profiteer> profiteers) {
        this.profiteers = profiteers;
    }

    @Override
    public String toString() {
        return "Expense [title=" + title + ", profiteers=" + profiteers + "]";
    }

}
