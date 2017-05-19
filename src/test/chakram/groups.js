/*
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
var chakram = require('chakram'),
    expect = chakram.expect;

var hostUrl = process.env.tylrurl;

var version = "v1/";
var baseUrl = hostUrl + version;

describe("Create a group", function () {
  it("should create a group", function () {
    var updateRef;
    var group = {
      name: "Hello World"
    };
    var response = chakram.post(baseUrl + "groups", group);
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");
    expect(response).to.have.json("id", function (id) {
      expect(id).to.be.a("string");
    });
    expect(response).to.have.json("name", function (url) {
      expect(url).to.equal("Hello World");
    });
    expect(response).to.have.json("update-time", function (updateTime) {
      var check = Date.parse(updateTime);
      expect(check).not.to.be.NaN;
      updateRef = updateTime;
    });
    expect(response).to.have.json("create-time", function (createTime) {
      var check = Date.parse(createTime);
      expect(check).not.to.be.NaN;
      expect(updateRef).to.equal(createTime);
    });
    return chakram.wait();
  })
});

describe("Get a group", function () {
  var groupId;
  before("create group", function () {
    var group = {
      name: "Hello World"
    };
    return chakram.post(baseUrl + "groups", group)
    .then(function (response) {
      groupId = response.body.id;
    })
  });

  it("should get group", function () {
    var response = chakram.get(baseUrl + "groups/" + groupId);
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");
    expect(response).to.have.json("id", function (id) {
      expect(id).to.be.a("string");
    });
    expect(response).to.have.json("name", function (url) {
      expect(url).to.equal("Hello World");
    });
    expect(response).to.have.json("currency", function (currency) {
      expect(currency).to.equal("chf");
    });
    expect(response).to.have.json("update-time", function (updateTime) {
      var check = Date.parse(updateTime);
      expect(check).not.to.be.NaN;
    });
    expect(response).to.have.json("create-time", function (createTime) {
      var check = Date.parse(createTime);
      expect(check).not.to.be.NaN;
    });

    return chakram.wait();
  });

  it("should return Not authorized to group", function () {
      var response = chakram.get(baseUrl + "groups/ABC");
      expect(response).to.have.status(401);
      expect(response).to.have.json("error", function (error) {
        expect(error[0]).to.equal("NOT_AUTHORIZED_TO_GROUP");
      });
      return chakram.wait();
  })
});

describe("Edit a group", function () {
  var groupId;
  before("create group", function () {
    var group = {
      name: "Hello World"
    };
    return chakram.post(baseUrl + "groups", group)
    .then(function (response) {
      groupId = response.body.id;
    })
  });

  it("should edit group", function () {
    var updateRef;
    var editedGroup = {
      name: "Hallo Welt",
      currency: "eur"
    };

    var response = chakram.put(baseUrl + "groups/" + groupId, editedGroup);
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");
    expect(response).to.have.json("id", function (id) {
      expect(id).to.be.a("string");
    });
    expect(response).to.have.json("name", function (url) {
      expect(url).to.equal("Hallo Welt");
    });
    expect(response).to.have.json("currency", function (currency) {
      expect(currency).to.equal("eur");
    });
    expect(response).to.have.json("update-time", function (updateTime) {
      var check = Date.parse(updateTime);
      expect(check).not.to.be.NaN;
      updateRef = updateTime;
    });
    expect(response).to.have.json("create-time", function (createTime) {
      var check = Date.parse(createTime);
      expect(check).not.to.be.NaN;
      expect(updateRef).not.to.be.equal(createTime);
    });
    return chakram.wait();
  });

  it("should return Not authorized to group", function () {
    var response = chakram.put(baseUrl + "groups/ABC");
    expect(response).to.have.status(401);
    expect(response).to.have.json("error", function (error) {
      expect(error[0]).to.equal("NOT_AUTHORIZED_TO_GROUP");
    });
    return chakram.wait();
  });

  it("should return Currency not valid", function () {
    var editedGroup = {
    name: "Hallo Welt",
    currency: "abc"
    };

    var response = chakram.put(baseUrl + "groups/" + groupId, editedGroup);
    expect(response).to.have.status(416);
    expect(response).to.have.json("error", function (error) {
      expect(error[0]).to.equal("CURRENCY_NOT_VALID");
    });
    return chakram.wait();
  })
});

describe("Delete group", function () {
  var groupId;
  before("create group", function () {
    var group = {
      name: "Hello World"
    };
    return chakram.post(baseUrl + "groups", group)
    .then(function (response) {
      groupId = response.body.id;
    })
  });

  it("should delete group", function () {
    var response = chakram.delete(baseUrl + "groups/" + groupId);
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");
    return chakram.wait();
  });

  it("should return Not authorized to group", function () {
    var response = chakram.delete(baseUrl + "groups/ABC");
    expect(response).to.have.status(401);
    expect(response).to.have.json("error", function (error) {
      expect(error[0]).to.equal("NOT_AUTHORIZED_TO_GROUP");
    });
    return chakram.wait();
  })
});
