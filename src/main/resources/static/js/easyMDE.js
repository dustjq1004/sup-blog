document.addEventListener('DOMContentLoaded', () => {
    if (!isNull(content)) {
        easyMDE.value(content);
    }
});

const easyMDE = new EasyMDE({
    status: ["lines", "words"],
    spellChecker: false,
    element: $("#content")[0],
    promptURLs: true,
    sideBySideFullscreen: false,
    toolbarButtonClassPrefix: "mde",
    imageUploadEndpoint: '/',
    uploadImage: true,
    toolbar: [
        "bold", "italic", "strikethrough", "heading", "|",
        "code", "quote", "unordered-list", "ordered-list", "table", "|",
        "upload-image", "link", "|",
        "preview", "side-by-side"
    ],
    theme: "Modern",
    imageUploadFunction:
        async (file, onSuccess, onError) => {
            const result = await getResignedUrl(file.name);
            const presigendUrl = result.signedUrl;
            const s3Data = await requestS3PresignedImageUpload(presigendUrl, file.type, file);
            console.log(s3Data);
            if (s3Data.status == 200 || s3Data.status == 201) {
                onSuccess(s3Data.url.split("?")[0]);
            }
            // onError(s3Data);
        }
});

const getResignedUrl = async (fileName) => {
    const options = {
        method: "GET",
    }

    function success(result) {
        console.log(result);
        return result.json();
    }

    function fail() {

    }

    return await httpRequest("/api/s3/image?fileName=" + fileName, options, success, fail);

}

const requestS3PresignedImageUpload = async (presigendUrl, type, file) => {

    let options = {
        method: "PUT",
        body: file,
        headers: {
            "Content-type": type
        }
    };
    return await fetch(presigendUrl, options);
}