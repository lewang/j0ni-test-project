var assert = require('should');
var mod = require('../');

describe('doSomething', function () {
  it('should return true', function () {
    mod.doSomething().should.eql(true);
  });
});
