function uploadBook(){
	var formData = new FormData();
	formData.append("nombre_libro", document.getElementById("nombre_libro").value);
	formData.append("autor_libro", document.getElementById("autor_libro").value);
	formData.append("genero_libro", document.getElementById("genero_libro").value);
	formData.append("anio_libro", document.getElementById("anio_libro").value);
	formData.append("editorial_libro", document.getElementById("editorial_libro").value);
	formData.append("file", document.getElementById("file").files[0]);
	let config = {
		method: 'POST',
		body: formData,
		header: {'Content-Type':'multipart/form-data'},
	}
	fetch("./UploadNewFile", config)
		.then(function(response){
			return response.json();
		})
		.then(function(data){
        console.log(data);
        if(data.status == 200){
            alert("Libro Agregado a la Lista..!");
        } else if (data.status == 500) {
        	alert(data.message);
        }
        location.reload();
    })
}

document.getElementById("upNewBook").addEventListener("click", uploadBook);