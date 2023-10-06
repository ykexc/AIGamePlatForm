<template>
  <NavaBar/>

  <n-card
      title="📖 介绍"
      embedded
      :bordered="false"
      style="max-width: 60vw; margin: 10vh auto"
  >
    <div style="font-weight: bold; font-size: 17px; letter-spacing: 2px">
      本平台是一个AI游戏对抗平台,目前已经开发两款游戏 <router-link to="/snake/pk" style="color: rgba(48,143,8,0.5)">绕蛇</router-link>  和<router-link style="color: #b88230" to="/gobang/pk">五子棋</router-link>
      ,玩家可以操控自己的代码(bot)实现人人、人与bot、bot和bot的在线对战,
      目前bot代码的语言支持Java,C++,本平台
      <el-link href="https://www.ykexc.xyz/2022/05/06/AIGamePlatform/" :underline="false" style="font-size: 17px; font-weight: bold; color: #b74522">
      具体说明以及代码示例
    </el-link>.
    </div>

    <hr />

    <div>
      <p style="font-size: 17px; font-weight: bold; color: #3db686">前端技术栈:&nbsp;&nbsp;Vue3、Vite、TypeScript、Pinia、vue3-ace-editor、axios、element-plus、naiveui、canvas</p>
      <p style="font-size: 17px; font-weight: bold; color: #932685; margin-top: 3vh">后端技术栈:&nbsp;&nbsp;SpringBoot、Mysql、Redis、RabbitMQ、minio、SpringSecurity、mybatis-plus、websocket、Django、Docker</p>
    </div>

    <div style="margin-top: 5vh;font-weight: bold; font-size: 17px; letter-spacing: 2px">
      由于还在开发中， 本平台可能有着各种奇怪的问题，遇到这些问题时，你可以通过下面的方式联系作者。
      <div >
        <el-link :underline="false" style="font-size: 17px; font-weight: bold; color: #b74522" href="mailto:728787402@qq.com">
          728787402@qq.com
        </el-link>
      </div>
    </div>

  </n-card>
</template>


<script lang="ts" setup>


import NavaBar from "@/components/NavaBar.vue";
</script>


<style scoped>

</style>