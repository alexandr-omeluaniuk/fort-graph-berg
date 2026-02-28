class iKassaTerminal {

    static intercept(data, response) {
        new Promise(async (resolve, reject) => {
            try {
                if (data.url.indexOf('/api/posap/1.0/entity/assortment') !== -1) {
                    const txt = await response.text();
                    fetch('http://localhost:19879/products', {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: txt
                    }).then(resp => {
                        console.log(resp.status);
                    });
                }
                if (data.url.indexOf('/api/posap/1.0/entity/retaildemand') !== -1 && data.method === 'POST') {
                    fetch('http://localhost:19879/sale', {
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