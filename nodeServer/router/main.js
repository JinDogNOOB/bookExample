// 라우팅 모아놓은 곳 
const jwt = require('jsonwebtoken');
const crypto = require('crypto');

// 임시 키 생성 jwt 용
const secret = crypto.createHash('sha256').update(randomString()).digest();

// 컨트랙트 객체 생성
const CowTraceAbility = require ('../config/CowTraceAbility');
const contract = CowTraceAbility.getContract();

// Async Wrapper 에러처리 try catch를 신경안쓸수 있게 해주는거
const wrap = asyncFn => {
      return (async (req, res, next) => {
        try {
          return await asyncFn(req, res, next)
        } catch (error) {
          return next(error)
        }
      })  
    };





module.exports = function(app, connection)
{

    
    // 회원가입 
    app.post('/user/signup', function(req, res){
        // req.query 는 get req.body 는 post 값

        // 유저수 카운트 
        let sql = "SELECT count(*) AS idcount FROM USER_TB WHERE USERID = " + "'"+ req.query.userid+"'";
        connection.query(sql, function(error, result){

            if(error){
                res.send({
                    "resultcode" : false
                });
                console.log(error);
            }else{
                // 아이디가 중복이 안되면 
                if(result[0].idcount == 0){
                    let key = randomString();

                    let user = {
                        "OWNERKEY" : key,
                        "USERID" : req.body.userid,
                        "PASSWORD" : req.body.password,
                        "USERTYPE" : req.body.usertype,
                        "NAME" : req.body.name,
                        "ADDRESS" : req.body.address
                    };
                    // DB에 유저 정보 입력
                    connection.query("INSERT INTO USER_TB SET ?", user, function(error, results, fields){ 
                        if (error){
                            res.send({
                                "resultCode":false
                            });
                            console.log(error);
                        }else{
                            res.send({
                                "resultCode":true
                            });
                        }
                    });
                //아이디가 중복되면 
                }else{
                    res.send({
                        "resultCode" :false
                    });
                }
            }
        });

        /*
        res.json({
            msg: "thisismsg",
            asdf: "thisisasd",
            asdd: "1"
        });
        */
      



    });

    // 로그인 
    app.post('/user/signin', function(req, res){
        // 회원 정보를 가져온다.
        let sql = "SELECT * FROM USER_TB WHERE USERID ='" + req.body.userid + "'";
        connection.query(sql, function(error, result){
            if(error){
                return res.json({
                    "resultCode" : false
                });
                console.log(error);
            }else{
                // 가져온 회원 정보와 입력값 비교 시작 
                if(req.body.password == result[0].PASSWORD){
                    // 같으면 성공

                    jwt.sign({
                        key: result[0].KEY,
                        userid: result[0].USERID,
                        usertype: result[0].USERTYPE,
                        username: result[0].NAME
                        
                    },
                    secret,
                    {
                        expiresIn: '24h',
                        issuer: "ckyang.com"
                        
                    }, (error, token) => {
                        if(error){
                            console.log(error);
                            return res.json({
                                resultCode: false
                            });
                        }else{
                            return res.json({
                                "resultCode" : true,
                                "usertype" : result[0].USERTYPE,
                                "key" : result[0].KEY, 
                                "jwt" : token
                            });
                        } 
                    });

                }else{
                    // 로그인 실패 
                    return res.json({
                        "resultCode" : false
                    });

                }
            }
        });
    });
    // ############################################## TEST ###################

    // jwt 테스트 
    app.get('/test/jwt', function(req, res){
        let token = req.body.jwt;
        if(token == null){
            // 토큰이 없을 시
            res.send({
                "resultCode": false
            });
        }else{
            jwt.verify(token, secret, (error, decoded)=>{

                if(error){
                    console.log(error);
                    res.send({
                        "resultCode":false
                    });
                }else{
                    res.send({
                        "resultCode":true,
                        "key": decoded.key,
                        "userid": decoded.userid,
                        "username" : decoded.username,
                        "usertype":decoded.usertype
                    });
                }
            });

        }
        
    })

    // 컨트랙트 테스트 
    app.get('/test/contract', function(req, res){
        let record = contract.methods.getMeat("aa").call(function(error, result){
            if(error){
                console.log(error);
            }else{
                console.log(result);
                res.send({
                    data : result
                });
            }
        });
        console.log("22");
    });

    // Express + Async Await Test with Wrapping
    app.get('/test/asyncwrap', wrap(async(req, res, next) =>{
        let record = await contract.methods.getMeat("aa").call();
        console.log(record);
        res.send({
            result : record
        });
    }));

    app.get('/test/async', async(req, res, next) =>{
        try{
            let record = await contract.methods.getMeat("aaa").call();
            
            console.log(record);
            res.send({
                result : record
            });
        }catch(error){
            console.log(error);
            res.send({
                resultCode : error      
            });
        }
       
    });

    // ################################## TEST END ######################


    // ################################## 컨트랙트 통신 ##################
    // ADMIN 소 추가 
    app.post('/admin/addcow', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            if(token == null){
                res.send({
                    resultCode : false
                });
            }else{
                let decoded = await jwt.verify(token, secret);
                if(decoded.usertype == 99){ //0 일반유저 1 가축주인 2 도축 99 관리자
                    let cowPtr = randomString();
                    let momPtr = req.body.momptr;
                    let dadPtr = req.body.dadptr;
                    let ownerPtr = req.body.ownerptr;
                    let desc = req.body.desc;

                    let result = await contract.methods.addCow(cowPtr, momPtr, dadPtr, ownerPtr, desc).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});

                    let cows = {
                        OWNERKEY : req.body.ownerptr,
                        COWPTR : cowPtr  
                    };
                    // DB에 유저 정보 입력
                    await connection.query("INSERT INTO COWS_TB SET ?", cows);

                    
                    res.send({
                        resultCode : true,
                        cowPtr : cowPtr
                    });

                }else{
                    res.send({
                        resultCode:false
                    });
                } 
    
            }
        }catch(error){
            console.log(error);
            res.send({
                resultCode:false
            });
        }
       
    });

     // F_USER 소 도축 
     app.post('/fuser/butchercow', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);
            

            if(decoded.usertype == 2){
                let meatPtr = randomString();
                let workerPtr = decoded.key;
                let cowPtr = req.body.cowptr;
                let weight = req.body.weight;
                let desc = req.body.desc;

                await contract.methods.butcherCow(meatPtr, workerPtr, cowPtr, weight, desc).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
                res.send({
                    resultCode:true,
                    meatPtr : meatPtr
                });
                
            }else{
                res.send({
                    resultCode:false
                });
            } 
        }catch(error){
            console.log(error);
            res.send({
                resultCode:false
            });
        }
                  
    });

      // F_USER 고기 분할 
      app.post('/fuser/dividemeat', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            if(decoded.usertype == 2){
                let meatPtr = randomString();
                let workerPtr = decoded.key;
                let pMeatPtr = req.body.pmeatptr;
                let weight = req.body.weight;
                let desc = req.body.desc;

                await contract.methods.divideMeat(meatPtr, workerPtr, pMeatPtr, weight, desc).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
                res.send({
                    resultCode:true,
                    meatPtr : meatPtr
                });
                
            }else{
                res.send({
                    resultCode:false
                });
            } 
        }catch(error){
            console.log(error);
            res.send({
                resultCode:false
            });
        }
    });

    // USER 소정보 가져오기 
    app.post('/user/getcow', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            let cowPtr = req.body.cowptr;
            let result = await contract.methods.getCow(cowPtr).call();
            res.send({
                resultCode:true,
                status : result[0],
                momPtr : result[1],
                dadPtr : result[2],
                ownerPtr : result[3],
                desc : result[4]
            });
        }catch(error){
            console.log(error);
            res.send({
                resultCode:false
            });
        }
    });

    // USER 고기 정보 가져오기 
    app.post('/user/getmeat', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            let meatPtr = req.body.meatptr;
            let result = await contract.methods.getMeat(meatPtr).call();
            console.log(result);
            res.send({
                resultCode:true,
                key : meatPtr,
                status : result[0],
                workerPtr : result[1],
                cowPtr : result[2],
                pMeatPtr : result[3],
                weight : result[4],
                cweight : result[5],
                desc : result[6]
            });
        }catch(error){
            console.log(error);
            res.send({
                resultCode:false
            });
        }
    });


    // ADMIN 소 사망
    app.post('/admin/cowdied', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            if(decoded.usertype == 99){

                let cowPtr = req.body.cowptr;
                await contract.methods.cowDied(cowPtr).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
                return res.json({
                    resultCode : true
                });

            }else{
                return res.json({
                    resultCode : false
                });
            }
        }catch(error){
            console.log(error);
            return res.json({
                resultCode : false

            });
        }

    });

    // USER 고기 팔림
    app.post('/user/meatselled', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            

            let meatPtr = req.body.meatptr;
            await contract.methods.meatSelled(meatPtr).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
            return res.json({
                resultCode : true
            });

          
        }catch(error){
            console.log(error);
            return res.json({
                resultCode : false

            });
        }
    });

    // ADMIN 소 정보 변경
    app.post('/admin/editcow', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);

            if(decoded.usertype == 99){

                let cowPtr = req.body.cowptr;
                let desc = req.body.desc;

                await contract.methods.changeCowDesc(cowPtr, desc).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
                return res.json({
                    resultCode : true
                });

            }else{
                return res.json({
                    resultCode : false
                });
            }
        }catch(error){
            console.log(error);
            return res.json({
                resultCode : false

            });
        }
    });


    // ADMIN 고기 정보 변경
    app.post('/admin/editmeat', async(req, res, next)=>{
        try{
            let token = req.body.jwt;
            let decoded = await jwt.verify(token, secret);
            

            if(decoded.usertype == 99){

                let meatPtr = req.body.meatptr;
                let desc = req.body.desc;

                await contract.methods.changeMeatDesc(meatPtr, desc).send({from : "0x642fd1b53daffcf27db09e8441219d2adb92bf74"});
                return res.json({
                    resultCode : true
                });

            }else{
                return res.json({
                    resultCode : false
                });
            }
        }catch(error){
            console.log(error);
            return res.json({
                resultCode : false

            });
        }
    });


// B_USER 소유한 소 전부 긁어오기
app.post('/buser/getMyCow', async(req, res, next)=>{
    try{
        let token = req.body.jwt;
        let decoded = await jwt.verify(token, secret);

        if(decoded.usertype == 1){
            let keystr = String(decoded.key);
            /*
            let sql = "SELECT COWPTR FROM COWS_TB WHERE KEY = '"+keystr+"'";
            let result = await connection.query("SELECT COWPTR FROM COWS_TB WHERE KEY = ?", keystr);
            return res.json({
                resultCode: true,
                cows : result
            });
	     * p
            */
           // let sql = "SELECT COWPTR FROM COWS_TB WHERE COWPTR = 'ZHkb88Dicf8pJiTBkEhqf8eGrsmLuiAS'";

            let sql = "SELECT COWPTR FROM COWS_TB WHERE OWNERKEY = '" + decoded.key + "'";
            connection.query(sql, function(error, result){

            if(error){
                console.log(error);
                return res.json({
                    "resultcode" : false
                });
                
            }else{
                return res.json({
                    "resultCode":true
                })

            }});
            
        }else{
            return res.json({
                resultCode : false
            });
        }
    }catch(error){
        console.log(error);
        return res.json({
            resultCode : false

        });
    }
});







}




function randomString() {
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
    var string_length = 32;
    var randomstring = '';
    for (var i=0; i<string_length; i++) {
    var rnum = Math.floor(Math.random() * chars.length);
    randomstring += chars.substring(rnum,rnum+1);
    }
    //document.randform.randomfield.value = randomstring;
    return randomstring;
    }
