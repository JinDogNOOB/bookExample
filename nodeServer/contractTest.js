


function test(){
// 임시 키 생성 jwt 용
const secret = crypto.createHash('sha256').update(randomString()).digest();

// 컨트랙트 객체 생성
const CowTraceAbility = require ('./config/CowTraceAbility');
const contract = CowTraceAbility.getContract();
const myAddress = CowTraceAbility.myAddress;

async()=>{
    momPtr = randomString();
    dadPtr = randomString();
    childPtr = randomString();

    tempMeatPtr = randomString();
    tempMeatPtrForDiv = randomString();

    console.log("어미소 추가 및 정보 확인");
    let result = await contract.methods.getMeat("aaa").call();
    result = await contract.methods.addCow(momPtr, "n", "n", "owner", "mom").send({from : myAddress});
    result = await contract.methods.getCow(momPtr).call();
    console.log("어미소 추가 및 정보 확인 완료" + result)

    console.log("아비소 추가 및 정보 확인");
    result = await contract.methods.addCow(dadPtr, "n", "n", "owner", "dad").send({from : myAddress});
    result = await contract.methods.getCow(dadPtr).call();
    console.log("아비소 추가 및 정보 확인 완료" + result)

    console.log("자식소 추가 및 정보 확인");
    result = await contract.methods.addCow(childPtr, momPtr, dadPtr, "owner", "child").send({from : myAddress});
    result = await contract.methods.getCow(childPtr).call();
    console.log("자식소 추가 및 정보 확인 완료" + result)

    console.log("어미소 도축");
    result = await contract.methods.butcherCow(tempMeatPtr, "worker", momPtr, 400, "meat").send({from : myAddress});
    result = await contract.methods.getMeat(tempMeatPtr).call();
    console.log("어미소 도축 및 고기 정보 확인 완료" + result);

    console.log("고기 분할");
    result = await contract.methods.divideMeat(tempMeatPtrForDiv, "worker", tempMeatPtr, 200, "divmeat").send({from : myAddress});
    result = await contract.methods.getMeat(tempMeatPtrForDiv).call();
    console.log("고기 분할 및 고기 정보 확인 완료" + result);
}
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
