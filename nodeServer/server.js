// 서버 시작하는 곳 
let bodyParser = require('body-parser');

// ### 서버
let express = require('express');
let app = express();
app.use(bodyParser.urlencoded({extended:true}));

// ### DB (mysql)
let mysql = require('mysql');
let dbconfig = require('./config/database.js');
let connection = mysql.createConnection(dbconfig);
connection.connect();





let router = require('./router/main')(app, connection);
/* 
// ejs 설정 
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);

*/



let server = app.listen(8546, function(){
    console.log("Express server has started on port 8546");
})