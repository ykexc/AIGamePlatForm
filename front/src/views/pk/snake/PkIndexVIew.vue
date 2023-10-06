<template>
  <NavaBar/>
  <component :is="component"/>
  <ResultBoard v-if="loser !== 'none' && status === 'playing'"/>

</template>


<script lang="ts" setup>


import pkUseStore from '@/stores/snake/pk'
import userUseStore from '@/stores/user'
import recordUseStore from '@/stores/snake/record'
import {computed, h, onMounted} from "vue";
import MatchGround from "@/components/pk/snake/MatchGround.vue";
import PlayGround from "@/components/pk/snake/PlayGround.vue";
import ResultBoard from "@/components/pk/snake/ResultBoard.vue";
import image from "@/enums/Image";
import {ElMessage, ElNotification} from "element-plus";
import NavaBar from "@/components/NavaBar.vue";

const pkStore = pkUseStore()
const userStore = userUseStore()
const recordStore = recordUseStore()
const {updateLoser, updateSocket, updateGame, updateStatus, updateOpponent, updateCompile} = pkStore
const {updateIsRecord} = recordStore
const status = computed(() => {
  return pkStore.status
})
const gameAId = computed(() => {
  return pkStore.a_id
})
const userId = computed(() => {
  return parseInt(userStore.id)
})
const loser = computed(() => {
  return pkStore.loser
})
const component = computed(() => {
  switch (status.value) {
    case 'playing':
      return PlayGround
    case 'matching':
      return MatchGround
  }
})
const socketUrl = `ws://127.0.0.1:3000/websocket/snake/${userStore.token}/`
let socket: WebSocket
onMounted(() => {
  updateOpponent({
    opponent_username: "我的对手",
    opponent_photo: image.rival
  })
  updateLoser('none')
  updateIsRecord(false)
  socket = new WebSocket(socketUrl)
  if (socket.CONNECTING) ElMessage.warning('websocket连接失败,请刷新页面')
  if (socket.OPEN) ElMessage.success("websocket连接成功")
  socket.onopen = (): void => {
    updateSocket(socket)
  }
  socket.onmessage = (message) => {
    const data = JSON.parse(message.data);
    const event = data.event
    if (event === 'start-matching') {
      updateOpponent({
        opponent_photo: data.opponent_photo,
        opponent_username: data.opponent_username
      })
      updateGame(data.game)
      updateStatus('playing', data.aBotId, data.bBotId)
      if (data.aBotId != -1 || data.bBotId != -1) {
        //console.log(data.aBotId + "  " + data.bBotId)
        updateCompile(true)
        setTimeout(() => {
          updateCompile(false)
          notification()
        }, 1500)
      } else {
        notification()
      }
    } else if (event === 'move') {
      const directionA = data.a_direction, directionB = data.b_direction
      const game = pkStore.gameObject
      if (game != null) {
        const [snake0, snake1] = game.snakes
        snake0.set_direction(directionA)
        snake1.set_direction(directionB)
      }
    } else if (event === 'result') {
      const game = pkStore.gameObject
      const loser = data.loser
      if (game != null) {
        const [snake0, snake1] = game.snakes
        switch (loser) {
          case 'all':
            snake0.status = 'die'
            snake1.status = 'die'
            break
          case 'A':
            snake0.status = 'die'
            break
          case 'B':
            snake1.status = 'die'
            break
        }
      }
      updateLoser(data.loser)
    }
  }
})
const notification = (): void => {
  const msg = userId.value === gameAId.value ? "你出生在左下角" : "你出生在右上角"
  const color = userId.value === gameAId.value ? "#2BD5D5" : "#EE1111"
  ElNotification({
    title: '游戏开始',
    message: h('i', {style: 'color: ' + color}, msg),
    duration: 3000
  })
}
</script>


<style scoped>

</style>