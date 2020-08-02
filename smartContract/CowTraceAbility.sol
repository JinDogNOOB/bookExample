pragma solidity ^0.6.6;

contract CowTraceAbility{
    
    // ########################### 스트럭트 정의 #############
    
    struct CowNode{ // key bytes32 == myPtr
        bool isUse; // 사용중인지 여부 
        uint8 status; // 상태 1이면 생존 2이면 도축 3이면 폐사
        string momNodePtr; // 어미 주소 32바이트 256비트
        string dadNodePtr; // 아비 주소
        string ownerPtr; // 주인 주소
    
        string desc; // 다양한 정보를 json 포맷으로 저장하는 변수 
    }
    
    struct MeatNode{ // key bytes32 
        bool isUse; // 사용중인지 여부 
        uint8 status; // 1이면 셀링중 2이면 소분할됨 3이면 팔림 
        
        string workerPtr; // 작업자의 주소
        string cowPtr; // 원본 소의 주소
        string parentNodePtr; // 부모 고기 노드의 주소
        
        uint32 weight; // 고기의 무게 
        uint32 cweight; // 현재 고기의 무게 만약 소분할 됬을 시에 사용
        
        string desc; // 다양한 정보를 json 포맷으로 저장하는 변수
    }
    
    
    // ############################# 변수 선언 ################
    
    address contractOwner; // 컨트랙트 주인 주소를 저장할 변수
    mapping(string => CowNode) cows;
    mapping(string => MeatNode) meats;
    
    
    // ############################# 아래부터 메소드 ##########
    
    constructor()public{ // 생성자 
        contractOwner = msg.sender; 
    }
    
    
    // 소를 추가하는 메소드 
    function addCow(string memory _ptr, string memory _momPtr, string memory _dadPtr, string memory _ownerPtr, string memory _desc)public {
        // 소 키가 사용중인지 확인, msg.sender 가 컨트랙트 소유주인지 확인, require 안의 값이 false이면 거절
        require((cows[_ptr].isUse != true) && (contractOwner == msg.sender) );
        
        // 소 정보 입력
        cows[_ptr].isUse = true;
        cows[_ptr].status = 1;
        cows[_ptr].momNodePtr = _momPtr;
        cows[_ptr].dadNodePtr = _dadPtr;
        cows[_ptr].ownerPtr = _ownerPtr;
        cows[_ptr].desc = _desc;
        
    }
    
    // 소를 도축하는 메소드 
    function butcherCow(string memory _ptr, string memory _workerPtr, string memory _cowPtr, uint32 _weight, string memory _desc)public {
        require((meats[_ptr].isUse != true) && (contractOwner == msg.sender) && (cows[_cowPtr].status == 1)); 
        // 고기 키가 사용중인지 확인, msg.sender 컨트랙트 소유주인지 확인, 소가 alive(1) 상태인지 확인 
        
        // 고기 정보 입력 
        meats[_ptr].isUse = true;
        meats[_ptr].status = 1;
        meats[_ptr].workerPtr = _workerPtr;
        meats[_ptr].cowPtr = _cowPtr;
        meats[_ptr].weight = _weight;
        meats[_ptr].cweight = _weight;
        meats[_ptr].desc = _desc;
    }
    
    // 고기를 분할하는 메소드 
    function divideMeat(string memory _ptr, string memory _workerPtr, string memory _parentMeatPtr, uint32 _weight, string memory _desc)public{
        require((meats[_ptr].isUse != true) && (contractOwner == msg.sender) && (meats[_parentMeatPtr].cweight >= _weight) && (meats[_parentMeatPtr].status != 3));
        // 고기 키가 사용중인지 확인, meg.sender 컨트랙트 소유주인지 확인, 원본고기의 cweight가 weight 보다 큰지 확인, 고기가 안팔렸는지 확인  
        
        meats[_ptr].isUse = true;
        meats[_ptr].status = 1;
        meats[_ptr].workerPtr = _workerPtr;
        meats[_ptr].cowPtr = meats[_parentMeatPtr].cowPtr;
        meats[_ptr].weight = _weight;
        meats[_ptr].cweight = _weight;
        meats[_ptr].desc = _desc;
        meats[_ptr].parentNodePtr = _parentMeatPtr;
        
        meats[_parentMeatPtr].status = 2; // 선대고기 분할됨 status 로 변경
        meats[_parentMeatPtr].cweight -= _weight; // 선대고기 cweight 감소 
    }
    
    
    // 소 정보를 가져오는 메소드 
    function getCow(string memory _ptr)public view returns(uint8, string memory, string memory, string memory, string memory){
        require(cows[_ptr].isUse == true);
        // 소가 존재하는 지 확인  
        return (cows[_ptr].status, cows[_ptr].momNodePtr, cows[_ptr].dadNodePtr, cows[_ptr].ownerPtr, cows[_ptr].desc);
    }
    
    // 고기 정보를 가져오는 메소드 
    function getMeat(string memory _ptr)public view returns(uint8, string memory, string memory, string memory, uint32, uint32, string memory){
        require(meats[_ptr].isUse == true);
        // 고기가 존재하는 지 확인 
        return (meats[_ptr].status, meats[_ptr].workerPtr, meats[_ptr].cowPtr, meats[_ptr].parentNodePtr, meats[_ptr].weight, meats[_ptr].cweight, meats[_ptr].desc);
        
    }
    
    
    // 소가 죽었을 시 
    function cowDied(string memory _ptr)public {
        require(contractOwner == msg.sender);
        cows[_ptr].status = 3;
    }
    
    // 고기가 팔렸을 시 
    function meatSelled(string memory _ptr)public {
        require(contractOwner == msg.sender);
        meats[_ptr].status = 3;
        
    }
    
    // 소 정보(desc)가 변경됨 
    function changeCowDesc(string memory _ptr, string memory _desc)public{
        require(contractOwner == msg.sender);
        cows[_ptr].desc = _desc;
    }
    
    // 고기 정보(desc)가 변경됨 
    function changeMeatDesc(string memory _ptr, string memory _desc)public{
        require(contractOwner == msg.sender);
        meats[_ptr].desc = _desc;
    }
    
    
}
