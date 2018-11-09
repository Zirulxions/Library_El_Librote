function load(){
	document.getElementById("Ced").innerHTML = sessionStorage.getItem("user");
}

/*function newFile(){
	var formData = new FormData();
	formData.append("file", document.getElementById("file").files[0]);
	let config{
		method: 'POST',
		body: formData,
		header: {'Content-Type':'multipart/form-data'},
	}
	fetch("./UploadNewFile", config)
	.then(function(response)){
		return response.json();
	}
	.then(function(formData){
        console.log(formData);
    })
}*/

/*
function uploadFiles() {
	var files = document.getElementById("files").files;
	var fd = new FormData();
	fd.append("card_id", localStorage.getItem("card_id"));

	for (var i = 0; i < files.length; i++) {
	  console.log('added file');
	  var file = files[i];
	  fd.append('files[]', file, file.name);
	}	

	xhr("POST", fd, "/TrelloProject/Main/Data/CardsServlet/addfiles", function(res) {
		handleResponse(res);
		getFileList();
	});
}*/

//function alterateInput(){
//	var f = document.getElementById("file").files[0].name;
//	var fName = document.getElementById("nameFile").getAttribute("value");
//	fName = "Holis";
//}