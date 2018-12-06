function setRandomBooks(){
    fetch("./UploadNewFile", {method: "GET"})
        .then(function(response){
            return response.json();
        })
        .then(function(data){
            alert(data.message);
        })
}