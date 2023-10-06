<template>
  <div class="game-map" ref="parent">
    <div style="margin-right: 5vw">
      <el-image src="https://cdn.acwing.com/media/article/image/2023/03/11/36510_56da3170bf-白子.png"/>
      <span style="font-weight: bold">{{ whiteName }}</span>
    </div>
    <canvas ref="canvas">
    </canvas>
    <div style="margin-left: 5vw">
      <span style="font-weight: bold">{{ blackName }}</span>
      <el-image src="https://cdn.acwing.com/media/article/image/2023/03/11/36510_5423e0d4bf-黑子.png"/>
    </div>
  </div>
  <div class="round" v-if="status === 'playing' && !is_record">
    <div style="font-weight: bold">{{ nowRound }}</div>
    <el-avatar :src="nowRoundImage"/>
  </div>
</template>


<script setup lang="ts">
import {computed, onMounted, ref} from "vue";
import {GameMap} from "@/assets/scripts/gobang/GameMap";
import useGobangPkStore from '@/stores/gobang/pk'
import useGobangRecordStore from '@/stores/gobang/record'
import useUserStore from '@/stores/user'
import {storeToRefs} from "pinia";

const parent = ref(), canvas = ref(), button = ref()
const gobangStore = useGobangPkStore()
const gobangRecordStore = useGobangRecordStore()
const userStore = useUserStore()
const {updateGameObject} = gobangStore
const {is_record, a_username, b_username} = storeToRefs(gobangRecordStore)
onMounted(() => {
  updateGameObject(new GameMap(
      parent.value,
      canvas.value.getContext('2d'),
      button.value, gobangStore,
      userStore,
      gobangRecordStore))
})
const {aId, bId, opponent_username, round, opponent_photo, status} = storeToRefs(gobangStore)
const {id, username, photo} = storeToRefs(userStore)
const whiteName = computed(() => {

  if (is_record.value) return a_username.value

  if (aId.value.toString() === id.value)
    return username.value
  return opponent_username.value
})

const blackName = computed(() => {

  if (is_record.value) return b_username.value

  if (bId.value.toString() === id.value)
    return username.value
  return opponent_username.value
})

// console.log('now round is: ' + round.value)

const nowRoundImage = computed(() => {

  if (is_record.value) return ''

  if (round.value === 'A' && id.value === aId.value.toString())
    return photo.value
  else if (round.value === 'B' && id.value === bId.value.toString())
    return photo.value
  return opponent_photo.value
})
const nowRound = computed(() => {

  if (is_record.value) return ''

  if (round.value === 'A')
    return `当前回合${whiteName.value}`
  else if (round.value === 'B')
    return `当前回合${blackName.value}`
  else return '当前无对局'
})

</script>

<style scoped>
.game-map {
  display: flex;
  width: 100%;
  height: 100%;
  justify-content: center;
  align-items: center;
}

.round {
  margin-bottom: 10px;
  text-align: center;
}

</style>