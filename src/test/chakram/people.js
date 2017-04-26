var chakram = require('chakram'),
    expect = chakram.expect;

var hostUrl = process.env.tylrurl;

var version = "v1/";
var baseUrl = hostUrl + version;

describe("Create a Person", function () {
  var groupId;
  var updateRef;
  before("create group", function () {
    var group = {
      name: "Hello World"
    };
    return chakram.post(baseUrl + "groups", group)
    .then(function (response) {
      groupId = response.body.id;
    })
  });

  it("should create a person", function () {
    var person = {
      name: "Hans Müller"
    };
    var response = chakram.post(baseUrl + "groups/" + groupId + "/people",
        person);
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");
    expect(response).to.have.json("id", function (id) {
      expect(id).to.be.a.number;
    });
    expect(response).to.have.json("name", function (url) {
      expect(url).to.equal("Hans Müller");
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

describe("Get people", function () {
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
  var personId1, personId2;
  before("create people", function () {
    var person1 = {
      name: "Hans Müller"
    };
    var person2 = {
      name: "Heiri Müller"
    };
    chakram.post(baseUrl + "groups/" + groupId + "/people", person1)
    .then(function (response) {
      personId1 = response.body.id
    });
    chakram.post(baseUrl + "groups/" + groupId + "/people", person2)
    .then(function (response) {
      personId2 = response.body.id
    });
  });

  it("should get both people", function () {
    var response = chakram.get(baseUrl + "groups/" + groupId + "/people");
    expect(response).to.have.status(200);
    expect(response).to.have.header("content-type", "application/json");

    // TODO more tests (later Beni, later)
    return chakram.wait();
  })
});
