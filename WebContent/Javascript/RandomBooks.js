function setRandomBooks(){
    fetch("./UploadNewFile", {method: "GET"})
        .then(function(response){
            return response.json();
        })
        .then(function(data){
        	document.getElementById("FirstName").innerHTML = data.Lib1;
            document.getElementById("FirstURL").src = data.Dir1;
            document.getElementById("SecondName").innerHTML = data.Lib2;
            document.getElementById("SecondURL").src = data.Dir2;
            document.getElementById("ThirdName").innerHTML = data.Lib3;
            document.getElementById("ThirdURL").src = data.Dir3;
        })
}

function fClick1(){
	document.getElementById("viewClicked").style.display = "block";
	var x = document.getElementById("FirstURL").src;
	document.getElementById("URLView").src = x;
}

function fClick2(){
	document.getElementById("viewClicked").style.display = "block";
	var x = document.getElementById("SecondURL").src;
	document.getElementById("URLView").src = x;
}

function fClick3(){
	document.getElementById("viewClicked").style.display = "block";
	var x = document.getElementById("ThirdURL").src;
	document.getElementById("URLView").src = x;
}

document.getElementById("clk1").addEventListener("click", fClick1);
document.getElementById("clk2").addEventListener("click", fClick2);
document.getElementById("clk3").addEventListener("click", fClick3);

document.getElementById("viewClicked").style.display = "none";