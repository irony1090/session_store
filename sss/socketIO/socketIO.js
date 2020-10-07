

const ioRouter = (socket) => {
  socket.on('login', (userData) => {
    const {session} = socket.handshake;
    console.log('[LOGIN] START');
    console.log(userData);
    console.log(session);
    console.log('[LOGIN] E N D');
    session.userData = userData;
    session.save();
  });
  socket.on('logout', () => {
    const {session} = socket.handshake;
    console.log('[LOGOUT] START');
    console.log(session);
    console.log('[LOGOUT] E N D');
    delete session.userData;
    session.save();
  })
}

module.exports = ioRouter;