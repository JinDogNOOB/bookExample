const Web3 = require('web3');
const url = 'ws://127.0.0.1:8545';
const web3 = new Web3(url);
const contractAddress="0x1b6d34A796bDFE819A0B6Ee0148402EE21F19999";


const contractAbi = [
	{
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
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
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
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
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
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
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
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
	  "inputs": [
		{
		  "internalType": "string",
		  "name": "_ptr",
		  "type": "string"
		}
	  ],
	  "name": "cowDied",
	  "outputs": [],
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
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
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
	  "inputs": [
		{
		  "internalType": "string",
		  "name": "_ptr",
		  "type": "string"
		}
	  ],
	  "name": "meatSelled",
	  "outputs": [],
	  "stateMutability": "nonpayable",
	  "type": "function"
	},
	{
	  "inputs": [],
	  "stateMutability": "nonpayable",
	  "type": "constructor"
	},
	{
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
	  "stateMutability": "view",
	  "type": "function"
	},
	{
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
	  "stateMutability": "view",
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