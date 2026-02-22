class iKassaTerminal {

    static intercept(data, response) {
        new Promise(async (resolve, reject) => {
            try {
                if (data.url.indexOf('/api/posap/1.0/entity/assortment') !== -1) {
                    const txt = await response.text();
                    console.log(txt);
                }
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
            } catch (e) {
                console.warn('Kassa interceptor error')
                console.warn(e);
            }
        }).then(() => console.log('Completed'));
    }
}