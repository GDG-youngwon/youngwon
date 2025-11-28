// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        function success() {
            alert("삭제가 완료되었습니다.");
            location.replace("/articles");
        }

        function fail() {
            alert("삭제 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("DELETE", "/api/articles/" + id, null, success, fail);
    });
}

// 수정 기능
// 1. id가 modify-btn인 엘리먼트 조회
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    // 2. 클릭 이벤트가 감지되면 수정 API 요청
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });

        function success() {
            alert("수정 완료되었습니다.");
            location.replace("/articles/" + id);
        }

        function fail() {
            alert("수정 실패되었습니다.");
            location.replace("/articles/" + id);
        }

        httpRequest("PUT", "/api/articles/" + id, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById("create-btn");

if (createButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보냄
    createButton.addEventListener("click", (event) => {
        body = JSON.stringify({
            title: document.getElementById("title").value,
            content: document.getElementById("content").value,
        });
        function success() {
            alert("등록 완료되었습니다.");
            location.replace("/articles");
        }
        function fail() {
            alert("등록 실패했습니다.");
            location.replace("/articles");
        }

        httpRequest("POST", "/api/articles", body, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(";");
    cookie.some(function (item) {
        item = item.replace(" ", "");

        var dic = item.split("=");

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: "Bearer " + localStorage.getItem("access_token"),
            "Content-Type": "application/json",
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}

// 좋아요 기능
let userId = -1;
let postId = null;
const isArticlePage = document.getElementById("article-id") !== null;

function fetchLikeStatus(postId) {
    if (userId === -1) return;

    fetch(`/posts/${postId}/like/status?userId=${userId}`)
        .then(res => res.json())
        .then(isLiked => {
            const btn = isArticlePage
                ? document.getElementById("likeBtn")
                : document.getElementById(`likeBtn-${postId}`);
            if (!btn) return;

            btn.innerText = isLiked ? "❤️" : "🤍";
            btn.classList.toggle("btn-like-active", isLiked);
            btn.classList.toggle("btn-outline-secondary", !isLiked);
        });
}

function fetchLikeCount(postId) {
    fetch(`/posts/${postId}/like/count`)
        .then(res => res.text())
        .then(count => {
            const countEl = isArticlePage
                ? document.getElementById("likeCount")
                : document.getElementById(`likeCount-${postId}`);
            if (countEl) countEl.innerText = count;
        });
}

function toggleLike(postId) {
    if (userId === -1) {
        alert("로그인 후 이용해주세요.");
        return;
    }

    fetch(`/posts/${postId}/like?userId=${userId}`, { method: 'POST' })
        .then(() => {
            fetchLikeStatus(postId);
            fetchLikeCount(postId);
        });
}

function initArticlePage() {
    const articleIdInput = document.getElementById("article-id");
    if (!articleIdInput) return;
    postId = articleIdInput.value;

    fetchLikeStatus(postId);
    fetchLikeCount(postId);

    const likeBtn = document.getElementById("likeBtn");
    if (likeBtn) {
        likeBtn.addEventListener("click", () => toggleLike(postId));
    }
}

function initArticleListPage() {
    const cards = document.querySelectorAll('.card[data-article-id]');
    const articleIds = Array.from(cards).map(c => c.dataset.articleId);

    articleIds.forEach(id => {
        fetchLikeStatus(id);
        fetchLikeCount(id);

        const likeBtn = document.getElementById(`likeBtn-${id}`);
        if (likeBtn) {
            likeBtn.addEventListener("click", () => toggleLike(id));
        }
    });
}

// DOM 로딩 후 실행
window.addEventListener("DOMContentLoaded", () => {
    const userIdInput = document.getElementById("userIdVar");
    if (userIdInput) {
        userId = parseInt(userIdInput.value || -1);
    }

    if (userId === -1) return;

    if (isArticlePage) {
        initArticlePage();
    } else {
        initArticleListPage();
    }
});