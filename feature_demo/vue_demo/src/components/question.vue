<!-- eslint-disable vue/multi-word-component-names -->
<template>
    <div>
        <v-md-editor v-model="text" height="400px" @save=saveFile></v-md-editor>
    </div>
</template>

<script>
/* eslint-disable */
import axios from 'axios';
export default {
    data() {
        return {
            text: '',
        };
    },
    methods:
    {
        stringToBytes(str) {

            var ch, st, re = [];
            for (var i = 0; i < str.length; i++) {
                ch = str.charCodeAt(i);  // get char  
                st = [];                 // set up "stack"  

                do {
                    st.push(ch & 0xFF);  // push byte to stack  
                    ch = ch >> 8;          // shift value down by 1 byte  
                }

                while (ch);
                // add stack contents to result  
                // done because chars have "wrong" endianness  
                re = re.concat(st.reverse());
            }
            // return an array of bytes  
            return re;
        },

        saveFile(text, html) {
            let objectUrl = window.URL.createObjectURL(new Blob([text], { type: 'text/plain;chartset=UTF-8' }));
            let a = document.createElement('a');
            a.href = objectUrl;
            a.download = 'file.md';
            a.click()
            a.remove();
        }
    },
    mounted() {
        axios.get("README.md").then(res => {
            this.text = res.data;
        });
    }
}
</script>

<style>

</style>