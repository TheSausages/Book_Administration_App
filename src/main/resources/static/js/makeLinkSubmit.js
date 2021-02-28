
function SubmitDeleteButton(inputId) {
    var confirmacion =  confirm('Are you sure about deleting this Entity?');

    if (confirmacion == true) {
        console.log('deleteEntityButton' + inputId);
        document.getElementById('deleteEntityButton' + inputId).submit();
    }
}