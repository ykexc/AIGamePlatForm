<template>
  <NavaBar/>
  <component :is="component"/>
  <ResultBoard v-if="loser !== 'none' && status !== 'matching'"/>
</template>


<script setup lang="ts">
import NavaBar from "@/components/NavaBar.vue";
import PlayGround from "@/components/pk/gobang/PlayGround.vue";
import MatchGround from "@/components/pk/gobang/MatchGround.vue";
import userUseStore from '@/stores/user'
import useGobangPkStore from '@/stores/gobang/pk'
import {storeToRefs} from 'pinia'
import {computed, onMounted, h} from "vue";
import image from "@/enums/Image";
import {ElMessage, ElNotification} from "element-plus";
import ResultBoard from "@/components/pk/gobang/ResultBoard.vue";

const userStore = userUseStore()
const pkStore = useGobangPkStore()
const socketUrl = `ws://127.0.0.1:3000/websocket/gobang/${userStore.token}/`
const {status, round, loser, aId} = storeToRefs(pkStore)
const {id} = storeToRefs(userStore)
const {updateGobangGame, updateStatus, updateSocket, updateOpponent, updateRound, updateLoser, updateCompile} = pkStore
let socket: WebSocket
onMounted(() => {
  updateOpponent({
    opponent_username: "我的对手",
    opponent_photo: image.rival
  })
  socket = new WebSocket(socketUrl)
  if (socket.CONNECTING) ElMessage.warning('websocket连接失败,请刷新页面')
  if (socket.OPEN) ElMessage.success("websocket连接成功")
  socket.onopen = (): void => {
    updateSocket(socket)
  }
  socket.onmessage = message => {
    const data = JSON.parse(message.data)
    if (data.event === 'start-matching') {
      updateOpponent({
        opponent_username: data.opponent_username,
        opponent_photo: data.opponent_photo
      })
      updateRound(data.game.round)
      updateGobangGame(data.game)
      updateStatus('playing', data.aBotId, data.bBotId)
      if (data.aBotId !== -1 || data.aBotId !== -1) {
        updateCompile(true)
        setTimeout(() => {
          updateCompile(false)
          notification()
        }, 1500)
      } else {
        notification()
      }
    } else if (data.event === 'play') {
      let nextRound: string = data.round
      let nx: number = data.nx, ny = data.ny
      const game = pkStore.gameObject
      if (game != null) {
        const [white, black] = game.chesses
        if (round.value === 'A') {
          white.chess[nx][ny] = 1
          black.chess[nx][ny] = 1
        } else if (round.value === 'B') {
          white.chess[nx][ny] = 2
          black.chess[nx][ny] = 2
        }
        updateRound(nextRound)
      }
    } else if (data.event === 'result') {
      let overRound = data.round
      let nx: number = data.nx, ny = data.ny
      let loser = data.loser
      let aBotId = data.aBotId, bBotId = data.bBotId
      const game = pkStore.gameObject
      if (game != null) {
        const [white, black] = game.chesses
        if (overRound === 'A') {
          white.chess[nx][ny] = 1
          black.chess[nx][ny] = 1
        } else if (round.value === 'B') {
          white.chess[nx][ny] = 2
          black.chess[nx][ny] = 2
        }
      }
      updateStatus('finished', aBotId, bBotId)
      updateLoser(loser)
    } else if (data.event === 'timeoutResult') {
      let loser = data.loser
      let aBotId = data.aBotId, bBotId = data.bBotId
      updateStatus('finished', aBotId, bBotId)
      updateLoser(loser)
    }
  }
})

const component = computed(() => {
  switch (status.value) {
    case "matching":
      return MatchGround
    default: return PlayGround
  }
})

const notification = () => {
  ElNotification({
    title: '游戏开始',
    message: h('i', {style: 'color: ' + '#2BD5D5'}, getMessage()),
    duration: 3000
  })
}

const getMessage = (): string =>{
  let player: string = (id.value === aId.value.toString() ? '白色' : '黑色')
  let round : string = (player === '白色' ? '先' : '后');
  return '您为' + player + '方' + '您' + round + '手' + ',' + '考虑时间为1分钟'
}

</script>

<style scoped>

</style>