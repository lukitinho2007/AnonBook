async function submitPostRequest(title, image) {
    const response = await fetch(`/secretSocial/post?title=${title}`, {
        method: 'POST',
        body: image
    });
    return await response.json();
}

async function submitPostWithoutImageRequest(title) {
    const response = await fetch(`/secretSocial/post?title=${title}`, {method: 'POST'});
    return await response.json();
}

async function fetchPostsData() {
    const response = await fetch("/secretSocial/posts", {method: "GET"});
    return await response.json();
}

async function fetchPostComments(postId) {
    const response = await fetch(`/secretSocial/comments?postId=${postId}`, {method: "GET"});
    return await response.json();
}

async function addCommentToPost(postId, comment) {
    await fetch(`/secretSocial/comment?postId=${postId}&comment=${comment}`, {method: "POST"});
}