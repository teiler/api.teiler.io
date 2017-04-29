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
  var firstPerson, secondPerson;
  var peopleResponse;

  before("create group with two people", function () {
    var groupID;

    var group = {
      name: "Hello World"
    };

    firstPerson = {
      name: "Hans Müller"
    };
    secondPerson = {
      name: "Heiri Müller"
    };

    peopleResponse = chakram.post(baseUrl + "groups", group)
    .then(function (groupResponse) {
      groupID = groupResponse.body.id;
      
      return chakram.post(baseUrl + "groups/" + groupID + "/people", firstPerson);
    })
    .then(function (createPersonResponse) {
      return chakram.post(baseUrl + "groups/" + groupID + "/people", secondPerson);
    })
    .then(function (createPersonResponse) {
      return chakram.get(baseUrl + "groups/" + groupID + "/people");
    });
  });

  it("should return 200 on success", function () {
      return expect(peopleResponse).to.have.status(200);
  });

  it("should return a json header", function () {
      return expect(peopleResponse).to.have.header("content-type", "application/json");
  });

  it("should return two people", function() {
    return expect(peopleResponse).to.have.json(function(json) {
      expect(json.length).to.equal(2);
    });
  });
});
