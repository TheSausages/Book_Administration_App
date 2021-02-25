document.querySelector("#publisherChoice").addEventListener('input', function(e) {
    var input = e.target,
    list = input.getAttribute('list'),
    options = document.querySelectorAll('#' + list + ' option'),
    hiddenInput = document.getElementById(input.getAttribute('id') + '-hidden'),
    label = input.value;

hiddenInput.value = label;

console.log(hiddenInput);

for(var i = 0; i < options.length; i++) {
    var option = options[i];

    if(option.value === label) {
        hiddenInput.value = option.getAttribute('data-value');
        break;
    }
}
});

document.querySelector("#authorChoice").addEventListener('input', function(e) {
    var input = e.target,
    list = input.getAttribute('list'),
    options = document.querySelectorAll('#' + list + ' option'),
    hiddenInput = document.getElementById(input.getAttribute('id') + '-hidden'),
    label = input.value;

hiddenInput.value = label;

for(var i = 0; i < options.length; i++) {
    var option = options[i];

    if(option.value === label) {
        hiddenInput.value = option.getAttribute('data-value');
        break;
    }
}
});