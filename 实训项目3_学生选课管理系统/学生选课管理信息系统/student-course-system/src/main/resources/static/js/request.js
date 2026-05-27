// 封装通用请求函数
function request(url, method, data) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: data ? JSON.stringify(data) : null
    })
        .then(response => {
            if (!response.ok) throw new Error('接口请求失败');
            return response.json();
        });
}