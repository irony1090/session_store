const {PORT, isProduct} = require('./constants/config')

const figlet = require('figlet');
const chalk = require('chalk');

const signboard = () => {
  if(!isProduct) console.log('\033[2J');

  const title = chalk.blue.bold(
                  figlet.textSync('Session Store Server', {
                    horizontalLayout: 'default',
                    verticalLayout: "default",
                  })
                ),
        description = chalk.white.yellow(`Activate SessionStoreServer as ${process.env.NODE_ENV} on PORT: ${PORT}`);

  console.log(title);
  console.log(description);
}

module.exports = signboard;