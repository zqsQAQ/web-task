<template>
  <div>
    <!-- <a href="../../../assets/allIcon.jpg" download="img">同源保存为图片</a> -->
    <div @click="down">不同源保存为图片</div>
  </div>
</template>

<script>
export default {
  data() {
    return {
        
    };
  },
  methods: {
    down() {
      // 保存二维码
      this.downloadIamge(this.srcImg, "pic");
    },
    downloadIamge(imgsrc, name) {
      // 下载图片地址和图片名
      var image = new Image();
      // 解决跨域 Canvas 污染问题
      image.setAttribute("crossOrigin", "anonymous");
      image.onload = function () {
        var canvas = document.createElement("canvas");
        canvas.width = image.width;
        canvas.height = image.height;
        var context = canvas.getContext("2d");
        context.drawImage(image, 0, 0, image.width, image.height);
        var url = canvas.toDataURL("image/png"); // 得到图片的base64编码数据
        var a = document.createElement("a"); // 生成一个a元素
        var event = new MouseEvent("click"); // 创建一个单击事件
        a.download = name || "photo"; // 设置图片名称
        a.href = url; // 将生成的URL设置为a.href属性
        a.dispatchEvent(event); // 触发a的单击事件
      };
      image.src = imgsrc;
    },
  },
};
</script>

<style>
</style>