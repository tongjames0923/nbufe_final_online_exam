<template>
	<v-md-preview :text="text"></v-md-preview>
</template>

<script>
	import axios from 'axios';
	export default {
		methods: {
			async readFile(filePath) {
				var da = await axios.get(filePath);
				return da.data;
			}
		},
		data() {
			return {
				text: "hello",
			};
		},
		created() {
			var that = this;
			this.readFile("test.md").then(function(res) {
				that.text = res;
				that.$forceUpdate();

			})；
		}
	};
</script>
