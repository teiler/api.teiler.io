var chakram = require('chakram'),
    expect = chakram.expect;

var hostUrl = process.env.tylrurl;

var version = "v1/";
var baseUrl = hostUrl + version;

describe("Create a Person", function () {
  var group, person;
  var groupID;
  var postResponse;


  before("create group and add a person", function () {
    group = {
      name: "Hello World"
    };
    person = {
      name: "Hans Müller"
    };

    postResponse =  chakram.post(baseUrl + "groups", group)
    .then(function (response) {
      groupID = response.body.id;
      return chakram.post(baseUrl + "groups/" + groupID + "/people", person);
    });
  });

  it("should return 200 on success", function () {
      return expect(postResponse).to.have.status(200);
  });

  it("should return a json header", function () {
      return expect(postResponse).to.have.header("content-type", "application/json");
  });

  it("should have returned an id", function () {
    return expect(postResponse).to.have.json("id", function (id) {
      expect(id).to.be.a.number;
    });
  });

  it("should have returned the correct name", function () {
    return expect(postResponse).to.have.json("name", function (url) {
      expect(url).to.equal(person.name);
    });
  });

  it("should have set the correct times", function () {
    var updateRef;
    expect(postResponse).to.have.json("update-time", function (updateTime) {
      var check = Date.parse(updateTime);
      expect(check).not.to.be.NaN;
      updateRef = updateTime;
    });
    expect(postResponse).to.have.json("create-time", function (createTime) {
      var check = Date.parse(createTime);
      expect(check).not.to.be.NaN;
      expect(updateRef).to.equal(createTime);
    });
  });

  it("should return an error if a person with the same name exists", function () {
    var response = chakram.post(baseUrl + "groups/" + groupID + "/people", person);
    expect(response).to.have.status(409);
    return expect(response).to.have.json("error", function (error) {
      expect(error[0]).to.equal("PEOPLE_NAME_CONFLICT");
    });  
  });
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
