<template>

  <div class="my-loading" v-if="compile">
    <div class="loading">
      <span class="dot"></span>
      <span class="dot"></span>
      <span class="dot"></span>
    </div>
    <p class="text">{{text}}</p>
  </div>


  <div class="playground" v-if="!compile">
    <GameMap/>
  </div>
</template>

<script setup lang="ts">
import pkUseStore from '@/stores/snake/pk'
import GameMap from "@/components/pk/snake/GameMap.vue";
import {storeToRefs} from 'pinia';
import userUseStore from '@/stores/user'
import {computed} from "vue";

const pkStore = pkUseStore(), userStore = userUseStore()
const {compile, aBotId, bBotId, a_id, b_id} = storeToRefs(pkStore)
const {id} = storeToRefs(userStore)
const text = computed(() => {
  if ((parseInt(id.value) === a_id.value && aBotId.value === -1) || (parseInt(id.value) === b_id.value && bBotId.value === -1)) {
    return "对方代码正在编译中"
  } else {
    return "代码正在编译中"
  }
})


</script>


<style scoped>


.my-loading {
  height: 30vh;
  width: 30vw;
  position: absolute;
  top: 37vh;
  left: 35vw; /*top left right bottom 一般搭配position使用将位置确定*/
}

.playground {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
}

.text {
  margin-top: 20px; /* 文本和点之间的间距 */
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  font-style: italic;
  background: linear-gradient(45deg, #78f3c2, #4888a4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.dot {
  width: 30px;
  height: 30px;
  margin: 0 15px;
  border-radius: 50%;
  animation: loading 1s ease-in-out infinite;
}

.dot:nth-child(1) {
  background-color: #ffcc00;
}

.dot:nth-child(2) {
  background-color: #ff9933;
  animation-delay: 0.2s;
}

.dot:nth-child(3) {
  background-color: #ff6600;
  animation-delay: 0.4s;
}

@keyframes loading {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);
  }
}
</style>