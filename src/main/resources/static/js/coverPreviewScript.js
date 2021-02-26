inpFile = document.getElementById("coverImg");
previewContainer = document.getElementById("coverPreview");
previewImage = previewContainer.querySelector(".coverPreview__cover");

inpFile.addEventListener("change", function() {
    file = this.files[0];

    if (file) {
        const reader = new FileReader();

        previewImage.style.display = "block";

        reader.addEventListener("load", function() {
            previewImage.setAttribute("src", this.result);
        });


        reader.readAsDataURL(file);
    } else {                       
        previewImage.style.display = null;
        previewImage.setAttribute("src", "");
    }
});

                    