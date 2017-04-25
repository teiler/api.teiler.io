var chakram = require('chakram'),
    expect = chakram.expect;

describe("Teiler.io Root URL", function() {
    it("should provide a simple json with the doc url", function () {
        var response = chakram.get("https://api.teiler.io");
        expect(response).to.have.status(200);
	expect(response).to.have.header("content-type", "application/json");
        return chakram.wait();
    });
});
