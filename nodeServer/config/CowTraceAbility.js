const Web3 = require('web3');
const url = 'http://172.27.0.168:8545';
const web3 = new Web3(url);
const contractAddress="0x81B3b924B51f45700eC108bD71Dd5134Ae15d5ac";

const contractAbi = [
	{
		"inputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "constructor"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_momPtr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_dadPtr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_ownerPtr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_desc",
				"type": "string"
			}
		],
		"name": "addCow",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_workerPtr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_cowPtr",
				"type": "string"
			},
			{
				"internalType": "uint32",
				"name": "_weight",
				"type": "uint32"
			},
			{
				"internalType": "string",
				"name": "_desc",
				"type": "string"
			}
		],
		"name": "butcherCow",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_desc",
				"type": "string"
			}
		],
		"name": "changeCowDesc",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_desc",
				"type": "string"
			}
		],
		"name": "changeMeatDesc",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			}
		],
		"name": "cowDied",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_workerPtr",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "_parentMeatPtr",
				"type": "string"
			},
			{
				"internalType": "uint32",
				"name": "_weight",
				"type": "uint32"
			},
			{
				"internalType": "string",
				"name": "_desc",
				"type": "string"
			}
		],
		"name": "divideMeat",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			}
		],
		"name": "getCow",
		"outputs": [
			{
				"internalType": "uint8",
				"name": "",
				"type": "uint8"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": true,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			}
		],
		"name": "getMeat",
		"outputs": [
			{
				"internalType": "uint8",
				"name": "",
				"type": "uint8"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			},
			{
				"internalType": "uint32",
				"name": "",
				"type": "uint32"
			},
			{
				"internalType": "uint32",
				"name": "",
				"type": "uint32"
			},
			{
				"internalType": "string",
				"name": "",
				"type": "string"
			}
		],
		"payable": false,
		"stateMutability": "view",
		"type": "function"
	},
	{
		"constant": false,
		"inputs": [
			{
				"internalType": "string",
				"name": "_ptr",
				"type": "string"
			}
		],
		"name": "meatSelled",
		"outputs": [],
		"payable": false,
		"stateMutability": "nonpayable",
		"type": "function"
	}
]


module.exports = {
    getContract: function(){
        return new web3.eth.Contract(contractAbi, contractAddress);
    }
}


/*
async function func(){
    const contract = new 
    const record = await contract.methods.getMeat("aaa").call();
    console.log(record);
}

func();
*/