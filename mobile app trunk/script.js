window.addEventListener('load',loaded,true);


function loaded() {
    var length = window.localStorage.length;
    document.querySelector('#local-count').innerHTML = length;
    document.querySelector('#submit').addEventListener('click',processData,false);
    alert('loaded!');
}
