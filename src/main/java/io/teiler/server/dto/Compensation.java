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
 * Class holding all information about a Compensation.
 *
 * @author pbaechli
 */
public class Compensation extends Transaction {

    @Expose
    @SerializedName("profiteer")
    private Person profiteer;

    /**
     * Usual constructor.
     *
     * @param id Id of the Compensation
     * @param amount Amount of money involved in the Compensation
     * @param payer Person who spent the money
     * @param profiteer {@link Profiteer} related to the Compensation
     */
    public Compensation(Integer id, Integer amount, Person payer, Person profiteer) {
        super(id, amount, payer, null, null);
        this.profiteer = profiteer;
    }

    /**
     * Constructor with all fields (useful for converting).
     *
     * @param id Id of the Compensation
     * @param amount Amount of money involved in the Compensation
     * @param payer Person who spent the money
     * @param updateTime {@link LocalDateTime} marking the last update of the Compensation
     * @param createTime {@link LocalDateTime} marking the creation of the Compensation
     * @param profiteer {@link Profiteer} related to the Compensation
     */
    public Compensation(Integer id, Integer amount, Person payer, LocalDateTime updateTime,
        LocalDateTime createTime, Person profiteer) {
        super(id, amount, payer, updateTime, createTime);
        this.profiteer = profiteer;
    }

    public Person getProfiteer() {
        return profiteer;
    }

    public void setProfiteer(Person profiteer) {
        this.profiteer = profiteer;
    }

    @Override
    public String toString() {
        return "Compensation [profiteer=" + profiteer + "]";
    }

}
