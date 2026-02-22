class iKassaTerminal {

    static intercept(data) {
        if (data.url.indexOf('/api/posap/1.0/entity/retaildemand') !== -1 && data.method === 'POST') {
            // const payment = JSON.parse(data.body);
            // console.log(payment);
            fetch('http://localhost:19879', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: data.body
            }).then(resp => {
                console.log(resp.status);
            });
        }
    }
}