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
        if(data.redirect != null && data.redirect != undefined){
            alert("Cuenta Registrada :D");
            window.location.href = data.redirect;
        }
    })
    .catch((e) => {
        console.log(e)
    })
}

function validateEmail(email) {
    var re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
    return re.test(email);
}

function validate() {
    var email = document.getElementById("email").value;
    var cedula = document.getElementById("cedula").value;
    var nombre = document.getElementById("nombre").value;
    var contrasenia = document.getElementById("contrasenia").value;
    var direccion = document.getElementById("direccion").value;
    if(email.trim() == "" || cedula.trim() == "" || nombre.trim() == "" || contrasenia.trim() == "" || direccion.trim() == ""){
        document.getElementById("Start").disabled = true;
    } else {
        if (validateEmail(email)) {
            document.getElementById("Start").disabled = false;
        } else {
            document.getElementById("Start").disabled = true;
        }
        return false;
    }
}

document.getElementById("Start").disabled = true;

$("Start").addEventListener("click", createAccount);