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
import io.teiler.server.util.enums.Currency;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class holding all information about a group.
 *
 * @author lroellin
 */
public class Group {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("currency")
    private Currency currency;

    @Expose(deserialize = false)
    @SerializedName("people")
    private List<Person> people;

    @Expose(deserialize = false)
    @SerializedName("update-time")
    private LocalDateTime updateTime;

    @Expose(deserialize = false)
    @SerializedName("create-time")
    private LocalDateTime createTime;

    /**
     * Usual constructor.
     *
     * @param id Id of the Group
     * @param name Name of the Group
     * @param currency Default currency of the Group
     */
    public Group(String id, String name, Currency currency) {
        this.id = id;
        this.name = name;
        this.currency = currency;
    }

    /**
     * Constructor with all fields (useful for converting).
     *
     * @param id Id of the Group
     * @param name Name of the Group
     * @param currency Default currency of the Group
     * @param people {@link List} of {@link Person} being members of the Group
     * @param updateTime {@link LocalDateTime} marking the last update of the Group
     * @param createTime {@link LocalDateTime} marking the creation of the Group
     */
    public Group(String id, String name, Currency currency,
        List<Person> people, LocalDateTime updateTime, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.people = people;
        this.updateTime = updateTime;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
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
        return "Group [id=" + id + ", name=" + name + ", currency=" + currency + ", people="
            + people + ", updateTime=" + updateTime + ", createTime=" + createTime + "]";
    }

}
