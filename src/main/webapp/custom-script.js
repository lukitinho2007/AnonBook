let selectedImage;

function showPostEditMenu() {
    const postEditMenu = document.getElementById("postEditMenu");
    if (postEditMenu) {
        postEditMenu.style.display = 'block';
    }
}

function handleFileChange(event) {
    selectedImage = event.target.files[0];
}

async function post() {
    const title = document.getElementById('postTitle').value;
    if (title !== undefined) {
        if (selectedImage) {
            await postWithImage(title);
        } else {
            await postWithoutImage(title);
        }
        hidePostEditMenu();
    }
}

async function postWithImage(title) {
    const formData = new FormData();
    formData.append('image', selectedImage);

    const jsonArray = await postRequest(title, formData);
    imageDecoder(jsonArray);
}

async function postWithoutImage(title) {
    const jsonArray = await postWithoutImageRequest(title);
    imageDecoder(jsonArray);
}

function hidePostEditMenu() {
    const postEditMenu = document.getElementById('postEditMenu');
    if (postEditMenu) {
        postEditMenu.style.display = 'none';
    }
}

async function getPosts() {
    const jsonArray = await getPostsRequest();
    imageDecoder(jsonArray);
}

function imageDecoder(jsonArray) {
    const tape = document.getElementById('tape');
    tape.innerHTML = "";
    for (let i = jsonArray.data.length - 1; i >= 0; i--) {
        const postDiv = createPostDiv(jsonArray.data[i]);
        postDiv.onclick = async function () {
            await switchPage(postDiv);
        };
        tape.appendChild(postDiv);
    }
}

function createPostDiv(postData) {
    const postDiv = document.createElement('div');
    postDiv.className = "postDiv";
    postDiv.id = postData.id;

    const p = document.createElement('p');
    p.className = "time";
    p.innerText = `[N${postData.id}] ${postData.time}`;

    const title = document.createElement('p');
    title.className = "title";
    title.innerText = postData.title;

    postDiv.appendChild(p);
    postDiv.appendChild(title);

    if (postData.imageBase64) {
        const img = document.createElement('img');
        img.src = 'data:image/jpeg;base64,' + postData.imageBase64;
        img.className = "postImg";
        postDiv.appendChild(img);
    }

    return postDiv;
}

async function switchPage(postDiv) {
    localStorage.setItem("postId", postDiv.id);
    window.location.href = "post.html";
}

async function getPostData() {
    const postId = localStorage.getItem("postId");
    const jsonArray = await getPostComments(postId);

    const postDiv = createPostDiv(jsonArray.data);
    document.getElementById('root').appendChild(postDiv);

    const commentInput = createCommentInput();
    postDiv.appendChild(commentInput);

    displayComments(jsonArray);
}

function createCommentInput() {
    const yourComment = document.createElement('input');
    yourComment.id = "yourComment";
    yourComment.type = "text";
    yourComment.placeholder = "Write a comment";
    yourComment.onkeydown = function (event) {
        handleKeyDown(event);
    };

    const commentInput = document.createElement('div');
    commentInput.appendChild(yourComment);

    return commentInput;
}

async function handleKeyDown(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        const postId = localStorage.getItem("postId");
        const comment = document.getElementById('yourComment').value;
        if (comment) {
            await addCommentRequest(postId, comment);
            const jsonArray = await getPostComments(postId);
            document.getElementById('yourComment').value = "";
            displayComments(jsonArray);
        }
    }
}

function displayComments(jsonArray) {
    const commentsDiv = document.createElement('div');
    commentsDiv.id = "comments";

    for (let i = 0; i < jsonArray.comments.length; i++) {
        const comment = document.createElement('h4');
        comment.innerText = `[N${i+1}] ${jsonArray.comments[i].comment}`;
        commentsDiv.appendChild(comment);
    }

    const postId = localStorage.getItem("postId");
    const postContainer = document.getElementById(postId);
    if (postContainer) {
        postContainer.appendChild(commentsDiv);
    }
}