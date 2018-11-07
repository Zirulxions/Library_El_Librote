//function $(id){
//  document.getElementById(id);
//}

function logOut(){
  let config = {
    method: 'GET',
  };
  fetch("./LogoutBiblioteca", config)
    .then(function(response){
    return response.json();
  })
  .then(function(data){
    if(data.redirect != null && data.redirect != undefined){
      sessionStorage.clear();
      window.location.href = data.redirect;
    }
  })
  .catch((e) => {
    console.log(e)
  })
}

//$("Start").addEventListener("click", logOut);