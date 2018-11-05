function $(id){
    return document.getElementById(id);
}    

function createAccount(){
    var data = {
        cedula: $('cedula').value,
        nombre: $('nombre').value,
        email: $('email').value,
        direccion: $('direccion').value,
        contrasenia: $('contrasenia').value,
    };          
    console.log(data)
    let config = {
        method: 'POST',
        body: JSON.stringify(data),
    };
    fetch("./Register", config)
    .then(function(response){
        return response.json();
    })
    .then(function(data){
        console.log(data);
    })
    .catch((e) => {
        console.log(e)
    })
    console.log("Something is not good.");
}

$("Start").addEventListener("click", createAccount);