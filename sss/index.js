const {PORT, SECRET} = require('./constants/config')
var path = require('path');
global.APP_ROOT = path.resolve(__dirname);

const REG_DATE_FORM = /^\d{4}\D*\d{1,2}\D*\d{1,2}T\d{1,2}\:\d{1,2}\:\d{1,2}\.\d{1,3}Z$/

const signboard = require('./signboard');

const express = require('express');
const express_session = require('express-session');
const session_store = new (require('session-file-store')(express_session))({
    retries: 2
});
const session = express_session({
  secret: SECRET,          // 암호화 키
  name: 'Nsess',            // 세션 쿠키명
  resave: false,            // ?
  saveUninitialized: true,  // ?
  store: session_store      // 스토어
});


const logger = require('morgan');
// const cookieParser = require('cookie-parser');

const index_router = require('./routes');

const app = express();

const server = require('http').createServer(app);
const io = require('socket.io')(server);
const sharedSession = require('express-socket.io-session');
const ioRouter = require('./socketIO/socketIO');

// SETUP LOGGER
app.use(logger());

// 제이슨 응답
app.set(express.json());

// 제이슨 응답이 날짜 형태면 millisecond 형태로 반환
app.set('json replacer', (key, val) => {
  if(REG_DATE_FORM.test(val)){
    return new Date(val).getTime();
  }else
    return val;
});

// SETUP SESSION
app.use(session);

// SETUP SOCKET-IO
// io.use(sharedSession(session, cookieParser(SECRET, {})))
io.use(sharedSession(session))

// SETUP SOCKET-IO Routers
io.on('connection', ioRouter)

/** SETUP Routers
 */
app.use('/', index_router)


server.listen(PORT, () => {
  signboard();              // 시작 알림
})