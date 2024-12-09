const Sandbox = require('postman-sandbox'),
    context;

Sandbox.createContext(function (err, ctx) {
    if (err) {
        return console.error(err);
    }

    ctx.execute(testScript, {}, {}, function (err) {
        if (err) {
            return console.error(err);
        }
        console.log('executed')
    });
});