var chakram = require('chakram'),
    expect = chakram.expect;

var hostUrl = process.env.tylrurl;

var version = "v1/";

describe("Root URL", function() {
    it("should provide a simple json with the doc url", function () {
        var response = chakram.get(hostUrl);
        expect(response).to.have.status(200);
	      expect(response).to.have.header("content-type", "application/json");
        expect(response).to.have.json("documentation", function (url) {
            expect(url).to.equal("doc.teiler.io");
        });
        return chakram.wait();
    });
});
