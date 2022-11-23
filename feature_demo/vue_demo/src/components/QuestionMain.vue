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
                req({ url: "file/question", method: "get", params: { "id": id } }).then(res => {
                    this.text = res.data;
                }).catch(err => {
                    console.log(err);
                });
            }
        }
    }


</script>