function $(id){
    return document.getElementById(id);
}

function load(){
	document.getElementById("Ced").innerHTML = sessionStorage.getItem("user");
}