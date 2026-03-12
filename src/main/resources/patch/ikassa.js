class iKassaTerminal {

    static async preSaleHook(data) {
        if (data.url.indexOf('/api/posap/1.0/entity/retaildemand') !== -1 && data.method === 'POST') {
            const sale = JSON.parse(data.body);
            const dateMoment = new Date(sale.moment);
            const differenceMs = new Date() - dateMoment;
            if (differenceMs > 10000) {
                console.log('Exit by date');
                return true;
            }
            const resp = await fetch('http://localhost:19879/sale', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: data.body
            });
            if (!resp.ok) {
                alert("Ошибка при оплате через терминал. Повторите попытку");
                return false;
            }
        }
        return true;
    }

    static async intercept(data, response) {
            try {
                if (data.url.indexOf('/api/posap/1.0/settings/retailstore') !== -1) {
                    const txt = await response.text();
                    fetch('http://localhost:19879/retail', {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: txt
                    }).then(resp => {
                        console.log("Retail save result: " + resp.status);
                    });
                } else if (data.url.indexOf('/api/posap/1.0/entity/assortment') !== -1) {
                    const txt = await response.text();
                    fetch('http://localhost:19879/products', {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: txt
                    }).then(resp => {
                        console.log("Products save result: " + resp.status);
                    });
                }
            } catch (e) {
                console.warn('Kassa interceptor error')
                console.warn(e);
            }
    }
}