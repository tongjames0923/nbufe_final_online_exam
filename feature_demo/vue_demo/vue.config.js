/* eslint-disable */
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // resolve: {
  //   // https://github.com/babel/babel/issues/8462
  //   // https://blog.csdn.net/qq_39807732/article/details/110089893
  //   // 如果确认需要node polyfill，设置resolve.fallback安装对应的依赖
  //   fallback: {
  //     "querystring": require.resolve("querystring-es3")
  //   }}
})
