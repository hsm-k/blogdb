const textType = document.getElementById('textType');
const imageType = document.getElementById('imageType');
const textContentSection = document.getElementById('textContentSection');
const imageContentSection = document.getElementById('imageContentSection');
const content = document.getElementById('content');
const imageFile = document.getElementById('imageFile');
const imageUrl = document.getElementById('imageUrl');
const imagePreview = document.getElementById('imagePreview');

function updateContentForm() {
    const isImage = imageType.checked;
    textContentSection.hidden = isImage;
    imageContentSection.hidden = !isImage;
    content.required = !isImage;
    imageFile.required = isImage && !imageUrl.value;
    imageUrl.required = isImage && imageFile.files.length === 0;
}

function showPreview(source) {
    if (!source) {
        imagePreview.hidden = true;
        imagePreview.removeAttribute('src');
        return;
    }

    imagePreview.src = source;
    imagePreview.hidden = false;
}

textType.addEventListener('change', updateContentForm);
imageType.addEventListener('change', updateContentForm);

imageFile.addEventListener('change', () => {
    const selectedFile = imageFile.files[0];
    if (selectedFile) {
        imageUrl.value = '';
        showPreview(URL.createObjectURL(selectedFile));
    }
    updateContentForm();
});

imageUrl.addEventListener('input', () => {
    if (imageUrl.value) {
        imageFile.value = '';
        showPreview(imageUrl.value);
    }
    updateContentForm();
});

updateContentForm();
