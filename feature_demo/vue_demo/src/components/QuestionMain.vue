<template>
    <div>
        <v-md-preview :text="text"></v-md-preview>
    </div>
</template>
<script>
/* eslint-disable */
import req from "@/util/request"
export default
    {
        name: "QuestionMain",
        data() {
            return {
                text: ""
            };
        }
        , methods:
        {
            updateText(id) {
                req.get({ url: "file/question", params: { "id": id } }).then(res => {
                    this.text = res.data;

                }).catch(err => {
                    debugger
                    console.log(err);
                });
            }
        }
    }


</script>