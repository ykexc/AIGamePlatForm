<template>
  <div class="result-board">
    <div class="result-board-text" v-if="props.loser === 'all'">
      Draw
    </div>
    <div class="result-board-text" v-else-if="props.loser === 'A' && userId === aId" >
      Lose
    </div>
    <div class="result-board-text" v-else-if="props.loser === 'B' && userId === bId">
      Lose
    </div>
    <div class="result-board-text" v-else>
      Win
    </div>
    <div class="btn">
      <n-button color="#5ba585" @click="doRestart" round size="large">
        再来
      </n-button>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Props {
  userId: string
  aId: string
  bId: string
  loser: string
}

const props = defineProps<Props>()
const emit = defineEmits<{
  (e: 'restart'): void
}>()

const doRestart = () => {
  emit("restart")
}

</script>


<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 30vh;
  left: 35vw;  /*top left right bottom 一般搭配position使用将位置确定*/
}
div.result-board-text {
  text-align: center;
  background: linear-gradient(45deg, #78f3c2, #4888a4);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}
.btn {
  text-align: center;
  padding-top: 7vh;
}
</style>