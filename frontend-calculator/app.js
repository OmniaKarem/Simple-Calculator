import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.js'

const app = Vue.createApp({
    data(){
        return{
            result:'',
        }
    },

    methods: {
        addOp(op){
            this.result += op;
        },

        deleteAll(){
            this.result = '';
        },

        omit(){
            this.result = this.result.substring(0 , (this.result.length)-1);
        },

        operate(){
            fetch("http://localhost:8080/operate" , {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json'
                },
                body: JSON.stringify(this.result)
            }).then(res => {
                return res.text()
            })
            .then(data => {
                console.log(data);
                this.result += " = "
                this.result += data
            })
            .catch(error => console.log('E'))
        }
    },
})

app.mount('.app')