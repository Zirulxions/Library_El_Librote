function $(id){
    return document.getElementById(id);
}

var aux;

function checkBName(){
    $('oldPass').value = null;
    $('newPass').value = null;
    $('oldEmail').value = null;
    $('newEmail').value = null;
    aux = "3";
    updateAccount();
}

function checkBEmail(){
    $('oldPass').value = null;
    $('newPass').value = null;
    $('newName').value = null;
    aux = "2";
    updateAccount();
}

function checkBPass(){
    $('oldEmail').value = null;
    $('newEmail').value = null;
    $('newName').value = null;
    aux = "1";
    updateAccount();
}

function updateAccount(){
    var data = {
    	aux: aux,
        contAntigua: $('oldPass').value,
        contNueva: $('newPass').value,
        emailAntiguo: $('oldEmail').value,
        emailNuevo: $('newEmail').value,
        nuNombre: $('newName').value,
    };          
    console.log(data)
    let config = {
        method: 'POST',
        body: JSON.stringify(data),
    };
    fetch("./UserUpdates", config)
    .then(function(response){
        return response.json();
    })
    .then(function(data){
        if(data.status == 200){
        	alert(data.message + " Actualizado...!");
        } else if (data.status == 500){
        	alert(data.message);
        }
        location.reload();
    })
}

$("cambiarNombre").addEventListener("click", checkBName);
$("cambiarEmail").addEventListener("click", checkBEmail);
$("cambiarPass").addEventListener("click", checkBPass);