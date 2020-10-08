const router = require('express').Router();

router.get('/', (req, res) => {
  console.log(`세션 생성 ${req.sessionID}`);
  const {maxAge} = req.query;
  if(maxAge)
    req.session.cookie.maxAge = parseInt(maxAge);

  const {cookie, ..._session} = req.session;
  const {__lastAccess, ...session} = _session;
  console.log(__lastAccess);
  res.send({
    id: req.sessionID,
    session, cookie,
    lastAccess: __lastAccess
  })
})

router.put('/', (req, res) => {
  console.log(`세션 수정 ${req.sessionID}`);

  const new_session = {
      ...req.session,
      ...req.body
  }
  // req.session = new_session
  Object.keys(new_session).map(key => {
      const val = new_session[key];
      if(key === 'cookie'){
          if(val.expires)
              val.expires = new Date(val.expires);
          
          Object.assign(req.session.cookie, val);
      }else if(val){
          req.session[key] = val;
      }else{
          delete req.session[key];
      }
  })

  res.send({
      ...req.session
  })
})
router.get('/hello', (req, res) => {
  console.log(`세션 생성 : ${req.sessionID}`);
  res.sendFile(APP_ROOT + '/public/index.html' );
})

module.exports = router;