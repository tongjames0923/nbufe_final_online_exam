<template>
  <v-md-preview :text="text"></v-md-preview>
</template>

<script>
export default {
	methods:{
		readFile(filePath) {
		    // 创建一个新的xhr对象
		    let xhr = null
		    if (window.XMLHttpRequest) {
		      xhr = new XMLHttpRequest()
		    } else {
		      // eslint-disable-next-line
		      xhr = new ActiveXObject('Microsoft.XMLHTTP')
		    }
		    const okStatus = document.location.protocol === 'file' ? 0 : 200
		    xhr.open('GET', filePath, false)
		    xhr.overrideMimeType('text/html;charset=utf-8')
		    xhr.send(null)
		    return xhr.status === okStatus ? xhr.responseText : null
		  }
	},
  data() {
    return {
      text:"hello",
    };
  },
  created() {
  	this.text=this.readFile("test.md");
	this.$forceUpdate();
	//alert("changed");
  }
};
</script>