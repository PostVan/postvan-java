let totalTests = 0;
let passedTests = 0;
let failedTests = 0;
class PostVanAPI {
  constructor(expector, variablePool, response) {
    this.expect = expector.expect;
    this.variables = variablePool;
    this.collectionVariables = variablePool.collectionVariables;
    this.response = response;
  }
  test(testName, testFunc) {
    // TODO: log in a map the testName and pass/fail result.
    try {
      totalTests++;
      testFunc();
      passedTests++;
    } catch (err) {
      failedTests++;
      console.error(err);
    }
  }
}

 function main() {
  const pm = new PostVanAPI(expector, variablePool, response);
    try {
      /* <REPLACE_ME_WITH_CODE> */
      return {totalTests, passedTests, failedTests};
    } catch (err) {
      return {totalTests, passedTests, failedTests};
    }
}

main();
