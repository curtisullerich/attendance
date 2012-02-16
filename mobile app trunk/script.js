window.addEventListener('load',loaded,true);

<!-- hide script from old browsers

function loaded() {
    var length = window.localStorage.length;
    document.querySelector('#local-count').innerHTML = length;
    document.querySelector('#submit').addEventListener('click',processData,false);
    alert('loaded!');
}

//end hiding script from old browsers -->
