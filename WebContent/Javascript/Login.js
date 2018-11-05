function $(id){
  return document.getElementById(id);
}    
function logIn(){
  var data = {
    ci: $('ci').value,
    password: $('password').value,
  };
  console.log(data)
  let config = {
    method: 'POST',
    body: JSON.stringify(data),
  };
  fetch("./Login", config)
    .then(function(response){
      return response.json();
    })
    .then(function(data){
      console.log(data);
      if(data.redirect != null && data.redirect != undefined){
        sessionStorage.setItem("Identity",data.ci);
        window.location.href = data.redirect;
      }
    })
    .catch((e) => {
      console.log(e)
    })
}

$("Start").addEventListener("click", logIn);